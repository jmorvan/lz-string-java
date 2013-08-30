package lzstring.datatypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lzstring.streaming.BitOutputStream;

public class Context {

	private Map<String, Integer> dictionary = new HashMap<String, Integer>();
	private Set<String> dictionaryToCreate = new HashSet<String>();

	private String wc = "", w = "", result = "";

	private BitOutputStream data = new BitOutputStream();

	private int enlargeIn = 2, dictSize = 3, numBits = 2;

	public BitOutputStream getData(){

		return data;
	}

	public Map<String, Integer> getDictionary() {
		
		return dictionary;
	}

	public Set<String> getDictionaryToCreate() {
		
		return dictionaryToCreate;
	}

	public String getWc() {
		
		return wc;
	}

	public void setWc(String wc) {
		
		this.wc = wc;
	}

	public String getW() {
		
		return w;
	}

	public void setW(String w) {
		
		this.w = w;
	}

	public String getResult() {
		
		return result;
	}
	public void setResult(String result) {
		
		this.result = result;
	}

	public int getEnlargeIn() {
		
		return enlargeIn;
	}

	public void setEnlargeIn(int enlargeIn) {
		
		this.enlargeIn = enlargeIn;
	}

	public int getDictSize() {
		
		return dictSize;
	}

	public void setDictSize(int dictSize) {
		
		this.dictSize = dictSize;
	}

	public int getNumBits() {
		
		return numBits;
	}

	public void setNumBits(int numBits) {
		
		this.numBits = numBits;
	}
}
