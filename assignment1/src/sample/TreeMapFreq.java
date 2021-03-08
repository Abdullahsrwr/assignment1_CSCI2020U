package sample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.io.File;
import java.io.*;
import java.util.*;
import java.lang.Math;
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

		TreeMap<String, Double> probHam = new TreeMap<String, Double>();
		TreeMap<String, Double> probSpam = new TreeMap<String, Double>();
		TreeMap<String, Double> spamWord = new TreeMap<String, Double>();
		
		File ham1 = new File("data/train/ham");
		int countHam = ham1.list().length;
		File ham2 = new File("data/train/ham2");
		countHam += ham2.list().length;
		File spam1 = new File("data/train/spam");
		int countSpam = spam1.list().length;

		Set set2 = trainHamFreq.entrySet();
    	Iterator it2 = set2.iterator();
    	while(it2.hasNext()) {
      		Map.Entry me2 = (Map.Entry)it2.next();
			String newKey2 = me2.getKey().toString();
			double newVal2 = Double.valueOf((int)me2.getValue()) / Double.valueOf(countHam);
			probHam.put(newKey2,newVal2);
			
    	}

		Set set = trainSpamFreq.entrySet();
    	Iterator it = set.iterator();
    	while(it.hasNext()) {
      		Map.Entry me = (Map.Entry)it.next();
			String newKey = me.getKey().toString();
			double newVal = Double.valueOf((int)me.getValue()) / Double.valueOf(countSpam);
			probSpam.put(newKey,newVal);
			
    	}

		Set set3 = probSpam.entrySet();
    	Iterator it3 = set3.iterator();
    	while(it3.hasNext()) {
      		Map.Entry me3 = (Map.Entry)it3.next();
			if (probHam.containsKey(me3.getKey()))
			{
				String newKey = me3.getKey().toString();
				double newVal = (double)me3.getValue() / ((double)me3.getValue() + (double)probHam.get(newKey));
				spamWord.put(newKey,newVal);
			}
			
    	}


		TreeMap<String, Double> testing = new TreeMap<String, Double>();			
		File directoryPath = new File("data/test/ham");
        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
      	for(File file : filesList) {
			BufferedReader reader3;
			try 
			{
				reader3 = new BufferedReader(new FileReader(file.getAbsolutePath()));
				String var3 = "";
				String line = reader3.readLine();
				double n = 0;
				double prob = 0;
				while (line != null) 
				{
					//System.out.println(line);
					var3 = reader3.readLine();
					if (var3 != null)
					{
						String[] words = var3.split(" ");
						for (int i = 0; i < words.length; i++) 
						{
							String word = words[i];
							//System.out.println(word);
							WordCounter checker = new WordCounter();
							if (checker.isValidWord(word))
							{
								if (spamWord.containsKey(word))
								{
									n += ((Math.log(1-(double)spamWord.get(word))) - (Math.log((double)spamWord.get(word))));
								}
							}
						}
					}
					line = reader3.readLine();
				}
				prob = 1/1+Math.pow(Math.E,n);
				testing.put(file.getName(),prob);
				//System.out.println(testing);
				reader3.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		/*System.out.println(trainHamFreq);
		System.out.println(trainSpamFreq);
		System.out.println(probHam);
		System.out.println(probSpam);
		System.out.println(spamWord);*/
		System.out.println(testing);
		
		
    } 
}
