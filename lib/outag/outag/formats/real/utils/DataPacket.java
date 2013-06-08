package outag.formats.real.utils;

import java.io.DataInputStream;

// TODO: Check which class correct ( At this moment no one realization is not correct)

/** Variant 1 */
public class DataPacket {
//	 word   Packet version (0 or 1 in available samples)
//	 word   Packet size
//	 word   Stream number
//	 dword  Timestamp (in ms)
//	 byte   Unknown
//	 byte   Flags (bitfield, see below)
//	#if version == 1
//	 byte   Unknown
//	#endif
//	 byte[]  Stream-specific data
	
	public byte [] packetData;
	public short streamID;
	public int timestamp;
	/**<ol start="0">
	 * 	<li>bit : reliable packet (refers to network transmission method)</li>
    	<li>bit : keyframe</li> 
	 * </ol> */
	public byte flags;
	
	public DataPacket(DataInputStream f) throws Exception {
		short packetVersion = f.readShort(); // 0 or 1
		if (packetVersion < 0 || packetVersion > 1) return; // Stream done if packet version not equal 0 or 1
		
		packetData = new byte[f.readShort()];
		streamID = f.readShort();
		timestamp = f.readInt();
		f.read(); // unknow byte
		flags = f.readByte();
		if (packetVersion == 1)
			f.read(); // unknow byte
		f.readFully(packetData);
	}
}

/** Alternative variant (official documentation) */
class DataPacket2 {
//	 word   Packet version
//	 word   Packet size
//	 word   Stream number
//	 dword  Timestamp
//	#if version == 0
//	 byte   Packet group
//	 byte   Flags
//	#endif
//	#if version == 1
//	 word   ASM rule
//	 byte   ASM flags
//	#endif
//	 byte[]  Stream-specific data
	
	public byte [] packetData;
	public short streamID;
	public int timestamp;
	public byte flags;
	public byte packetGroup;
	public short ASM_rule;
	
	public DataPacket2(DataInputStream f) throws Exception {
		short packetVersion = f.readShort(); // 0 or 1
		if (packetVersion < 0 || packetVersion > 1) return; // Stream done if packet version not equal 0 or 1 
		
		packetData = new byte[f.readShort()];
		streamID = f.readShort();
		timestamp = f.readInt();
		
		if (packetVersion == 0)
			packetGroup = f.readByte();
		else
			ASM_rule = f.readShort();
		flags = f.readByte();

		f.readFully(packetData);
	}	
}


///////////////////////Lossless//////////////////////////

//Frame format
//
//Data is read MSB.
//
//block header
//channel data 1
//if (stereo)
//  channel data 2
//
//Each channel is then restored by applying filter and in stereo case channels are recombined.
//
//[edit]
//Block header
//
//get frame length code
//if (stereo)
//  decoding_mode = get_bits(2) + 1;
//else
//  decoding_mode = 0;
//
//Frame length code:
//
//  64 - 111110
// 128 - 111111
// 256 - 11110
// 512 - 1110
//1024 - 110
//2048 - 10
//4096 - 0
//
//Decoding mode specifies table sets, bits and channel recombining:
//Mode 	tableset1 	bits1 	tableset2 	bits2 	Channel decoupling
//0 	0 	16 	n/a 	n/a 	mono
//1 	0 	16 	0 	16 	L, R
//2 	0 	16 	2 	17 	L+R, R
//3 	1 	16 	2 	17 	L, R-L
//4 	2 	17 	2 	17 	(L+R)/2, (L-R)/2
//[edit]
//Block data
//[edit]
//Variable-length codes
//
//RALF uses hybrid coding scheme for signed integers: coded value in range 1..N-1 maps to -N/2+1..N/2-1 and values 0 and N are used as escape values. In case an escape value is occurred, an additional Elias Gamma code (aka exp-Golomb) is read and added to or subtracted from the result. Also some low bits can be read explicitly.
//
//extend_code(code, range, add_bits)
//{
//  if (code == 0) {
//    len = get_unary(); // 000...001
//    code = -range + 1 - get_bits(len + 1);
//  } else if (code == range*2) {
//    len = get_unary(); // 000...001
//    code = range - 1 + get_bits(len + 1);
//  } else {
//    code -= range;
//  }
//  if (add_bits > 0)
//    code = (code << add_bits) | get_bits(add_bits);
//  return code;
//}
//
//[edit]
//Channel data
//
//Channel data is organised that way:
//
//filter_parameter = get_vlc(filter_parameter_table);
//if (filter_parameter == 642) {
//    bias_val = 0;
//    all samples are coded raw
//}
//bias_val = get_vlc(bias_table);
//bias_val = extend_code(bias_val, 127, 4); // input is 0..254, make it signed, read escape values if needed and low 4 bits
//if (filter_parameter == 0) {
//    all samples are zero
//    return
//}
//if (filter_parameter > 1) {
//    filter_bits = (filter_parameter - 2) >> 6; // range 0..9
//    filter_len  = filter_parameter - 1 - (filter_bits << 6); // range 1..64
//    coeff = 0;
//    coeff_mode = 0;
//    for (i = 0; i < filter_len; i++) {
//        t = get_vlc(filt_coef_table[filter_bits][coeff_mode + 5]);
//        t = extend_code(t, 21, filter_bits);
//        if (coeff_mode == 0)
//            coeff -= 12 << filter_bits;
//        coeff = t - coeff;
//        filter[i] = coeff;
//        coeff_mode = coeff >> filter_bits;
//        if (coeff_mode < 0)
//            coeff_mode = max(-5, -1 - log2(-coeff_mode));
//        else if (coeff_mode > 0)
//            coeff_mode = min(5, 1 + log2(coeff_mode));
//    }
//}
//coding_mode = get_vlc(coding_mode_tree);
//if (coding_mode >= 15) { // big codes
//    bits = (coding_mode / 5 - 3) / 2;
//    if (bits > 9) {
//        bits = 9;
//        if ((coding_mode % 5) == 2)
//            bits++;
//    }
//    for (i = 0; i < num_samples; i += 2) {
//        t = get_vlc(long_codes_trees[coding_mode - 15]);
//        val1 = t / 21;
//        val2 = t - val1 * 21;
//        val1 = extend_code(val1, 10, 0);
//        val2 = extend_code(val2, 10, 0);
//        if (bits == 0) {
//            dst[i]     = val1;
//            dst[i + 1] = val2;
//        } else {
//            dst[i]     = (val1 << bits) | get_bits(bits);
//            dst[i + 1] = (val2 << bits) | get_bits(bits);
//        }
//    }
//} else {
//    for (i = 0; i < num_samples; i += 2) {
//        t = get_vlc(short_codes_trees[coding_mode]);
//        val1 = t / 13;
//        val2 = t - val1 * 13;
//        val1 = extend_code(val1, 6, 0);
//        val2 = extend_code(val2, 6, 0);
//        dst[i]     = val1;
//        dst[i + 1] = val2;
//    }
//}
//
//[edit]
//Restoring channel data
//
//   apply LPC filter
//   add bias value to all samples
//   re-correlate stereo channels if needed 
//
//[edit]
//LPC filter
//
//filter_bits = ((filter_parameter - 2) >> 6) + 3;
//range_bits  = bits_in_channel; // i.e. 16 or 17
//bias        = 1 << (filter_bits - 1);
//pos_clip    = (1 << range_bits) - 1;
//neg_clip    = ~pos_clip;
//
//for (i = 0; i < num_samples; i++) {
//    acc = 0;
//    for (j = 0; j < min(i, filter_length); j++)
//        acc += filter[j] * src[i - j - 1];
//    if (acc < 0)
//        val = (acc + bias - 1) >> filter_bits;
//    else
//        val = (acc + bias) >> filter_bits;
//    val = clip(val, neg_clip, pos_clip);
//    src[i] += val;
//}