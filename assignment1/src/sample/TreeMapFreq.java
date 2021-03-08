package sample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class TreeMapFreq {

	public static void main(String[] args) {
		TreeMap<String, Integer> trainSpamFreq = new TreeMap<String, Integer>();
		String var = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("outputSpam.txt"));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				var = reader.readLine();
				if (var != null)
				{
					String[] str_array = var.split(": ");
					String word = str_array[0]; 
					int occur = Integer.parseInt(str_array[1]);
					trainSpamFreq.put(word,occur);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		TreeMap<String, Integer> trainHamFreq = new TreeMap<String, Integer>();
		String var2 = "";
		BufferedReader reader2;
		try {
			reader2 = new BufferedReader(new FileReader("outputHam.txt"));
			String line2 = reader2.readLine();
			while (line2 != null) {
				//System.out.println(line);
				var2 = reader2.readLine();
				if (var2 != null)
				{
					String[] str_array = var2.split(": ");
					String word = str_array[0]; 
					int occur = Integer.parseInt(str_array[1]);
					trainHamFreq.put(word,occur);
				}
				line2 = reader2.readLine();
			}
			reader2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(trainHamFreq);
		System.out.println(trainSpamFreq);
	}
}