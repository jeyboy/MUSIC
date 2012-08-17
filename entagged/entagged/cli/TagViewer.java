package entagged.cli;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.generic.TagField;
import entagged.audioformats.mp3.util.id3frames.ApicId3Frame;

public class TagViewer {
    public static void main(String[] args) throws Exception{
		for(int i = 0; i<args.length; i++) {
		    File file = new File(args[i]);
		    
			System.out.println("Tag content: "+file);
			try {
				AudioFile af = AudioFileIO.read(file);
				System.out.println(af);
				
				List l = af.getTag().get("APIC");
				if(l == null)
				    continue;
				
				Iterator it = l.iterator();
				while(it.hasNext()) {
				    TagField f = (TagField) it.next();
				    ApicId3Frame pic = (ApicId3Frame) f;
				    byte[] data = pic.getData();
			        Image img = ImageIO.read(new ByteArrayInputStream(data));
			        System.out.println(img);
			        
			        JFrame frame = new JFrame("Image viewer");
			        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
			        frame.setVisible(true);
			        frame.setSize(800, 600);
			        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				}
			} catch(Exception e) {
			    System.out.println(e);
			}
			System.out.println("------------------------------\n");
		}
	}
}
