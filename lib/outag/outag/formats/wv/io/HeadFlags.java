package outag.formats.wv.io;

import java.io.IOException;

import outag.file_presentation.Parseable;
import outag.reference.SampleRates;

//bits 1,0:   // 00 = 1 byte / sample (1-8 bits / sample)
//    // 01 = 2 bytes / sample (1-16 bits / sample)
//    // 10 = 3 bytes / sample (1-24 bits / sample)
//    // 11 = 4 bytes / sample (1-32 bits / sample)
//bit 2:      // 0 = stereo output; 1 = mono output
//bit 3:      // 0 = lossless mode; 1 = hybrid mode
//bit 4:      // 0 = true stereo; 1 = joint stereo (mid/side)
//bit 5:      // 0 = independent channels; 1 = cross-channel decorrelation
//bit 6:      // 0 = flat noise spectrum in hybrid; 1 = hybrid noise shaping
//bit 7:      // 0 = integer data; 1 = floating point data
//bit 8:      // 1 = extended size integers (> 24-bit) or shifted integers
//bit 9:      // 0 = hybrid mode parameters control noise level
//    // 1 = hybrid mode parameters control bitrate
//bit 10:     // 1 = hybrid noise balanced between channels
//bit 11:     // 1 = initial block in sequence (for multichannel)
//bit 12:     // 1 = final block in sequence (for multichannel)
//bits 17-13: // amount of data left-shift after decode (0-31 places)
//bits 22-18: // maximum magnitude of decoded data
//    //  (number of bits integers require minus 1)
//bits 26-23: // sampling rate (1111 = unknown/custom)
//bits 27-28: // reserved (but decoders should ignore if set)
//bit 29:     // 1 = use IIR for negative hybrid noise shaping
//bit 30:     // 1 = false stereo (data is mono but output is stereo)
//bit 31:     // reserved (decoders should refuse to decode if set)

public class HeadFlags {
//	private int  SHIFT_LSB   = 13;
//	private long SHIFT_MASK  = (0x1fL << SHIFT_LSB);
//	private int  SRATE_LSB   = 23;
//	private long SRATE_MASK  = (0xfL << SRATE_LSB);	
	
	
	int flags;
	int sampleRateIndex;
	int dataAmount;
	public int bitsPerSample;
	public boolean isStereo;
	public boolean isLossless;
	public boolean isJoinStereo;
	public boolean isIndependentChannels;
	public boolean isPseudoStereo;	
	
	
	public HeadFlags(Parseable p) throws IOException {
		flags = p.UInt();
		
		bitsPerSample = ((Parseable.getBit(flags, 0) * 2) + Parseable.getBit(flags, 1)) + 1;
		isStereo = Parseable.getBit(flags, 2) == 0;
		isLossless = Parseable.getBit(flags, 3) == 0;
		isJoinStereo = Parseable.getBit(flags, 4) == 1;
		isIndependentChannels = Parseable.getBit(flags, 5) == 0;
		dataAmount = Parseable.calcByBits(flags, 13, 4);
	
		sampleRateIndex = Parseable.calcByBits(flags, 23, 4);		
		isPseudoStereo = Parseable.getBit(flags, 30) == 1;
	}
	
	public int getSampleRate() {
		return sampleRateIndex < 15 ? SampleRates.getRate(sampleRateIndex) : -1;
	}
	
	public int getBitsPerSample() {
		return (int) (((flags & 3) + 1) * 8 - dataAmount);
//			((flags & SHIFT_MASK) >> SHIFT_LSB));
	}
}