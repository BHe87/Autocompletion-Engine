import java.util.*;
import java.io.*;
import java.text.*;

public class ac_test {
	public static void main (String[] args) {
		DLB dictionary = new DLB();
		Set<String> set = new HashSet<>();
		List<String> list = new LinkedList<String>();
		BufferedWriter bw;
		File file;
		Scanner dictionaryReader, historyReader, input; 
		String response = "";
		StringBuilder word = new StringBuilder();
		long end = 0, predictionTime = 0, averageTime = 0;
		int averageCount = 0, counter = 1;
		double start = 0, time = 0;
		NumberFormat formatter;

		try {
			dictionaryReader = new Scanner(new File("dictionary.txt"));
			int i = 0;
			while (dictionaryReader.hasNext()) {//adding the words to the DLB
				String key = dictionaryReader.nextLine();
				dictionary.add(key);
				i++;
			}
		}
		catch (IOException e) {
			System.out.println("Error");
		}

		try {
			file = new File("user_history.txt");
			historyReader = new Scanner(file);
			while (historyReader.hasNext()) {//adding words from user_history.txt to the set for better predictions
				String historyWord = historyReader.next();
				set.add(historyWord);
				System.out.println(historyWord);
			}
		}
		catch (IOException e){
			System.out.println("user_history not found but this program makes one later");
		}

		input = new Scanner(System.in);
		while (!response.equals("!")) {//main control flow of the program
			counter = 1;
			System.out.println("Please enter your word one letter at a time");
			response = input.next();

			if (response.equals("1") || response.equals("2") || response.equals("3") || response.equals("4") || response.equals("5")) {//making a prediction selection
				int selection = Integer.parseInt(response);
				set.add(list.get(selection-1));//adding the prediction to the set for the user history
				System.out.println("WORD COMPLETED: " + list.get(selection-1));
				list.clear();
				word = new StringBuilder();
				counter = 1;
				continue;
			}

			if (response.equals("$")) {//end of word
				dictionary.clearList();
				list.clear();
				counter = 1;
				set.add(word.toString());
				word = new StringBuilder();
			}

			else {
				list.clear();
				word.append(response);
				System.out.println("Predictions: ");
				start = System.nanoTime();

				for (String s : set) {
					if (word.length() > s.length()) continue;//word is not in the user history
					if (s.substring(0, word.length()).equals(word.toString()) && counter < 6) {//checking each word in the hashset for a prefix that matches the current word
						System.out.print("(" + counter + ")" + s + " ");
						list.add(s);
						counter++;
					}
				}
			
				for (String s : dictionary.keysWithPrefix(word.toString())) {//searching the DLB for predictitions
					if (counter < 6) {
						System.out.print("(" + counter + ")" + s + " ");
						list.add(s);
						counter++;
					}
				}

				if (response.equals("!")) {//calculating average time
					formatter = new DecimalFormat("#0.000000");
					averageTime = averageTime/averageCount;
					System.out.println("Average Time: " + (formatter.format(averageTime)) + " s");
					System.out.println("Bye!");
					break;
				}
				counter = 1;
				end = System.nanoTime();
				time = (end - start)/1000000000.0;
				averageTime += time;
				averageCount++;
				formatter = new DecimalFormat("#0.000000");
				System.out.println("\n(" + (formatter.format(time) + " s)"));
			}
		}

		try {//writing the contents of the hashset to the text file
			bw = new BufferedWriter(new FileWriter("user_history.txt"));
			for (String s : set) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
		}
		catch (IOException e) {
			System.out.println("Something went wrong with writing to the file");
		}
	}
}