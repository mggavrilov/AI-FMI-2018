package uni.sofia.fmi.is.knn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class KNN {
	public static final int DATA_SET_SIZE = 150;
	public static final int TEST_SET_SIZE = 20;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int k = 0;

		while (k < 1) {
			System.out.print("k: ");
			k = sc.nextInt();
		}

		String file = "iris.data";
		ArrayList<Iris> dataSet = new ArrayList<Iris>(DATA_SET_SIZE);
		ArrayList<Iris> testSet = new ArrayList<Iris>(TEST_SET_SIZE);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				dataSet.add(new Iris(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
						Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[4]));
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("I/O Exception");
			e.printStackTrace();
		}

		Random rand = new Random();

		for (int i = 0; i < TEST_SET_SIZE; i++) {
			int randIndex = rand.nextInt(dataSet.size());
			testSet.add(dataSet.remove(randIndex));
		}

		int guessedCount = 0;

		for (int i = 0; i < testSet.size(); i++) {
			for (int j = 0; j < dataSet.size(); j++) {
				dataSet.get(j).euclideanDistance(testSet.get(i));
			}

			Collections.sort(dataSet, new Comparator<Iris>() {
				@Override
				public int compare(final Iris i1, Iris i2) {
					if (i1.getTempDistance() < i2.getTempDistance()) {
						return -1;
					} else if (i1.getTempDistance() > i2.getTempDistance()) {
						return 1;
					}

					return 0;
				}
			});

			HashMap<String, Integer> classVote = new HashMap<String, Integer>();

			for (int j = 0; j < k; j++) {
				String className = dataSet.get(j).getClassName();

				if (classVote.containsKey(className)) {
					classVote.put(className, classVote.get(className) + 1);
				} else {
					classVote.put(className, 1);
				}
			}

			int mostVotes = 0;
			String mostVotesClass = "";

			for (HashMap.Entry<String, Integer> entry : classVote.entrySet()) {
				if (entry.getValue() > mostVotes) {
					mostVotes = entry.getValue();
					mostVotesClass = entry.getKey();
				}
			}

			System.out.println("Test subject " + (i + 1) + ": predicted class: " + mostVotesClass + ", real class: "
					+ testSet.get(i).getClassName());

			if (mostVotesClass.equals(testSet.get(i).getClassName())) {
				guessedCount++;
			}
		}

		System.out.println("Accuracy: " + ((double) guessedCount / TEST_SET_SIZE * 100) + "%");

		sc.close();
	}
}
