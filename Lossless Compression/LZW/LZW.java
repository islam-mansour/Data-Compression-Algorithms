import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LZW {
	
	private HashMap<String, Integer> Dictionary = new HashMap<String, Integer>();
	private Vector<Integer> compressed = new Vector<Integer>();
	private HashMap<Integer, String> invertDictionary = new HashMap<Integer, String>();
	
	public HashMap<Integer, String> getInvertDictionary() {
		return invertDictionary;
	}

	public void setInvertDictionary(HashMap<Integer, String> invertDictionary) {
		this.invertDictionary = invertDictionary;
	}

	public HashMap<String, Integer> getDictionary() {
		return Dictionary;
	}

	public void setDictionary(HashMap<String, Integer> dictionary) {
		Dictionary = dictionary;
	}

	public Vector<Integer> getCompressed() {
		return compressed;
	}

	public void setCompressed(Vector<Integer> compressed) {
		this.compressed = compressed;
	}



	public Vector<Integer> Compress(String text) {
		int count = 128;
		
		for (int i = 0; i < 128; i++) {
			String temp = "";
			temp += (char) i;
			Dictionary.put(temp, (int) temp.charAt(0));
			setDictionary(Dictionary);
		}
		//previous hold the previous value
		String previous = "", f = "";
		char current;//hold the current value
		
		for (int j = 0; j < text.length(); j++) {
			current = text.charAt(j);
			previous += current;
			
			if (!getDictionary().containsKey(previous)) {
				Dictionary.put(previous, count);
				setDictionary(Dictionary);
				count++;
				f = previous.substring(0, previous.length() - 1);
				compressed.addElement((int) getDictionary().get(f));
				setCompressed(compressed);
				previous = Character.toString(current);
			}
			
			if (j == text.length() - 1 && (getDictionary().containsKey(previous))) {
				compressed.addElement((int) getDictionary().get(previous));
				setCompressed(compressed);
			}
		}
		for (int i = 0; i < getCompressed().size(); i++)
			System.out.println(getCompressed().elementAt(i));
		return compressed;
	}

	public String decompress() {
		Vector<Integer> compressed = new Vector<Integer>();
		int count = 128;
		
		for (int i = 0; i < count; i++) {
			String temp = "";
			temp += (char) i;
			invertDictionary.put((int) temp.charAt(0), temp);
			setInvertDictionary(invertDictionary);
		}
		String out = "", tag = "", prevt = "", add = "";
		int pre = -1, current = -1;
		
		for (int i = 0; i < getCompressed().size(); i++) {
			if (getInvertDictionary().containsKey(getCompressed().elementAt(i))) {
				out += getInvertDictionary().get(getCompressed().elementAt(i)).toString();
				pre = current;
				current = getCompressed().elementAt(i);
				if (pre != -1) {
					prevt = getInvertDictionary().get(pre);// Previous tag
					tag = getInvertDictionary().get(current);// Current tag
					add = prevt + tag.charAt(0);
					invertDictionary.put(count++, add);
					setInvertDictionary(invertDictionary);
				}
			} else {
				tag = getInvertDictionary().get(current);// the new Current
				add = tag + tag.charAt(0);// To handle the case which i have a tag is not in the dic
				out += add;// output of the decompress
				count++;// to get the New TAG
				current = count;
				invertDictionary.put(count, add);
				setInvertDictionary(invertDictionary);
			}
		}
		System.out.println(out);
		return out;
	}

}
// ABAABABBAABAABAAAABABBBBBBBB
