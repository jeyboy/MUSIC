package filelist;

import java.awt.Color;

public class FileListConst {
	public static Color selected = Color.black;	
	
	public static Color usual = new Color(98, 173, 248);	
	public static Color [] usual_grad = {Color.white, usual};
	public static Color [] usual_grad_select = {selected, usual};

	public static Color listened = new Color(240, 128, 128);
	public static Color [] listened_grad = {Color.white, listened};
	public static Color [] listened_grad_select = {selected, listened};

	public static Color droped = new Color(232, 196, 0);
	public static Color [] droped_grad = {Color.white, droped};
	public static Color [] droped_grad_select = {selected, droped};

	public static Color played = new Color(144, 238, 144);	
	public static Color [] played_grad = {Color.green, played};
	public static Color [] played_grad_select = {selected, played};		
}