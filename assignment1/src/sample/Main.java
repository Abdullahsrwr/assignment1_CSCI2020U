package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import static javafx.scene.paint.Color.*;


public class Main extends Application {

    private BorderPane layout;
    private TableView<TestFile> testTable;
    double numTruePositives = 0;
    double numFalsePositives = 0;
    double numTrueNegatives = 0;
	private Background RED;


	
	/** 
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
    public void start(Stage primaryStage) throws Exception{
        
        // Implement directory picker to let the user navigate to data folder
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        // Extentions from main dir to get to needed folders
        File spamDir = new File(mainDirectory + "/train/spam");
        File hamDir = new File(mainDirectory + "/train/ham");
        File hamDir2 = new File(mainDirectory + "/train/ham2");
        File spamTest = new File(mainDirectory + "/test/spam");
        File hamTest = new File(mainDirectory + "/test/ham");
        File outFileHam = new File("outputHam.txt");
        File outFileSpam = new File("outputSpam.txt");

        WordCounter wordCounter = new WordCounter();
		try{
			wordCounter.parseFile(hamDir);
			wordCounter.parseFile(hamDir2);
			wordCounter.outputWordCount(2, outFileHam);
		}catch(FileNotFoundException e){
			System.err.println("Invalid input dir: " + hamDir.getAbsolutePath());
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		WordCounter wordCounter3 = new WordCounter();
		try{
			wordCounter3.parseFile(spamDir);
			wordCounter3.outputWordCount(2, outFileSpam);
		}catch(FileNotFoundException e){
			System.err.println("Invalid input dir: " + spamDir.getAbsolutePath());
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

        // Call function for trainHam and trainSpam
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
		
		int countHam = hamDir.list().length;
		countHam += hamDir2.list().length;
		int countSpam = spamDir.list().length;

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


		TreeMap<String, Double> testingHam = new TreeMap<String, Double>();			
		File directoryPath = hamTest;
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
				prob = 1/(1+Math.pow(Math.E,n));
				testingHam.put(file.getName(),prob);
				//System.out.println(testing);
				reader3.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}

		TreeMap<String, Double> testingSpam = new TreeMap<String, Double>();			
		File directoryPath2 = spamTest;
        //List of all files and directories
        File filesList2[] = directoryPath2.listFiles();
      	for(File file : filesList2) {
			BufferedReader reader4;
			try 
			{
				reader4 = new BufferedReader(new FileReader(file.getAbsolutePath()));
				String var4 = "";
				String line = reader4.readLine();
				double n = 0;
				double prob = 0;
				while (line != null) 
				{
					//System.out.println(line);
					var4 = reader4.readLine();
					if (var4 != null)
					{
						String[] words = var4.split(" ");
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
					line = reader4.readLine();
				}
				prob = 1/(1+Math.pow(Math.E,n));
				testingSpam.put(file.getName(),prob);
				//System.out.println(testing);
				reader4.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
        
        ObservableList<TestFile> fileList = FXCollections.observableArrayList();
        Set set4 = testingSpam.entrySet();
    	Iterator it4 = set4.iterator();
    	while(it4.hasNext()) {
      		Map.Entry me4 = (Map.Entry)it4.next();
			String newKey4 = me4.getKey().toString();
			double newVal4 = ((double)me4.getValue());
			String folderName = "Spam";
            TestFile a = new TestFile(newKey4,newVal4,folderName);
            fileList.add(a);
    	}
        Set set5 = testingHam.entrySet();
    	Iterator it5 = set5.iterator();
    	while(it5.hasNext()) {
      		Map.Entry me5 = (Map.Entry)it5.next();
			String newKey5 = me5.getKey().toString();
			double newVal5 = ((double)me5.getValue());
			String folderName2 = "Ham";
            TestFile a2 = new TestFile(newKey5,newVal5,folderName2);
            fileList.add(a2);
    	}
     // Variable for testfile contents
        // Test probabilities and place into fileList


        testTable = new TableView<>();
        testTable.setItems(fileList); // load filelist to a table to show results
        //testTable.setItems(fileList);

        // Create table columnns
        TableColumn<TestFile, String> fileColumn;
        fileColumn = new TableColumn<>("File");
        fileColumn.setMinWidth(400);
		fileColumn.setStyle("-fx-text-fill: black; -fx-background-color: grey;-fx-font-size: 16px;");
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("Filename"));

        TableColumn<TestFile,String> classColumn;
        classColumn = new TableColumn<>("Actual Class");
        classColumn.setMinWidth(150);
		classColumn.setStyle("-fx-text-fill: black; -fx-background-color: silver;-fx-font-size: 16px;");
        classColumn.setCellValueFactory(new PropertyValueFactory<>("ActualClass"));

        TableColumn<TestFile,String> spamColumn;
        spamColumn = new TableColumn<>("Spam Probability");
        spamColumn.setMinWidth(300);
		spamColumn.setStyle("-fx-text-fill: black; -fx-background-color: grey;-fx-font-size: 16px;");
        spamColumn.setCellValueFactory(new PropertyValueFactory<>("SpamProbability"));

        // Table Columns
        testTable.getColumns().add(fileColumn);
        testTable.getColumns().add(classColumn);
        testTable.getColumns().add(spamColumn);

        // Display Area Below Table
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);
        editArea.setStyle("-fx-text-fill: black; -fx-background-color: dimgrey;-fx-font-size: 16px;");

        // Calculate Acc and Precision
        double truePositives = 0;
        double falsePositives = 0;
        double correctGuess = 0;
        int numFiles = 0;

        // Loop through the fileList variable size (hold testFile contents)
        // Use a threshold (0.5) to change acc and precision
        // Lower or higher the threshold for what we would consider spam email.
        for (int i = 0; i < fileList.size(); i++) {
            for (int j = i + 1; j < fileList.size(); j++) {
                if (fileList.get(i).getActualClass() == "Spam" && fileList.get(i).getSpamProbability() >= 0.5) {
                    correctGuess++;
                    truePositives++;
                } else if (fileList.get(i).getActualClass() == "Ham" && fileList.get(i).getSpamProbability() <= 0.5) {
                    correctGuess++;
                } else if (fileList.get(i).getActualClass() == "Ham" && fileList.get(i).getSpamProbability() > 0.5) {
                    falsePositives++;
                }
                numFiles++;
            }
        }

        double accuracy = correctGuess/numFiles;
        double precision = truePositives / (falsePositives + truePositives);

        // Convert acc and prec to string for display using Decimal format
        DecimalFormat df = new DecimalFormat("0.00000");
        String repAcc = df.format(accuracy);
        String repPrec = df.format(precision);

        // Display Accuracy and Precsion
        Label accLabel = new Label("Accuracy");
        editArea.add(accLabel, 0, 0);
        TextField accField = new TextField();
        accField.setEditable(false);
        accField.setText(repAcc);
        accField.setStyle("-fx-text-fill: black; -fx-background-color: silver; -fx-font-size: 16px;");
		accLabel.setStyle("-fx-text-fill: black;  -fx-font-size: 16px; fx-font-weight: bold; ");
        editArea.add(accField, 1, 0);

        Label precLabel = new Label("Precision");
        editArea.add(precLabel, 0, 1);
        TextField precField = new TextField();
        precField.setEditable(false);
        precField.setText(repPrec);
        precField.setStyle("-fx-text-fill: black; -fx-background-color: silver; -fx-font-size: 16px;");
		precLabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px; fx-font-weight: bold; ");
        editArea.add(precField, 1, 1);

        // Components in UI
        layout = new BorderPane();
        layout.setCenter(testTable);
        layout.setBottom(editArea);
        Scene scene = new Scene(layout, 865, 600);
        primaryStage.setTitle("Spam Detection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
        launch(args);
    }
}
