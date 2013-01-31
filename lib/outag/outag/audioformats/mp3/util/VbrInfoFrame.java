package outag.audioformats.mp3.util;

public interface VbrInfoFrame {
	public int getFrameCount();
	public boolean isValid();
	public boolean isVbr();
	public int getFileSize();
}