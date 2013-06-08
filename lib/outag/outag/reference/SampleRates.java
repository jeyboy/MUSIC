package outag.reference;

public class SampleRates {
//	static int [] rates = { 8000, 11025, 16000, 22050, 32000, 44056, 44100, 47250, 48000, 50000, 50400, 88200, 96000, 176400, 192000, 352000, 2822400, 5644800 };
	static int [] rates = {	6000, 8000, 9600, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000, 64000, 88200, 96000, 192000 };
	
	public static int getRate(int index) { return rates[index]; }
}