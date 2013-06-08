package outag.formats.ogg.util;

public class OggPageHeader {
	private double absoluteGranulePosition;
	private byte[] checksum;
	private byte headerTypeFlag;

	private boolean isValid = false;
	private int pageLength = 0;
	private int pageSequenceNumber,streamSerialNumber;
	private byte[] segmentTable;

	public OggPageHeader(byte[] b) {
		int streamStructureRevision = b[4];
		headerTypeFlag = b[5];
		if (streamStructureRevision == 0) {
			this.absoluteGranulePosition = 0;  //b[6] + (b[7]<<8) + (b[8]<<16) + (b[9]<<24) + (b[10]<<32) + (b[11]<<40) + (b[12]<<48) + (b[13]<<56);
			for (int i = 0; i < 8; i++)
				this.absoluteGranulePosition += u(b[i + 6]) * Math.pow(2, 8 * i);

			streamSerialNumber = u(b[14]) + (u(b[15]) << 8) + (u(b[16]) << 16) + (u(b[17]) << 24);
			pageSequenceNumber = u(b[18]) + (u(b[19]) << 8) + (u(b[20]) << 16) + (u(b[21]) << 24);
			checksum = new byte[]{b[22], b[23], b[24], b[25]};

			//int pageSegments = u(b[26]);
			//System.err.println("pageSegments: " + pageSegments);

			this.segmentTable = new byte[b.length - 27];
			//System.err.println("pagesegment length; "+ (b.length-27));
			for (int i = 0; i < segmentTable.length; i++) {
				segmentTable[i] = b[27 + i];
				this.pageLength += u(b[27 + i]);
			}

			isValid = true;
		}
	}
	
	private int u(int i) { return i & 0xFF; }

	public double getAbsoluteGranulePosition() { return this.absoluteGranulePosition; }

	public byte[] getCheckSum() { return checksum; }

	public byte getHeaderType() { return headerTypeFlag; }

	public int getPageLength() { return this.pageLength; }
	
	public int getPageSequence() { return pageSequenceNumber; }
	
	public int getSerialNumber() { return streamSerialNumber; }

	public byte[] getSegmentTable() { return this.segmentTable; }

	public boolean isValid() { return isValid; }

	public String toString() {
		String out = "Ogg Page Header:\n";

		out += "Is valid?: " + isValid + " | page length: " + pageLength + "\n";
		out += "Header type: " + headerTypeFlag;
		return out;
	}
}