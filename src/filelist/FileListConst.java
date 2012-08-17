package filelist;

import java.awt.Color;

public class FileListConst {
	public static Color focused = Color.gray;
	public static Color [] focused_grad = {focused, Color.darkGray};	
	
	public static Color selected = Color.lightGray;
	public static Color [] selected_grad = {selected, Color.gray};

	public static Color listened = new Color(240, 128, 128);
	public static Color [] listened_grad = {listened, Color.white};

	public static Color droped = new Color(232, 196, 0);
	public static Color [] droped_grad = {droped, Color.white};

	public static Color played = new Color(144, 238, 144);	
	public static Color [] played_grad = {played, Color.white};
	
	public static Color usual = new Color(98, 173, 248);	
	public static Color [] usual_grad = {Color.white, usual};	
}