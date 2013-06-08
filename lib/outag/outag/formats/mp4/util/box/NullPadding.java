package outag.formats.mp4.util.box;

/** Some mp4s contain null padding at the end of the file, possibly do with gapless playback. This is not really
 * allowable but seeing as seems to occur in files encoded with iTunes 6 and players such as Winamp and iTunes deal
 * with it we should <br>
 * It isn't actually a box, but it helps to keep as a subclass of this type */
public class NullPadding {
    public NullPadding(long startPosition,long fileSize) {
//        setFilePos(startPosition);
//        length=((int)(fileSize - startPosition));
    }
}