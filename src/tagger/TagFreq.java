package tagger;


/**
 * This class represents the frequency of a tag. It contains the tag and
 * its occurrence count.
 * @author Or Shwartz
 */
public class TagFreq implements Comparable<TagFreq> {

	private int freq;
	private String tagName;

	/**
	 * Creates a tag frequency.
	 * @param freq The frequency of the tag.
	 * @param tagName The tag.
	 */
	public TagFreq(int freq, String tagName) {
		this.freq = freq;
		this.tagName = tagName;
	}
	
	/**
	 * Creates this object from a string representation of the object.
	 * The format is:<BR><I>tag (frequency)</I>.
	 * @param stringRepresentation String representation of the object.
	 */
	public TagFreq(String stringRepresentation) {
		
		int indexOfFreq = stringRepresentation.lastIndexOf('(') + 1; 
		
		this.tagName =
			stringRepresentation.substring(0, indexOfFreq - 2);
		this.freq =
			Integer.parseInt(stringRepresentation.
								substring(indexOfFreq,
										  stringRepresentation.length() - 1));
	}

	/**
	 * Frequency setter.
	 * @param freq The frequency to set.
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}

	/**
	 * Frequency getter.
	 * @return The frequency of the tag.
	 */
	public int getFreq() {
		return freq;
	}

	/**
	 * Tag name setter.
	 * @param tagName The tag to set.
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Tag getter.
	 * @return The tag.
	 */
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

	/**
	 * Return string representation of this object formatted as:<BR><CODE>tag (frequency)</CODE>
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return String.format("%s (%d)", getTagName(), getFreq());
	}
}
