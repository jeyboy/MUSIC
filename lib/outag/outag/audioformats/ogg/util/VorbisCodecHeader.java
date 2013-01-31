package outag.audioformats.ogg.util;

public class VorbisCodecHeader {
	private int audioChannels;
	private boolean isValid = false;
	
	private int vorbisVersion, audioSampleRate;
	private int bitrateMinimal ,bitrateNominal,bitrateMaximal;

	public VorbisCodecHeader(byte[] vorbisData) { generateCodecHeader(vorbisData); }

	public int getChannelNumber() { return audioChannels; }

	public String getEncodingType() { return "Ogg Vorbis Version " + vorbisVersion;	}

	public int getSamplingRate() { return audioSampleRate; }
	
	public int getNominalBitrate() { return bitrateNominal; }

	public int getMaxBitrate() { return bitrateMaximal; }
	
	public int getMinBitrate() { return bitrateMinimal; }
	
	public boolean isValid() { return isValid; }

	public void generateCodecHeader( byte[] b ) {
		int packetType = b[0];
		//System.err.println("packetType" + packetType);
		String vorbis = new String( b, 1, 6 );
		//System.err.println("vorbis" + vorbis);
		if ( packetType == 1 && vorbis.equals( "vorbis" ) ) {
			this.vorbisVersion = b[7] + ( b[8] << 8 ) + ( b[9] << 16 ) + ( b[10] << 24 );
			//System.err.println("vorbisVersion" +vorbisVersion );
			this.audioChannels = u( b[11] );
			//System.err.println("audioChannels" +audioChannels );
			this.audioSampleRate = u( b[12] ) + ( u( b[13] ) << 8 ) + ( u( b[14] ) << 16 ) + ( u( b[15] ) << 24 );
			//System.err.println("audioSampleRate" + audioSampleRate);
			//System.err.println("audioSampleRate" + b[12] + " "+b[13] +" "+ b[14]);
			
			this.bitrateMinimal = u(b[16]) + ( u(b[17]) << 8 ) + ( u(b[18]) << 16 ) + ( u(b[19]) << 24 );
			this.bitrateNominal = u(b[20]) + ( u(b[21]) << 8 ) + ( u(b[22]) << 16 ) + ( u(b[23]) << 24 );
			this.bitrateMaximal = u(b[24]) + ( u(b[25]) << 8 ) + ( u(b[26]) << 16 ) + ( u(b[27]) << 24 );
			//byte blockSize0 = (byte) ( b[28] & 240 );
			//byte blockSize1 = (byte) ( b[28] & 15 );
			
			int framingFlag = b[29];
			//System.err.println("framingFlag" +framingFlag );
			if ( framingFlag != 0 )
				isValid = true;

		}
	}
	
	private int u(int i) { return i & 0xFF; }
}