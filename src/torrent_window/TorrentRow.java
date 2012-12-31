package torrent_window;

import java.io.File;
import java.util.LinkedHashMap;

import components.MainWnd;

import service.Common;
import service.Errorist;
import service.IOOperations;
import jBittorrentAPI.DownloadManager;
import jBittorrentAPI.TorrentFile;
import jBittorrentAPI.TorrentProcessor;
import jBittorrentAPI.Utils;

public class TorrentRow extends Thread {
	public String savePath;
	public String torrentPath;
	public boolean seeding = false;
	private TorrentModel parentModel;

	private String torrentName;
	private String complete = "0 %";
	private String downSpeed = "Unknow";
	
	public TorrentRow(TorrentModel parent, String torrentFilePath, String saveDirectory) {
		savePath = saveDirectory;
		torrentPath = torrentFilePath;
		parentModel = parent;
		
		this.setDaemon(true);
		this.start();
	}
	
	public void run() { 
		File torrentFile = new File(torrentPath);
        TorrentProcessor tp = new TorrentProcessor();
        TorrentFile tf = tp.getTorrentFile(tp.parseTorrent(torrentFile), savePath);       
        torrentName = IOOperations.name_without_extension(torrentFile.getName());
        
        DownloadManager dm = new DownloadManager(tf, Utils.generateID());
        parentModel.AddTorrent(this);

        dm.startListening(6882, 6889);
        dm.startTrackerUpdate();

        if (seeding)
        	dm.blockUntilCompletion(this);
        else
        	dm.blockUntilDownload(this);
    	

        dm.stopTrackerUpdate();
        dm.closeTempFiles();
       
        // проверка, куда были сохранены скачанные данные (то поле, которое задается в TorrentProcessor.setName())
//        String torrentSavedTo = tp.getTorrentFile(tp.parseTorrent(torrentPath)).saveAs;			
	}
	
	public void UpdateInfo(float progress, float speed) {
		complete = Common.formatter.format(progress) + " %";
		downSpeed = "" + speed;
		parentModel.UpdateRow(this);
	}
	
	public String Name() { return torrentName; }
	public String Progress() { return complete; }
	public String DownSpeed() { return downSpeed; }	
}