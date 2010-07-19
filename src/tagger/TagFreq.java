package tagger;

/**
 * This class represents the frequency of a tag. It contains the tag and
 * its occurrence count.
 * @author Or Shwartz
 */
public class TagFreq implements Comparable<TagFreq> {

	private int freq;
	private String tagName;

	public TagFreq(int freq, String tagName) {
		this.freq = freq;
		this.tagName = tagName;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getFreq() {
		return freq;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	/**
	 * Returns a negative integer if this is less frequent than the other.
	 * 0 is returned if the frequencies are equal and a positive integer
	 * is returned if this is more frequent than the other.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TagFreq otherTagFreq) {
		
		// Return the comparison result 
		return this.getFreq() - otherTagFreq.getFreq();
	}
}
