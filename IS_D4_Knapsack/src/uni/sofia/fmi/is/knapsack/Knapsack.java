package uni.sofia.fmi.is.knapsack;

import java.util.Random;
import java.util.Scanner;

public class Knapsack {
	private static final int POPULATION_SIZE = 10000;
	private static final int MAX_GENERATIONS_UNCHANGED = 25;
	private static final double MUTATION_CHANCE = 0.01;
	private static final double CROSSOVER_CHANCE = 0.9;
	private static final int BEST_SIZE = 2;
	private int[] weights;
	private int[] values;
	private boolean[][] population;
	private int m;
	private int n;
	private Random rand;
	private boolean[][] bestIndividuals;
	private int bestGlobalFitness;
	private int generationsUnchanged;

	public Knapsack(int m, int n) {
		this.m = m;
		this.n = n;
		weights = new int[POPULATION_SIZE];
		values = new int[POPULATION_SIZE];
		population = new boolean[POPULATION_SIZE][n];
		rand = new Random();
		bestIndividuals = new boolean[BEST_SIZE][n];
	}

	public void geneticAlgorithm() {
		generatePopulation();

		int generationCount = 0;

		while (generationsUnchanged < MAX_GENERATIONS_UNCHANGED) {
			generationCount++;

			fitness();

			crossover();

			mutate();

			System.out.println("Generation " + generationCount + " Fitness: " + bestGlobalFitness);
		}
	}

	public void crossover() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			double random = rand.nextDouble();

			if (random <= CROSSOVER_CHANCE) {
				// choose a random pivot between 1 and n - 2 so there can be no cloning here
				int pivot = rand.nextInt(n - 1) + 1;

				if (rand.nextDouble() < 0.5) {
					for (int j = 0; j < pivot; j++) {
						population[i][j] = bestIndividuals[0][j];
					}

					for (int j = pivot; j < n; j++) {
						population[i][j] = bestIndividuals[1][j];
					}
				} else {
					for (int j = 0; j < pivot; j++) {
						population[i][j] = bestIndividuals[1][j];
					}

					for (int j = pivot; j < n; j++) {
						population[i][j] = bestIndividuals[0][j];
					}
				}
			} else {
				// clone
				int randomBest = rand.nextInt(2);

				for (int j = 0; j < n; j++) {
					population[i][j] = bestIndividuals[randomBest][j];
				}
			}
		}
	}

	public void mutate() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			int currentWeight = 0;

			for (int j = 0; j < n; j++) {
				if (population[i][j]) {
					currentWeight += weights[j];
				}

				double random = rand.nextDouble();

				if (random <= MUTATION_CHANCE) {
					if (population[i][j] == false && currentWeight + weights[j] > m) {
						continue;
					}

					if (population[i][j] == false) {
						currentWeight += weights[j];
					}

					population[i][j] = !population[i][j];
				}
			}
		}
	}

	public void fitness() {
		int bestFitness = 0;
		int bestFitnessIndex = -1;

		int secondBestFitness = 0;
		int secondBestFitnessIndex = -1;
		for (int i = 0; i < POPULATION_SIZE; i++) {

			int fitness = 0;
			int weight = 0;

			for (int j = 0; j < population[i].length; j++) {
				if (population[i][j]) {
					fitness += values[j];
					weight += weights[j];
				}

				if (weight > m) {
					fitness = 0;
					break;
				}
			}

			if (fitness > bestFitness) {
				bestFitness = fitness;
				bestFitnessIndex = i;
			}

			if (fitness > secondBestFitness && fitness <= bestFitness && i != bestFitnessIndex) {
				secondBestFitness = fitness;
				secondBestFitnessIndex = i;
			}
		}

		bestIndividuals[0] = population[bestFitnessIndex];
		bestIndividuals[1] = population[secondBestFitnessIndex];

		if (bestFitness > bestGlobalFitness) {
			bestGlobalFitness = bestFitness;
			generationsUnchanged = 0;
		} else if (bestFitness == bestGlobalFitness) {
			generationsUnchanged++;
		}
	}

	public void generatePopulation() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			int currentWeight = 0;

			while (true) {
				int randomItem = rand.nextInt(n);

				if (currentWeight + weights[randomItem] <= m) {
					if (!population[i][randomItem]) {
						population[i][randomItem] = true;
						currentWeight += weights[randomItem];
					}
				} else {
					break;
				}
			}
		}
	}

	public int[] getWeights() {
		return weights;
	}

	public void setWeights(int[] weights) {
		this.weights = weights;
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public boolean[][] getPopulation() {
		return population;
	}

	public void setPopulation(boolean[][] population) {
		this.population = population;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public boolean[][] getBestIndividuals() {
		return bestIndividuals;
	}

	public void setBestIndividuals(boolean[][] bestIndividuals) {
		this.bestIndividuals = bestIndividuals;
	}

	public boolean[] getBestIndividual() {
		return bestIndividuals[0];
	}

	public int getBestGlobalFitness() {
		return bestGlobalFitness;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int m, n;

		m = sc.nextInt();
		n = sc.nextInt();

		int[] weights = new int[n];
		int[] values = new int[n];

		for (int i = 0; i < n; i++) {
			weights[i] = sc.nextInt();
			values[i] = sc.nextInt();
		}

		Knapsack knapsack = new Knapsack(m, n);
		knapsack.setValues(values);
		knapsack.setWeights(weights);

		knapsack.geneticAlgorithm();

		System.out.println(knapsack.getBestGlobalFitness());

		sc.close();
	}
}