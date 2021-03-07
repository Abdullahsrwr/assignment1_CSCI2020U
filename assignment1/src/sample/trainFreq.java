package sample;

// This java class is for the trainHam and trainSpam Frequency maps, contains a map of words and number of files containing that word in each folder.

// Imports for working with tree maps
import java.util.TreeMap;
import java.util.Map;
import java.util.Iterator;

public class trainFreq {
    private Map<String, Integer> map;
    private double fileCount;

    // Constructor
    public trainFreq(){
        map = new TreeMap<>();
        fileCount = 0;
    }

    // Increment word and files.
    public void incrementWord(String word) {
        // If the map cannot add a key, associate value with key.
        if (!map.containsKey(word)) {
            map.put(word, 1);
        } else { // associate word with the next key
            map.put(word, (map.get(word) + 1) );
        }
    }

    public void incrementFiles() {
        fileCount += 1;
    }

    // Constructs int object that represents the int value indicated by string param.
    public Integer getWord(String word) {
        if (map.containsKey(word)) {
            return map.get(word);
        } else {
            return 0;
        }
    }

    public double getFileCount() {
        return fileCount;
    }

    public boolean hasWord(String word) {
        return map.containsKey(word);
    }

    // Want to generate series of our words and files
    public Iterator<String> getTrainFreq() {
        return (map.keySet().iterator());
    }
}


