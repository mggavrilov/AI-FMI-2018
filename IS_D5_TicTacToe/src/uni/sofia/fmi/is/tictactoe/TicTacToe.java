package uni.sofia.fmi.is.tictactoe;

import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe {
	public static final char NULL_CHAR = '\u0000';

	public static void print(char[][] board) {
		System.out.println();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == NULL_CHAR) {
					System.out.print("_ ");
				} else {
					System.out.print(board[i][j] + " ");
				}

			}

			System.out.println();
		}

		System.out.println();
	}

	public static boolean isGameOver(char[][] board) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == NULL_CHAR) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isWinning(char[][] board, char player) {
		if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
			return true;
		}

		if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
			return true;
		}

		for (int i = 0; i < 3; i++) {
			if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
				return true;
			}

			if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
				return true;
			}
		}

		return false;
	}

	public static void deepCopyBoard(char[][] source, char[][] target) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				target[i][j] = source[i][j];
			}
		}
	}

	public static ArrayList<char[][]> getChildren(char[][] board, char turnSymbol) {
		ArrayList<char[][]> children = new ArrayList<char[][]>();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == NULL_CHAR) {
					char[][] newMove = new char[3][3];
					deepCopyBoard(board, newMove);
					newMove[i][j] = turnSymbol;
					children.add(newMove);
				}
			}
		}

		return children;
	}

	public static int minimax(char[][] board, char aiSymbol, double alpha, double beta, int depth, char currentTurn) {
		char opponent = (aiSymbol == 'X') ? 'O' : 'X';
		depth++;

		if (isWinning(board, opponent)) {
			return depth - 10;
		} else if (isWinning(board, aiSymbol)) {
			return 10 - depth;
		} else if (isGameOver(board)) {
			return 0;
		}

		if (currentTurn == aiSymbol) {
			// ai move
			int bestMoveIndex = -1;
			ArrayList<char[][]> children = getChildren(board, aiSymbol);

			for (int i = 0; i < children.size(); i++) {
				char[][] temp = new char[3][3];
				deepCopyBoard(children.get(i), temp);

				int score = minimax(temp, aiSymbol, alpha, beta, depth, opponent);

				if (score > alpha) {
					alpha = score;
					bestMoveIndex = i;
				}

				if (alpha >= beta) {
					break;
				}
			}

			if (bestMoveIndex != -1) {
				deepCopyBoard(children.get(bestMoveIndex), board);
			}

			return (int) alpha;
		} else {
			// player move
			int bestMoveIndex = -1;
			ArrayList<char[][]> children = getChildren(board, opponent);

			for (int i = 0; i < children.size(); i++) {
				char[][] temp = new char[3][3];
				deepCopyBoard(children.get(i), temp);

				int score = minimax(temp, aiSymbol, alpha, beta, depth, aiSymbol);

				if (score < beta) {
					beta = score;
					bestMoveIndex = i;
				}

				if (alpha >= beta) {
					break;
				}
			}

			if (bestMoveIndex != -1) {
				deepCopyBoard(children.get(bestMoveIndex), board);
			}

			return (int) beta;
		}

	}

	public static void main(String[] args) {
		char[][] board = new char[3][3];

		Scanner sc = new Scanner(System.in);

		System.out.print("Would you like to go first? (Y/N): ");
		String first = sc.nextLine();

		boolean humanTurn;

		char humanSymbol, aiSymbol;

		if (first.charAt(0) == 'y' || first.charAt(0) == 'Y') {
			humanTurn = true;
			humanSymbol = 'X';
			aiSymbol = 'O';
		} else {
			humanTurn = false;
			aiSymbol = 'X';
			humanSymbol = 'O';
		}

		boolean tie = true;

		while (!isGameOver(board)) {
			if (humanTurn) {
				print(board);

				System.out.print("Your move (x, y): ");
				String userMove = sc.nextLine();

				if (userMove.length() != 3) {
					System.out.println("Invalid input. Try again.");
					continue;
				}

				int x = Character.getNumericValue(userMove.charAt(0));
				int y = Character.getNumericValue(userMove.charAt(2));

				// get board indices from coordinates
				x--;
				y--;

				if (board[x][y] != NULL_CHAR) {
					System.out.println("Cell already occupied.");
					continue;
				} else {
					board[x][y] = humanSymbol;
					humanTurn = false;

					if (isWinning(board, humanSymbol)) {
						System.out.println("You win!");
						tie = false;
						break;
					}
				}
			} else {
				minimax(board, aiSymbol, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, aiSymbol);
				humanTurn = true;

				if (isWinning(board, aiSymbol)) {
					print(board);
					System.out.println("You lose!");
					tie = false;
					break;
				}
			}
		}

		if (tie) {
			print(board);
			System.out.println("Tie!");
		}

		sc.close();
	}
}
