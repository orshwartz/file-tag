package tagger;

public class TagFreq implements Comparable {

	private int freq;
	private String tagName;
	
	public TagFreq(int freq, String tagName){
		this.freq = freq;
		this.tagName = tagName;	
	}
	
	
	public void setFreq(int freq){
		this.freq = freq;
	}
	
	public int getFreq(){
		return freq;
	}
	
	
	public void setTagName(String tagName){
		this.tagName = tagName;
	}
	
	public String getTagName(){
		return tagName;
	}
	
	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
