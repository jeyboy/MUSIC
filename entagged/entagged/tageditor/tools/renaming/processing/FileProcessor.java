/*
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */

package entagged.tageditor.tools.renaming.processing;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.HashMap;

import entagged.audioformats.AudioFile;
import entagged.listing.Lister;
import entagged.tageditor.tools.renaming.data.AbstractFile;
import entagged.tageditor.tools.renaming.data.DirectoryDescriptor;
import entagged.tageditor.tools.renaming.data.FileDescriptor;
import entagged.tageditor.tools.renaming.data.ProcessingResult;
import entagged.tageditor.tools.renaming.data.RenameConfiguration;
import entagged.tageditor.tools.renaming.data.stat.Category;
import entagged.tageditor.tools.renaming.data.stat.Prop;
import entagged.tageditor.tools.renaming.data.stat.properties.CanNotWriteProperty;
import entagged.tageditor.tools.renaming.pattern.DirectoryPattern;
import entagged.tageditor.tools.renaming.pattern.FilePattern;
import entagged.tageditor.tools.renaming.pattern.MissingValueException;
import entagged.tageditor.tools.renaming.util.FileSystemUtil;

/**
 * This class is used to determine the file locations.<br>
 * 
 * @author Christian Laireiter
 */
public final class FileProcessor implements Lister {

	/**
	 * This method will replace the bitrate placeholder whithin the
	 * directory-names against the average contained bitrate.<br>
	 * 
	 * @param files
	 *            The Audiofiles to process.
	 * @param copyUnmoveable
	 *            if <code>true</code>, the files which cannot be modified
	 *            will be assumed to be copied.<br>
	 *            So they'll be processed
	 * @return long-Array with two entries.<br>
	 *         index 0: the sum of the bitrate of the contained files.<br>
	 *         index 1: the number of files whose bitrates have been added.<br>
	 */
	public static long[] processBitrate(AbstractFile[] files,
			boolean copyUnmoveable) {
		long sum = 0;
		int count = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i] instanceof FileDescriptor) {
				FileDescriptor currFile = (FileDescriptor) files[i];
				// Only if the file has been readable and contains no
				// Error-Property it will be mentioned.
				if (!currFile.isUnreadable()) {
					Prop[] props = currFile.getStatistic()
							.getPropertiesOfCategory(Category.ERROR_CATEGORY);
					if (props.length <= 1) {
						boolean accept = true;
						// If one error property is set...
						if (props.length == 1) {
							// test if its the cannot write one and if copying
							// is enabled.
							accept = copyUnmoveable
									&& props[0].getName().equals(
											CanNotWriteProperty.PROPERTY_NAME);
						}
						if (accept) {
							count++;
							sum += ((FileDescriptor) files[i]).getBitrate();
						}
					}

				}
			} else {
				DirectoryDescriptor dir = (DirectoryDescriptor) files[i];
				// Only targetdirectories will be processed. The rest woul just
				// be useless work
				if (dir.isTargetDirectory()) {
					long[] result = processBitrate(dir.getChildren(),
							copyUnmoveable);
					sum += result[0];
					count += result[1];
					if (dir.getOriginalName().indexOf(
							DirectoryPattern.INTERNAL_BITRATE_PATTERN) != -1) {
						/*
						 * And now replace the bitrate-Pattern
						 */
						long avg = 0;
						if (count > 0) {
							avg = sum / count;
						}
						String newName = dir.getOriginalName().replaceAll(
								DirectoryPattern.INTERNAL_BITRATE_PATTERN,
								String.valueOf(avg));
						dir.overrideName(newName);
					}
				}
			}

		}
		return new long[] { sum, count };
	}

	/**
	 * If needed, an instance will be stored here, which will extend the
	 * tracknumbers to be at least two digits long.<br>
	 */
	private DecimalFormat decFormatter = null;

	/**
	 * The directory pattern processor.
	 */
	private DirectoryPattern directoryPattern;

	/**
	 * the filename pattern processor.
	 */
	private FilePattern filePatten;

	/**
	 * This field contains the {@link DirectoryDescriptor} objects, which
	 * represent a filesystem root.
	 */
	private HashMap filesysroots;

	/**
	 * This field stores the absolute path names of files to
	 * {@link entagged.tageditor.tools.renaming.data.AbstractFile} instances.
	 */
	private HashMap path2file;

	/**
	 * Stores the object, which recieves the results. In Addition to that it is
	 * the source to resolve conflicts.
	 */
	private final ProcessingResult processingResult;

	/**
	 * This field stores the configuration of the renaming process.
	 */
	private final RenameConfiguration renameConfig;

	/**
	 * Creates an instance.
	 * 
	 * @param result
	 *            The result object, which will recieve the created directory
	 *            tree structure.
	 * @param config
	 *            the Configuration of the renaming operation. (It is used to
	 *            gather the patterns)
	 */
	public FileProcessor(ProcessingResult result, RenameConfiguration config) {
		assert result != null;
		this.processingResult = result;
		this.path2file = new HashMap();
		this.directoryPattern = config.getDirectoryPattern();
		this.filePatten = config.getFilePattern();
		this.renameConfig = config;
		this.filesysroots = new HashMap();
	}

	/**
	 * (overridden) This method will process the given file.
	 * 
	 * @see entagged.listing.Lister#addFile(entagged.audioformats.AudioFile,
	 *      java.lang.String)
	 */
	public void addFile(AudioFile audioFile, String relativePath)
			throws Exception {
		FileDescriptor descriptor = insertSourceFile(audioFile, audioFile
				.getBitrate());
		try {
			prepareTagData(audioFile);
			String[] directories = directoryPattern.createFrom(audioFile, null);
			StringBuffer path = new StringBuffer(directories[0]);
			for (int i = 1; i < directories.length; i++) {
				path.append(File.separatorChar);
				path.append(directories[i]);
			}
			String fileName = filePatten.createFrom(audioFile);
			/*
			 * now we know path and fileName
			 */
			if (!fileName.endsWith(descriptor.getExtension())) {
				descriptor.setTargetName(fileName + "."
						+ descriptor.getExtension());
			} else {
				descriptor.setTargetName(fileName);
			}

			// Now create the DirectoryDescritor for the target
			DirectoryDescriptor targetDirectory = assertDirectory(path
					.toString(), true);
			descriptor.setTargetDirectory(targetDirectory);
			targetDirectory.addChild(descriptor);

		} catch (MissingValueException e) {
			descriptor.setMissingFields(e.getMissingFields());
		}
	}

	/**
	 * (overridden) This method will process the errorneous file.
	 * 
	 * @see entagged.listing.Lister#addFile(java.lang.String)
	 */
	public void addFile(String fileName) throws Exception {
		File file = new File(fileName);
		FileDescriptor descriptor = insertSourceFile(file, -1);
		descriptor.setUnreadable(true);
	}

	/**
	 * This method will ensure that for the given path a
	 * {@link DirectoryDescriptor} object is created and linked to its parents
	 * (which must eventually be created, too).<br>
	 * 
	 * @param path
	 *            The absolute file path, of the directory, which should be
	 *            created.
	 * @param target
	 *            if <code>true</code>, the directory will be additionally
	 *            marked as a target of the processing operation (and its
	 *            parents).
	 * @return The directory descriptor for the given path.
	 * @throws Exception
	 *             If filesystem case sensitivity determination failed, or
	 *             {@link File#getCanonicalPath()} failed.
	 * @see FileSystemUtil#isFilesystemCaseSensitive()
	 */
	private DirectoryDescriptor assertDirectory(String path, boolean target)
			throws Exception {
		assert path != null;
		/*
		 * look into the path2file map, if the given directory already exists.
		 * If the filesystem is not case sensitive, the case must be ignored.
		 * The artist value of two audiofiles may differ only in their case. On
		 * Windows, this would result in the same directory. If the file system
		 * is case sensitive there may exist two directory names which only
		 * differ in case, however, if the configuration searches for similiar
		 * directory names and has decided for an existing or already processed
		 * one dir with one case, this is taken.
		 */
		String searchPath = path;
		if (target
				&& (!FileSystemUtil.isFilesystemCaseSensitive() || renameConfig
						.isSearchSimiliarDirectoriesEnabled())) {
			searchPath = searchPath.toUpperCase();
		}
		DirectoryDescriptor result = (DirectoryDescriptor) path2file
				.get(searchPath);
		if (result != null) {
			/*
			 * If the current directory is not marked as target, but now is
			 * requested as a target, it must be set as one.
			 */
			if (target && !result.isTargetDirectory()) {
				result.setTargetDirectory(true);
				/*
				 * This will result in a upward recursion, marking all parent
				 * directories as used for target. Stop if no parent file exist.
				 */
				DirectoryDescriptor current = result.getParent();
				while (current != null) {
					current.setTargetDirectory(true);
					current = current.getParent();
				}
			}
			// same with source
			if (!target && !result.isSourceDirectory()) {
				result.setSourceDirectory(true);
				DirectoryDescriptor current = result.getParent();
				while (current != null) {
					current.setSourceDirectory(true);
					current = current.getParent();
				}
			}
			return result;
		}
		// From here on, the directory does not exist (In memory).
		File directoryInstance = null;
		if (target)
			directoryInstance = getDirectoryFile(path);
		else
			directoryInstance = new File(path);
		// assert parent directory first.
		// Does a parent exist?
		if (directoryInstance.getParentFile() != null) {
			DirectoryDescriptor parent = assertDirectory(directoryInstance
					.getParent(), target);
			result = new DirectoryDescriptor(directoryInstance.getName(),
					parent, !target, target);
			parent.addChild(result);
		} else {
			// consider, for whatever reason the getName() for a drive (C:\)
			// returns an empty string.
			String rootName = directoryInstance.getAbsolutePath();
			if (rootName.endsWith(File.separator)) {
				rootName = rootName.substring(0, rootName.length() - 1);
			}
			result = new DirectoryDescriptor(rootName, null, !target, target);
			// No, so it is a filesystem root (eg. c:\ or /)
			if (!filesysroots.containsKey(rootName)) {
				filesysroots.put(rootName, result);
			}
		}
		String absolutePath = directoryInstance.getAbsolutePath();
		// cache it for the source case
		path2file.put(absolutePath, result);
		/*
		 * If the filesystem is case insensitive or the case sensitivity was
		 * disabled, just put the uppercase version into the cache.
		 */
		if ((!FileSystemUtil.isFilesystemCaseSensitive() || renameConfig
				.isSearchSimiliarDirectoriesEnabled())) {
			absolutePath = absolutePath.toUpperCase();
			if (!path2file.containsKey(absolutePath))
				path2file.put(absolutePath, result);
		}
		return result;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.listing.Lister#close()
	 */
	public void close() {
		this.processingResult
				.setFileSystemRoots((DirectoryDescriptor[]) filesysroots
						.values().toArray(
								new DirectoryDescriptor[filesysroots.size()]));
		if (directoryPattern.containsBitrate()) {
			// Now perform deep scan of each directory an replace its filename
			// by the average contained bitrate.
			processBitrate(processingResult.getFileSystemRoots(), renameConfig
					.isCopyUnmodifiableFiles());
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.listing.Lister#getContent()
	 */
	public String getContent() {
		// No content needed (Maybe a statistical description)
		return null;
	}

	/**
	 * This method is meant to create a {@link File} object from the given path.
	 * Additionally performs two tasks:<br>
	 * 1. If the underlying filesystem is not case sensitive like "Windows", it
	 * creates the object and returns the canonical file
	 * {@link File#getCanonicalFile()}, if the file exists. This prevents
	 * confusion in the preview. The original case for directory names is taken
	 * from existing directories. Not the one computed.<br>
	 * <br>
	 * 2, For case sensitive file systems (like "linux") it may occur, that the
	 * processed directory name is "scooter" (consider the case). Next to this
	 * non existing directory already exists two directories like "Scooter" and
	 * "SCOOTER". Those two will be found if
	 * {@link RenameConfiguration#isSearchSimiliarDirectoriesEnabled()} returns
	 * <code>true</code>. And if they were found, the one directory is
	 * choosen, with most files in it. If just one similiar directory is found,
	 * this is taken. If the processed directory path exists, it will be kept as
	 * is.<br>
	 * <br>
	 * If the parent file instance of the specified path does not exist, just a
	 * new file object is created. Why? This method is meant to handle only the
	 * last specified directory (or file).
	 * 
	 * @see File#getParentFile()
	 * @param path
	 *            The path for which a file instance is requested.
	 * @return A file instance, which should be used for the given path.
	 * @throws Exception
	 *             On two occasions, first,
	 *             {@link FileSystemUtil#isFilesystemCaseSensitive()} or
	 *             {@link File#getCanonicalFile()} caused an Exception.
	 */
	private File getDirectoryFile(String path) throws Exception {
		File result = new File(path);
		/*
		 * If the parent file does not exist, it is completely unessecary to
		 * determine possible similiar directories.
		 */
		if (result.getParentFile() != null && result.getParentFile().exists()) {
			if (!FileSystemUtil.isFilesystemCaseSensitive()) {
				if (result.exists()) {
					// Case 1
					result = result.getCanonicalFile();
				}
			} else {
				// Case 2,
				if (renameConfig.isSearchSimiliarDirectoriesEnabled()
						&& !result.exists()) {
					// If not exists and search is activated.
					final String directoryName = result.getName();
					File[] similiars = result.getParentFile().listFiles(
							new FileFilter() {
								public boolean accept(File pathname) {
									return pathname.isDirectory()
											&& pathname.getName()
													.equalsIgnoreCase(
															directoryName);
								}
							});
					if (similiars != null && similiars.length > 0) {
						// Now we have got at least one directory next to the
						// requested one, which only differs in the string case.
						if (similiars.length > 1) {
							/*
							 * Here we have multiple directories next to the
							 * requested (non existing) one. So it must be
							 * decided which to take for further processing.
							 * Here we decide for the one which contains most
							 * files.
							 */
							int maxFileCount = -1;
							File maxFile = null;
							for (int i = 0; i < similiars.length; i++) {
								int currentCount = FileSystemUtil
										.countFiles(similiars[i]);
								if (currentCount > maxFileCount) {
									maxFileCount = currentCount;
									maxFile = similiars[i];
								}
							}
							result = maxFile;
						} else {
							/*
							 * Here we have just one similiar directory, which
							 * will be taken as the result.
							 */
							result = similiars[0];
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * This methode ensures the existens of the
	 * {@link entagged.tageditor.tools.renaming.data.DirectoryDescriptor}
	 * objects and the {@link FileDescriptor} object for the given file.
	 * 
	 * @param audioFile
	 *            the file whose source structure should be build.
	 * @param bitrate
	 *            The bitrate in kbps of the file.
	 * @return The FileDescriptor.
	 * @throws Exception
	 *             If {@link #assertDirectory(String, boolean)} throwed one.
	 */
	private FileDescriptor insertSourceFile(File audioFile, int bitrate)
			throws Exception {
		DirectoryDescriptor parent = assertDirectory(audioFile.getParent(),
				false);
		FileDescriptor result = new FileDescriptor(audioFile.getName(), parent,
				bitrate);
		parent.addChild(result);
		return result;
	}

	/**
	 * This method prepares the tag from the given file according to the
	 * configuration.
	 * 
	 * @param audioFile
	 *            The file to be adjusted.
	 */
	private void prepareTagData(AudioFile audioFile) {
		if (renameConfig.isExtendTrackNumbers()) {
			String firstTrack = audioFile.getTag().getFirstTrack();
			try {
				if (firstTrack != null) {
					int number = Integer.parseInt(firstTrack);
					if (decFormatter == null) {
						decFormatter = new DecimalFormat("00");
					}
					audioFile.getTag().setTrack(decFormatter.format(number));
				}
			} catch (NumberFormatException nfe) {
				// Nothing to do
			}
		}
	}
}
