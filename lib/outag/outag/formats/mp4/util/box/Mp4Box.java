package outag.formats.mp4.util.box;

import outag.file_presentation.Parseable;

public class Mp4Box {
    private String id;
    private int length;
    
    public Mp4Box() {}
    public Mp4Box(String id) { this.id = id; }
    
    public String getId() { return id; }
    
    public int getLength() { return length - getHeadLength(); }
    
    public int getHeadLength() { return 8; }
    
    public String getEncoding() { return "UTF-8"; }

    public void init(Parseable f) throws Exception {
    	length = f.UBEInt();
        id = f.Str(4);
        if (length == 1) { // means what uses extra size fields because block size bigger than 2^32
        	throw new Exception("Long block size unsupported");
//        	length = f.ULong();
        }
    }
    
    public boolean find(Parseable f, boolean throw_error, String ... box_ids) throws Exception {
    	if (box_ids.length == f.available()) return false;
    	
    	init(f);
    	
    	if (box_ids.length == 0) return true;
    	
    	try {
	    	while(f.available() > 0) {
	    		for(String box_id : box_ids)
	    			if (getId().equals(box_id))
	    				return true;
	    		
	    		f.skip(getLength());
	    		init(f);
	    	}
    	}
    	catch(Exception e) {  }
    	if (throw_error) throw new Error("Not find compatible headers");
    	return false;
    } 
    
    public static Mp4Box init(Parseable f, boolean throw_error, String ... box_ids) throws Exception {
    	Mp4Box box = new Mp4Box();
    	box.find(f, throw_error, box_ids);
    	return box;
    }
}