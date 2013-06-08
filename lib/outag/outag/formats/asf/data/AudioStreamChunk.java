package outag.formats.asf.data;

import java.math.BigInteger;

import outag.formats.asf.util.Utils;

public class AudioStreamChunk extends StreamChunk {
    /** Stores the hex values of codec identifiers to their descriptions. */
    public final static String[][] CODEC_DESCRIPTIONS = {
            { "161", " (Windows Media Audio (ver 7,8,9))" },
            { "162", " (Windows Media Audio 9 series (Professional))" },
            { "163", "(Windows Media Audio 9 series (Lossless))" },
            { "7A21", " (GSM-AMR (CBR))" }, { "7A22", " (GSM-AMR (VBR))" } };

    /** codec number for WMA */
    public final static long WMA = 0x161;
    /** codec number for WMA (CBR) */
    public final static long WMA_CBR = 0x7A21;
    /** codec number for WMA_LOSSLESS */
    public final static long WMA_LOSSLESS = 0x163;
    /** codec number for WMA_PRO */
    public final static long WMA_PRO = 0x162;
    /** codec number for WMA (VBR) */
    public final static long WMA_VBR = 0x7A22;    
    
    /** Stores the average amount of bytes used by audio stream. <br>
     * This value is a field within type specific data of audio stream. Maybe it
     * could be used to calculate the kbps. */
    private long averageBytesPerSec;

    /** Amount of bits used per sample. */
    private int bitsPerSample;

    /** The block alignment of the audio data. */
    private long blockAlignment;

    /** Number of channels. */
    private long channelCount;

    /** Some data which needs to be interpreted if the codec is handled. */
    private byte[] codecData;

    /** The audio compression format code. */
    private long compressionFormat;

    /** this field stores the error concealment type. */
    private GUID errorConcealment;

    /** Sampling rate of audio stream. */
    private long samplingRate;

    /** Creates an instance.
     * @param pos
     *                   Position of current chunk within asf file or stream.
     * @param chunkLen
     *                   Length of the entire chunk (including guid and size) */
    public AudioStreamChunk(long pos, BigInteger chunkLen) { super(pos, chunkLen); }

    /** @return Returns the averageBytesPerSec. */
    public long getAverageBytesPerSec() 				{ return averageBytesPerSec; }
    /** @param avgeBytesPerSec */
    public void setAverageBytesPerSec(long avgeBytesPerSec) { this.averageBytesPerSec = avgeBytesPerSec; }    

    /** @return Returns the bitsPerSample. */
    public int getBitsPerSample() 						{ return bitsPerSample; }
    /** Sets the bitsPerSample 
     * @param bps */
    public void setBitsPerSample(int bps) 				{ this.bitsPerSample = bps; }    

    /** @return Returns the blockAlignment. */
    public long getBlockAlignment() 					{ return blockAlignment; }
    /**Sets the blockAlignment.
     * @param align */
    public void setBlockAlignment(long align) 			{ this.blockAlignment = align; }    

    /** @return Returns the channelCount. */
    public long getChannelCount() 						{ return channelCount; }
    /** @param channels The channelCount to set. */
    public void setChannelCount(long channels) 			{ this.channelCount = channels; }    

    /** @return Returns the codecData. */
    public byte[] getCodecData() 						{ return codecData; }
    /** Sets the codecData<br>
     * @param codecSpecificData */
    public void setCodecData(byte[] codecSpecificData) 	{ this.codecData = codecSpecificData; }

    /** This method will take a look at {@link #compressionFormat}and returns a
     * String with its hex value and if known a textual note on what coded it
     * represents. <br>
     * 
     * @return A description for the used codec. */
    public String getCodecDescription() {
        StringBuffer result = new StringBuffer(Long
                .toHexString(getCompressionFormat()));
        String furtherDesc = " (Unknown)";
        for (int i = 0; i < CODEC_DESCRIPTIONS.length; i++) {
            if (CODEC_DESCRIPTIONS[i][0].equalsIgnoreCase(result.toString())) {
                furtherDesc = CODEC_DESCRIPTIONS[i][1];
                break;
            }
        }
        
        result.insert(0, ((result.length() % 2 != 0) ? "0x0" : "0x"));
        result.append(furtherDesc);
        return result.toString();
    }

    /** @return Returns the compressionFormat. */
    public long getCompressionFormat() 					{ return compressionFormat; }
    public String getCompressionDescription()			{
    	if (compressionFormat == WMA) 				return "Simple";
    	if (compressionFormat == WMA_CBR) 			return "Constant bitrate";
    	if (compressionFormat == WMA_LOSSLESS)	 	return "Lossless";
    	if (compressionFormat == WMA_PRO)			return "Pro";
    	if (compressionFormat == WMA_VBR)			return "Variable bitrate";
    	return "Unknow";
    }    
    /** @param cFormatCode The compressionFormat to set. */
    public void setCompressionFormat(long cFormatCode) 	{ this.compressionFormat = cFormatCode; }    

    /** @return Returns the errorConcealment. */
    public GUID getErrorConcealment() 					{ return errorConcealment; }
    /** This method sets the error concealment type which is given by two GUIDs.
     * @param errConc the type of error concealment the audio stream is stored as. */
    public void setErrorConcealment(GUID errConc) 		{ this.errorConcealment = errConc; }    
    

    /** This method takes the value of {@link #getAverageBytesPerSec()}and
     * calculates the kbps out of it, by simply multiplying by 8 and dividing by
     * 1000. <br>
     * 
     * @return amount of bits per second in kilo bits. */
    public int getKbps() 								{ return (int) getAverageBytesPerSec() * 8 / 1000; }

    /** @return Returns the samplingRate. */
    public long getSamplingRate() 						{ return samplingRate; }
    /** @param sampRate The samplingRate to set. */
    public void setSamplingRate(long sampRate) 			{ this.samplingRate = sampRate; }    

    /** This mehtod returns whether the audio stream data is error concealed.
     * For now only interleaved concealment is known. <br>
     * @return <code>true</code> if error concealment is used. */
    public boolean isErrorConcealed() {
        return getErrorConcealment().equals(
                GUID.GUID_AUDIO_ERROR_CONCEALEMENT_INTERLEAVED);
    }

    public String prettyPrint() {
        StringBuffer result = new StringBuffer(super.prettyPrint().replaceAll(
                Utils.LINE_SEPARATOR, Utils.LINE_SEPARATOR + "   "));
        result.insert(0, Utils.LINE_SEPARATOR + "AudioStream");
        result.append("Audio info:" + Utils.LINE_SEPARATOR);
        result.append("      Bitrate : " + getKbps() + Utils.LINE_SEPARATOR);
        result.append("      Channels : " + getChannelCount() + " at "
                + getSamplingRate() + " Hz" + Utils.LINE_SEPARATOR);
        result.append("      Bits per Sample: " + getBitsPerSample()
                + Utils.LINE_SEPARATOR);
        result.append("      Formatcode: " + getCodecDescription()
                + Utils.LINE_SEPARATOR);
        return result.toString();
    }
}