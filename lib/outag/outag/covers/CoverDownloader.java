package outag.covers;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CoverDownloader {
	CoverSource[] sources;
	
	public CoverDownloader() { this.sources = new CoverSource[] { new AmazonCoverSource() }; }
	public CoverDownloader(CoverSource[] sources) { this.sources = sources; }
	
	public List search(String artist, String album) {
		List list = new LinkedList();
		
		for(int i = 0; i<sources.length; i++) {
			try {
				List results = sources[i].search(artist, album);
				list.addAll(results);
			} 
			catch(CoverException e) {
				//Do not add the results of the source since we got an exception
			}
		}
		
		Collections.sort(list);
		return list;
	}
	
	public static void main(String[] args) {
		CoverDownloader cd = new CoverDownloader();
		
		List covers = cd.search("yes","yessongs");
		
		System.out.println(covers.size()+" matches");
		
		Iterator it = covers.iterator();
		int i = 0;
		while(it.hasNext()) {
			Cover cover = (Cover) it.next();
			System.out.println(cover);
			try{
				cover.saveAs(new File("/home/kikidonk/test-"+i+".jpg"));
			} catch(CoverException e) {
				System.err.println("Could not save cover: \n"+cover);
			}
			i++;
		}
	}
}