package outag.formats.real;

import outag.formats.generic.GenericTag;

public class RealTag extends GenericTag {
	String copyright;
	
	public void setCopyright(String cI) { copyright = cI; }
	public String getCopyright() { return copyright; }
	
    public String toString() {
        String output = "REAL " + super.toString();
        return output;
    }
}