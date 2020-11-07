package uni.sofia.fmi.is.slidingblocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class SlidingBlocks {

	public static Node solutionNode;

	public static int manhattan(int[][] board) {

		int length = board.length;

		int expectedValue = 0;
		int score = 0;

		int solutionRow;
		int solutionColumn;

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				expectedValue++;

				if (board[i][j] != 0 && board[i][j] != expectedValue) {
					if (board[i][j] % length == 0) {
						solutionRow = board[i][j] / length - 1;
						solutionColumn = length - 1;
					} else {
						solutionRow = board[i][j] / length;
						solutionColumn = board[i][j] % length - 1;
					}

					score += Math.abs(i - solutionRow) + Math.abs(j - solutionColumn);
				}
			}
		}

		return score;
	}

	public static void astar(Node startNode, Node goalNode) {
		PriorityQueue<Node> openSet = new PriorityQueue<Node>(new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				if (n1.getFScore() < n2.getFScore()) {
					return -1;
				}

				if (n1.getFScore() > n2.getFScore()) {
					return 1;
				}

				return 0;
			}
		});

		HashSet<int[][]> closedSet = new HashSet<int[][]>();

		Node currentNode = startNode;

		while (!currentNode.equals(goalNode)) {
			closedSet.add(currentNode.getBoard());
			currentNode.generateChildren();

			ArrayList<Node> children = currentNode.getChildren();

			for (int i = 0; i < children.size(); i++) {
				if (closedSet.contains(children.get(i).getBoard())) {
					continue;
				}

				closedSet.add(children.get(i).getBoard());

				int gScore = currentNode.getGScore() + 1;
				children.get(i).setGScore(gScore);
				children.get(i).setFScore(gScore + manhattan(children.get(i).getBoard()));

				openSet.add(children.get(i));
			}

			currentNode = openSet.poll();
		}

		solutionNode = currentNode;
	}

	public static boolean isSolvable(int[][] board) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] != 0) {
					sb.append(board[i][j]);
				}
			}
		}

		String stringBoard = sb.toString();

		int inversions = 0;

		for (int i = 0; i < stringBoard.length(); i++) {
			for (int j = i + 1; j < stringBoard.length(); j++) {
				if (stringBoard.charAt(i) > stringBoard.charAt(j)) {
					inversions++;
				}
			}
		}

		return inversions % 2 == 0;
	}

	public static boolean isPerfectSquare(int n) {
		return Math.pow(Math.sqrt(n), 2) == Math.pow((int) Math.sqrt(n), 2);
	}

	public static void main(String[] args) {
		solutionNode = null;

		Scanner sc = new Scanner(System.in);
		int length = sc.nextInt();

		if (length < 8 || !isPerfectSquare(length + 1)) {
			System.out.println("Invalid N.");
			sc.close();
			return;
		}

		int rowSize = (int) Math.sqrt(length + 1);
		int colSize = rowSize;

		int[][] board = new int[rowSize][colSize];
		int[][] goalBoard = new int[rowSize][colSize];
		int count = 1;

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				board[i][j] = sc.nextInt();

				if (board[i][j] > length) {
					System.out.println("Invalid number.");
					sc.close();
					return;
				}

				goalBoard[i][j] = count++;
			}
		}

		sc.close();

		if (!isSolvable(board)) {
			System.out.println("Unsolvable.");
			return;
		}

		goalBoard[rowSize - 1][colSize - 1] = 0;

		Node startNode = new Node(board);
		Node goalNode = new Node(goalBoard);

		astar(startNode, goalNode);

		if (solutionNode != null) {
			Stack<String> stack = new Stack<String>();

			Node parent = solutionNode;
			int moveCount = 0;

			while (parent.getMove() != null) {
				moveCount++;
				stack.push(parent.getMove());

				parent = parent.getParent();
			}

			System.out.println(moveCount);

			while (!stack.isEmpty()) {
				System.out.println(stack.pop());
			}
		}
	}
}