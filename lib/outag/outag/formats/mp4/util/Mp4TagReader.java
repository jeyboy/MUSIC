package outag.formats.mp4.util;

import java.io.RandomAccessFile;
import outag.file_presentation.JBBuffer;
import outag.file_presentation.JBFile;
import outag.formats.Tag;
import outag.formats.mp4.Mp4Tag;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4BoxIdentifier;
import outag.formats.mp4.util.box.Mp4MetaBox;
import outag.formats.mp4.util.tag.Mp4TagField;

/**
 * Reads metadata from mp4,
 * <p/>
 * <p>The metadata tags are usually held under the ilst atom as shown below<p/>
 * <p>Valid Exceptions to the rule:</p>
 * <p>Can be no udta atom with meta rooted immediately under moov instead<p/>
 * <p>Can be no udta/meta atom at all<p/>
 *
 * <pre>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |......|----- udta
 * |..............|
 * |..............|-- meta
 * |....................|
 * |....................|-- hdlr
 * |....................|-- ilst
 * |.........................|
 * |.........................|---- @nam (Optional for each metadatafield)
 * |.........................|.......|-- data
 * |.........................|....... ecetera
 * |.........................|---- ---- (Optional for reverse dns field)
 * |.................................|-- mean
 * |.................................|-- name
 * |.................................|-- data
 * |.................................... ecetere
 * |
 * |--- mdat
 * </pre
 */
public class Mp4TagReader {
	public Tag read(RandomAccessFile raf) throws Exception {
		Mp4Tag tag = new Mp4Tag();
        Mp4Box box = new Mp4Box();
        JBFile file = new JBFile(raf);
        
        box.find(file, true, Mp4BoxIdentifier.MOOV.getName());       
        JBBuffer moovBuffer = file.buffer(box.getLength());		
		       
        if (box.find(moovBuffer, false, Mp4BoxIdentifier.UDTA.getName(), Mp4BoxIdentifier.META.getName())) {
        	switch(box.getId()) {
        		case "udta" :
        			box.find(moovBuffer, true, Mp4BoxIdentifier.META.getName());
        			new Mp4MetaBox(moovBuffer);       			
        			box.find(moovBuffer, true, Mp4BoxIdentifier.ILST.getName());
        			
        			break;
        		case "meta" :
        			new Mp4MetaBox(moovBuffer);
        			box.find(moovBuffer, true, Mp4BoxIdentifier.ILST.getName());        			
        			
        			break;
        	}
        	        	
        	while(box.find(moovBuffer, false)) {
        		Mp4TagField.parse(tag, box, moovBuffer);
        		moovBuffer.skip(box.getLength());
        	}
        }

        return tag;		
	} 
}