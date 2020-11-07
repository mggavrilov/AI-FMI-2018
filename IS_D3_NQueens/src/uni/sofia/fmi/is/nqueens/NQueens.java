package uni.sofia.fmi.is.nqueens;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NQueens {
	private int n;
	private int[] board;
	private int[] rowConflicts;
	private int[] mainDiag;
	private int[] antiDiag;
	private int disposition;

	public NQueens() {
		board = null;
	}

	public NQueens(int n) {
		this.n = n;
		initBoard();
	}

	public int[] getBoard() {
		return board;
	}

	public int getN() {
		return n;
	}

	public void initBoard() {
		board = new int[n];

		for (int i = 0; i < n; i++) {
			board[i] = i;
		}

		Random rand = new Random();

		// shuffle and make sure there are no queens in the corners when n is even
		do {
			for (int i = 0; i < n; i++) {
				int j = rand.nextInt(n);

				int temp = board[i];
				board[i] = board[j];
				board[j] = temp;
			}
		} while (n % 2 == 0 && hasCornerQueens());

		// how many queens on each row [0...n]
		rowConflicts = new int[n];
		// how many queens on each diagonal parallel to the main diagonal
		// (x - y = c for some c where x, y - board indices)
		mainDiag = new int[2 * n - 1];
		// how many queens on each diagonal parallel to the anti diagonal
		// (x + y = c for some c where x, y - board indices)
		antiDiag = new int[2 * n - 1];

		// this is needed when subtracting indices of the main diagonal, as we can't
		// have negative array indices.
		disposition = (2 * n - 1) / 2;

		for (int i = 0; i < n; i++) {
			rowConflicts[i]++;

			int x = board[i];
			int y = i;

			// main diagonal (x - y = c)
			// disposition is needed because you can't have negative indices
			mainDiag[x - y + disposition]++;

			// anti diagonal ( x + y = c)
			antiDiag[x + y]++;
		}
	}

	public boolean minConflicts() {
		if (board == null) {
			return false;
		}

		int iterations = 0;
		int maxIterations = 2 * n;

		Random rand = new Random();

		while (true) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();

			int maxConflicts = 0;

			// find queens with most conflicts
			for (int i = 0; i < n; i++) {
				int conflicts = calculateConflicts(board[i], i);

				if (conflicts == maxConflicts) {
					candidates.add(i);
				} else if (conflicts > maxConflicts) {
					candidates.clear();
					candidates.add(i);
					maxConflicts = conflicts;
				}
			}

			if (maxConflicts == 0) {
				return true;
			}

			// pick a random queen from the list of queens with most conflicts
			int mostConflictsColumn = candidates.get(rand.nextInt(candidates.size()));

			// find cells in the same column with minimum conflicts
			int minConflicts = n;
			candidates.clear();

			for (int i = 0; i < n; i++) {
				int conflicts = calculateConflicts(i, mostConflictsColumn);

				if (conflicts == minConflicts) {
					candidates.add(i);
				} else if (conflicts < minConflicts) {
					candidates.clear();
					candidates.add(i);
					minConflicts = conflicts;
				}
			}

			// move queen with most conflicts to cell with the least conflicts in her column
			if (!candidates.isEmpty()) {
				int oldRow = board[mostConflictsColumn];

				// remove 1 queen from each of these 3 arrays, as we're moving it
				rowConflicts[oldRow]--;
				mainDiag[oldRow - mostConflictsColumn + disposition]--;
				antiDiag[oldRow + mostConflictsColumn]--;

				int newRow = candidates.get(rand.nextInt(candidates.size()));

				board[mostConflictsColumn] = newRow;

				// add 1 queen to each of these 3 arrays, as we're moving it
				rowConflicts[newRow]++;
				mainDiag[newRow - mostConflictsColumn + disposition]++;
				antiDiag[newRow + mostConflictsColumn]++;
			}

			iterations++;

			if (iterations == maxIterations) {
				// restart after maxIterations tries because we're in a bad case
				initBoard();
				iterations = 0;
			}
		}
	}

	public void printBoard() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[j] == i) {
					System.out.print("* ");
				} else {
					System.out.print("_ ");
				}
			}

			System.out.println();
		}
	}

	private int calculateConflicts(int row, int col) {
		int conflicts = 0;

		conflicts += rowConflicts[row];
		conflicts += mainDiag[row - col + disposition];
		conflicts += antiDiag[row + col];

		if (board[col] == row) {
			// there's a queen in the cell that we're calculating conflicts for,
			// subtract it 3 times because it's present in all arrays that keep conflicts
			conflicts -= 3;
		}

		return conflicts;
	}

	private boolean hasCornerQueens() {
		if (board[0] == 0 || board[0] == n - 1 || board[n - 1] == 0 || board[n - 1] == n - 1) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		int n = 0;

		Scanner sc = new Scanner(System.in);

		while (n < 4) {
			System.out.print("n: ");
			n = sc.nextInt();

			if (n < 4) {
				System.out.println("Invalid n.");
			}
		}

		NQueens nQueens = new NQueens(n);
		nQueens.initBoard();

		long startTime = System.currentTimeMillis();
		nQueens.minConflicts();
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Solved in " + elapsedTime + "ms");

		nQueens.printBoard();

		sc.close();
	}
}
