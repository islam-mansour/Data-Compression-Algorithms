import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class LZW {
	
	public static ArrayList<String> dictionary;
	public static ArrayList<Integer> indeces;
	public static String data;
//	public static String original;
//	public static String compressed;
//	public static String decompressed;
	
	public static void readData(String filePath)throws IOException {
		data = "";
		FileInputStream file = new FileInputStream(filePath);
		int b;
		while((b = file.read()) != -1) {
			data += (char)b;
		}
		file.close();
	}
	
	public static void write_decompressed(String data, String filePath)throws IOException{
		filePath = filePath.replace(".lzw", ".txt");
//		filePath = filePath.substring(0, filePath.indexOf('.'))+".txt";
		FileWriter file = new FileWriter(filePath);
		file.write(data);
		file.close();
	}
	
	public static void write_indeces(String filePath)throws IOException {
		filePath = filePath.replace(".txt", ".lzw");
//		filePath = filePath.substring(0, filePath.indexOf('.'))+".lzw";
		FileWriter file = new FileWriter(filePath);
		for(Integer index : indeces) {
			file.write(index.toString() + " ");
		}
		file.close();
	}
	
	public static void read_indeces(String filePath)throws IOException {
		indeces = new ArrayList<Integer>();
		FileInputStream file = new FileInputStream(filePath);
		String buffer = "";
		int b;
		while((b = file.read()) != -1) {
			buffer+=(char)b;
		}
		file.close();
		String[] ind = buffer.split(" ");
		for(String i : ind){
			indeces.add(new Integer(i));
		}
	}
	
	public static void buildDictionary() {
		dictionary = new ArrayList<String>();
		for(int i = 0; i < 128; i++) {
			dictionary.add(String.valueOf((char)i));
		}
	}
	
	public static void compress(String filePath)throws IOException {
		readData(filePath);
		buildDictionary();
		indeces = new ArrayList<Integer>();
		String search = "",current = "";
		int index = 0;
		char next;
		while(index < data.length()) {
			next = data.charAt(index);
			current = search + next;
			if(dictionary.contains(current)) {
				search += next;
				if(index == data.length()-1) {
					indeces.add(dictionary.indexOf(search));
				}
				index++;
			}
			else {
				dictionary.add(current);
				indeces.add(dictionary.indexOf(search));
				search = "";
			}
		}
		write_indeces(filePath);
		indeces.clear();
		dictionary.clear();
	}
	public static void decompress(String filePath)throws IOException {
		read_indeces(filePath);
		buildDictionary();
		String data = "";
		String current = "";
		Integer next = 0;
		for(int i = 0; i < indeces.size(); i++) {
			Integer index = indeces.get(i);
			if(i < indeces.size()-1) {
				next = indeces.get(i+1);
			}
			if(index < dictionary.size()) {
				String a = dictionary.get(index);
				data += a;
				if(next < dictionary.size()) {
					String b = String.valueOf(dictionary.get(next).charAt(0));
					current = a + b;
					if(!dictionary.contains(current)) {
						dictionary.add(current);
					}
				}
			}
			else {
				String previous = dictionary.get(indeces.get(i-1));
				previous += previous.charAt(0);
				data+=previous;
				dictionary.add(previous);
				if(dictionary.size() > next) {
					String b = String.valueOf(dictionary.get(next).charAt(0));
					current = previous+b;
					if(!dictionary.contains(current)) {
						dictionary.add(current);
					}
				}
			}
			current = "";
		}
		dictionary.clear();
		indeces.clear();
		write_decompressed(data, filePath);
	}
	public static void main(String[] args)throws IOException {
		GUI x = new GUI();
		x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		x.setSize(500,300);
		x.setVisible(true);
	}
}



//97 98 97 128 131 128
//a b a ab aba ab
//ab - ba - aa - aba