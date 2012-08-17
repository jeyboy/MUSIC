/*
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */
package entagged.freedb;

// $Id: FreedbAlbum.java,v 1.1 2007/03/23 14:16:58 nicov1 Exp $
public class FreedbAlbum {

	private FreedbTrack[] trackListing;

	public FreedbAlbum() {
		this(new FreedbTrack[0]);
	}

	public FreedbAlbum(FreedbTrack[] tracks) {
		this.trackListing = tracks;
	}

	public String getDiscId() {
		String out;
		long l = getLongDiscId();

		if (l < 0)
			out = Long.toHexString(l).substring(8);
		else
			out = Long.toHexString(l);
		return out;
	}

	public long getLongDiscId() {
		int tracksNumber = getTracksNumber();
		int t = 0, n = 0;
		int trackOffsets[] = getTrackOffsets();
		int cddbSum = 0;

		for (int i = 0; i < tracksNumber; i++) {
			n = trackOffsets[i] / 75;
			while (n > 0) {
				cddbSum = cddbSum + (n % 10);
				n = n / 10;
			}
		}

		t = (trackOffsets[tracksNumber] - trackOffsets[0]) / 75;

		return ((cddbSum % 0xff) << 24 | t << 8 | tracksNumber);
	}

	public int getTracksNumber() {
		return this.trackListing.length;
	}
	
	/**
	 * This method calculates the offsets of the tracks, as they would appear on
	 * an audio cd, if there are no spaces (empty frames) between them.<br>
	 * The offset of the first song will assumed to be at offset 150.<br>
	 * The array will have the length of the number of tracks plus one. The last
	 * entry will store the offset, where the lead out starts.
	 * 
	 * @return The offsets where the songs would start.
	 */
	public int[] getTrackOffsets() {
		int[] result = new int[getTracksNumber() + 1];
		result[0] = 150;
		for (int i = 1; i < result.length; i++) {
			float frameCount = getTrack(i - 1).getPreciseLength() * 75;
			int intValue = (int)frameCount;
			if ((frameCount-intValue) > 0.5f) {
				intValue++;
			}
			result[i] = result[i - 1] + intValue;
		}
		return result;
	}

	public FreedbTrack getTrack(int i) {
		return this.trackListing[i];
	}

	public void swapTracks(int i1, int i2) {
		FreedbTrack t1 = this.trackListing[i1];
		FreedbTrack t2 = this.trackListing[i2];

		this.trackListing[i1] = t2;
		this.trackListing[i2] = t1;
	}

	public FreedbTrack setTrack(int i, FreedbTrack track) {
		FreedbTrack old = this.trackListing[i];
		this.trackListing[i] = track;
		return old;
	}
}
