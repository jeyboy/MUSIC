package outag.formats.mp4.util;

import java.io.RandomAccessFile;

import outag.file_presentation.JBBuffer;
import outag.file_presentation.JBFile;
import outag.formats.EncodingInfo;
import outag.formats.mp4.util.box.Mp4AlacBox;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4BoxIdentifier;
import outag.formats.mp4.util.box.Mp4DrmsBox;
import outag.formats.mp4.util.box.Mp4EsdsBox;
import outag.formats.mp4.util.box.Mp4FtypBox;
import outag.formats.mp4.util.box.Mp4MdhdBox;
import outag.formats.mp4.util.box.Mp4Mp4aBox;
import outag.formats.mp4.util.box.Mp4MvhdBox;
import outag.formats.mp4.util.box.Mp4StsdBox;

public class Mp4InfoReader {
	
//    private boolean isTrackAtomVideo(Mp4BoxHeader boxHeader, ByteBuffer mvhdBuffer) throws IOException {
//        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4AtomIdentifier.MDIA.getFieldName());
//        if (boxHeader == null)
//        {
//            return false;
//        }
//        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4AtomIdentifier.MDHD.getFieldName());
//        if (boxHeader == null)
//        {
//            return false;
//        }
//        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
//        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4AtomIdentifier.MINF.getFieldName());
//        if (boxHeader == null)
//        {
//            return false;
//        }
//        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4AtomIdentifier.VMHD.getFieldName());
//        if (boxHeader != null)
//        {
//            return true;
//        }
//        return false;
//    }	
	
	/** Read audio info from file. <br>
	 * The info is held in the mvdh and mdhd fields as shown below
	 * <pre>
	 * |--- ftyp
	 * |--- moov
	 * |......|
	 * |......|----- mvdh
	 * |......|----- trak
	 * |...............|----- mdia
	 * |.......................|---- mdhd
	 * |.......................|---- minf
	 * |..............................|---- smhd
	 * |..............................|---- stbl
	 * |......................................|--- stsd
	 * |.............................................|--- mp4a
	 * |......|----- udta
	 * |
	 * |--- mdat
	 * </pre> 
	 * @throws Exception */	
    public EncodingInfo read(RandomAccessFile raf) throws Exception {
        EncodingInfo info = new EncodingInfo();
        Mp4Box box = new Mp4Box();
        JBFile file = new JBFile(raf);

        box.find(file, true, Mp4BoxIdentifier.FTYP.getName());

        Mp4FtypBox ftyp = new Mp4FtypBox(file.buffer(box.getLength()));
        info.setEncodingType(ftyp.getMajorBrand());

        //Get to the facts everything we are interested in is within the moov box, so just load data from file
        //once so no more file I/O needed        
        box.find(file, true, Mp4BoxIdentifier.MOOV.getName());       

        JBBuffer moovBuffer = file.buffer(box.getLength());

        //Level 2-Searching for "mvhd" somewhere within "moov", we make a slice after finding header
        //so all get() methods will be relative to mvdh positions
        
        box.find(moovBuffer, true, Mp4BoxIdentifier.MVHD.getName());
        
        JBBuffer mvhdBuffer = moovBuffer.slice();
        Mp4MvhdBox mvhd = new Mp4MvhdBox(mvhdBuffer);
        info.setLength(mvhd.getLength());        

        //Advance position, TODO should we put this in box code ?
//        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        mvhdBuffer.pos(box.getLength());

        //Level 2-Searching for "trak" within "moov"
        box.find(mvhdBuffer, true, Mp4BoxIdentifier.TRAK.getName());

        //Level 3-Searching for "mdia" within "trak"
        box.find(mvhdBuffer, true, Mp4BoxIdentifier.MDIA.getName());

        //Level 4-Searching for "mdhd" within "mdia"
        
        box.find(mvhdBuffer, true, Mp4BoxIdentifier.MDHD.getName());

        Mp4MdhdBox mdhd = new Mp4MdhdBox(mvhdBuffer.slice());
        info.setSamplingRate(mdhd.getSampleRate());

        /*/Level 4-Searching for "hdlr" within "mdia"
        //We dont currently need to process this because contains nothing we want
        mvhdBuffer.skip(box.getLength());
        
        box.find(mvhdBuffer, Mp4BoxIdentifier.HDLR.getName());

        Mp4HdlrBox hdlr = new Mp4HdlrBox(mvhdBuffer.slice());*/


        //Level 4-Searching for "minf" within "mdia"
        mvhdBuffer.skip(box.getLength());
        
        box.find(mvhdBuffer, true, Mp4BoxIdentifier.MINF.getName());

        //Level 5-Searching for "smhd" within "minf"
        //Only an audio track would have a smhd frame
        
        if (!box.find(mvhdBuffer, false, Mp4BoxIdentifier.SMHD.getName(), Mp4BoxIdentifier.VMHD.getName()))
        	throw new Exception("Find no audio and no video box");

        mvhdBuffer.skip(box.getLength());
        
        //Level 5-Searching for "stbl within "minf"
        box.find(mvhdBuffer, true, Mp4BoxIdentifier.STBL.getName());

        //Level 6-Searching for "stsd within "stbl" and process it direct data, don't think these are mandatory so don't throw exception if unable to find
        
        if (box.find(mvhdBuffer, false, Mp4BoxIdentifier.STSD.getName())) {
            Mp4StsdBox stsd = new Mp4StsdBox(mvhdBuffer);       	

            ///Level 7-Searching for "mp4a within "stsd"
        	if (box.find(mvhdBuffer, false, Mp4BoxIdentifier.MP4A.getName(), Mp4BoxIdentifier.DRMS.getName(), Mp4BoxIdentifier.ALAC.getName())) {
        		switch(box.getId()) {
        			case "mp4a" : 
        				JBBuffer mp4aBuffer = mvhdBuffer.slice();
        				Mp4Mp4aBox mp4a = new Mp4Mp4aBox(mp4aBuffer);
                      
                      	//Level 8-Searching for "esds" within mp4a to get No Of Channels and bitrate
                      	if (box.find(mp4aBuffer, false, Mp4BoxIdentifier.ESDS.getName())) {
                          	Mp4EsdsBox esds = new Mp4EsdsBox(mp4aBuffer.slice());
      
                          	info.setBitrate(esds.getAvgBitrate() / 1000);
                          	info.setChannelNumber(esds.getNumberOfChannels());
                          	info.setEncodingType("AAC " + esds.getAudioProfile() + " - " + esds.getKind());
                      	}        				
        				break;
        			case "drms" :
        				Mp4DrmsBox drmsbox = new Mp4DrmsBox(mvhdBuffer);

        				//Level 8-Searching for "esds" within drms to get No Of Channels and bitrate
        				if (box.find(mvhdBuffer, false, Mp4BoxIdentifier.ESDS.getName())) {
        					Mp4EsdsBox esds = new Mp4EsdsBox(mvhdBuffer.slice());
                          
        					info.setBitrate(esds.getAvgBitrate() / 1000);
        					info.setChannelNumber(esds.getNumberOfChannels());
        					info.setEncodingType("DRM_AAC " + esds.getAudioProfile() + " - " + esds.getKind());                    	                      	  
        				}        				
        				break;
        			case "alac" : 
        				Mp4AlacBox alac = new Mp4AlacBox(mvhdBuffer);
                      
        				//Level 8-Searching for 2nd "alac" within box that contains the info we really want
                        if (box.find(mvhdBuffer, false, Mp4BoxIdentifier.ALAC.getName())) {
                        	alac = new Mp4AlacBox(mvhdBuffer);
                        	info.setEncodingType("APPLE_LOSSLESS");
                        	info.setChannelNumber(alac.getChannels());
                        	info.setBitrate(alac.getBitRate()/1000);
                        }        				
        				break;
        		}
        	}	
        }
        

        //Set default channels if couldn't calculate it
        if (info.getChannelNumber() == -1)
            info.setChannelNumber(2);

        //Set default bitrate if couldn't calculate it
        if (info.getBitrate() == -1)
            info.setBitrate(128);

        //This is the most likely option if can't find a match
        if (info.getEncodingType().equals(""))
            info.setEncodingType("AAC");
//
//        logger.config(info.toString());
//
//        //Level 2-Searching for others "trak" within "moov", if we find any traks containing video
//        //then reject it if no track if not video then we allow it because many encoders seem to contain all sorts
//        //of stuff that you wouldn't expect in an audio track
//        mvhdBuffer.position(endOfFirstTrackInBuffer);
//        while(mvhdBuffer.hasRemaining())
//        {
//            boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4AtomIdentifier.TRAK.getFieldName());
//            if (boxHeader != null)
//            {
//                if(isTrackAtomVideo(ftyp,boxHeader,mvhdBuffer))
//                {
//                    throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
//                }
//            }
//            else
//            {
//                break;
//            }
//        }

        return info;
    }
}