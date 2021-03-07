package sample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class TreeMapFreq {

	public static void main(String[] args) {
		TreeMap<String, Integer> tmap = new TreeMap<String, Integer>();
		String var = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("outputHam.txt"));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				var = reader.readLine();
				if (var != null)
				{
					String[] str_array = var.split(": ");
					String word = str_array[0]; 
					int occur = Integer.parseInt(str_array[1]);
					tmap.put(word,occur);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(tmap);
	}
}