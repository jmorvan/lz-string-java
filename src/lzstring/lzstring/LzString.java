package lzstring.lzstring;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lzstring.datatypes.Context;
import lzstring.exceptions.DecompressException;
import lzstring.streaming.BitInputStream;
import lzstring.streaming.BitOutputStream;

public class LzString {

	public static String decompress(String compressed) throws DecompressException {

		String output = null;

		try {
			output =  decompress(new ByteArrayInputStream(compressed.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

	public static String decompress(InputStream compressed) throws IOException, DecompressException {

		BitInputStream bis = new BitInputStream(compressed);

		Map<Integer, String> dict = new HashMap<Integer, String>();

		int next, enlargeIn = 4, dictSize = 4, numBits = 3, errorCount = 0;
		int c;

		String entry = ""; String w = "";

		StringBuffer result = new StringBuffer();

		for(int i = 0; i < 3; i++)
			dict.put(i, null);

		next = bis.readBits(2);

		if(next == 0 || next == 1)
			c = bis.readBits((next + 1) * 8);
		else
			return "";

		dict.put(3, Character.toString((char)c));

		w = Character.toString((char) c);
		result.append(w);

		while(true){

			c = bis.readBits(numBits);

			if(c == 0 || c == 1){

				if(errorCount++ > 10000)
					throw new DecompressException("This is not a compressed string.");

				c = bis.readBits((c + 1) * 8);
				dict.put(dictSize++, Character.toString((char) c));
				c = dictSize - 1;
				enlargeIn--;
			}
			else if(c == 2)
				return result.toString();

			if(enlargeIn == 0)
				enlargeIn = 1 << numBits++;

			if(dict.get(c) != null)
				entry = dict.get(c);
			else {
				if(c == dictSize)
					entry = w + w.charAt(0);
				else
					return null;
			}

			result.append(entry);

			dict.put(dictSize++, w + entry.charAt(0));
			enlargeIn--;

			w = entry;

			if(enlargeIn == 0)
				enlargeIn = 1 << numBits++;
		}
	}

	private static void produceW(Context context) throws IOException {

		Map<String, Integer> dict = context.getDictionary();
		Set<String> cDict = context.getDictionaryToCreate();

		String w = context.getW();
		BitOutputStream bos = context.getData();

		int numBits = context.getNumBits();
		int enlargeIn = context.getEnlargeIn();

		if(cDict.contains(w)){

			int charCode = Character.codePointAt(w, 0);
			boolean notUnicode = charCode < 256;

			bos.writeBits(numBits, notUnicode? 0 : 1);
			bos.writeBits(notUnicode? 8 : 16, charCode);

			enlargeIn = (--enlargeIn == 0)? 1 << numBits++ : enlargeIn;

			cDict.remove(w);
		}
		else {

			bos.writeBits(numBits, dict.get(w));
		}

		enlargeIn = (--enlargeIn == 0)? 1 << numBits++ : enlargeIn;

		context.setEnlargeIn(enlargeIn);
		context.setNumBits(numBits);
	}

	public static byte[] compress(String uncompressed) {

		byte[] output = null;

		try {
			output =  compress(new ByteArrayInputStream(uncompressed.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

	public static byte[] compress(InputStream uncompressed) throws IOException {

		Context context = new Context();
		int c;

		Map<String, Integer> dict = context.getDictionary();

		// Create a character stream from the binary input stream.
		InputStreamReader isr = new InputStreamReader(uncompressed);

		while((c = isr.read()) != -1){

			String nextCharacter = Character.toString((char) c);

			if(!context.getDictionary().containsKey(nextCharacter)){

				dict.put(nextCharacter, 3 + dict.size());

				context.getDictionaryToCreate().add(nextCharacter);
			}

			context.setWc(context.getW() + nextCharacter);

			if(context.getDictionary().containsKey(context.getWc()))
				context.setW(context.getWc());
			else {

				produceW(context);

				dict.put(context.getWc(), 3 + dict.size());
				context.setW(nextCharacter);
			}
		}

		if(!context.getW().equals(""))
			produceW(context);

		context.getData().writeBits(context.getNumBits(), 2);
		uncompressed.close();

		return context.getData().getContent();
	}

	public static void main(String args[]) throws Exception{

		String test = "Lets see how much we can compress this string!";
		
		byte[] output = LzString.compress(test);

		System.out.println("Original: " + test);
		System.out.println("Compressed: " + new String(output, "UTF-16"));
		System.out.println("Decompressed: " + LzString.decompress(new ByteArrayInputStream(output)));
	}
}
