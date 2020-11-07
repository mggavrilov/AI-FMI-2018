package uni.sofia.fmi.is.slidingblocks;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
	private int[][] board;
	private ArrayList<Node> children;
	private Node parent;
	private String move;
	private int fScore;
	private int gScore;

	public Node() {
		board = null;
		children = new ArrayList<Node>();
		parent = null;
		move = null;
		fScore = 0;
		gScore = 0;
	}

	public Node(int[][] board) {
		this();
		this.board = board;
	}

	public Node(int[][] board, String move) {
		this(board);
		this.move = move;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		child.parent = this;
		children.add(child);
	}

	public int[][] getBoard() {
		return board;
	}

	public Node getParent() {
		return parent;
	}

	public String getMove() {
		return move;
	}

	public int getFScore() {
		return fScore;
	}

	public void setFScore(int fScore) {
		this.fScore = fScore;
	}

	public int getGScore() {
		return gScore;
	}

	public void setGScore(int gScore) {
		this.gScore = gScore;
	}

	public void generateChildren() {
		if (board != null) {
			int length = board.length;

			int zeroX = -1;
			int zeroY = -1;

			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					if (board[i][j] == 0) {
						zeroX = i;
						zeroY = j;
					}
				}
			}

			if (zeroX == -1 || zeroY == -1) {
				return;
			}

			// down
			if (zeroX > 0) {
				addChild(new Node(swapCells(zeroX, zeroY, zeroX - 1, zeroY), "down"));
			}

			// up
			if (zeroX < length - 1) {
				addChild(new Node(swapCells(zeroX, zeroY, zeroX + 1, zeroY), "up"));
			}

			// right
			if (zeroY > 0) {
				addChild(new Node(swapCells(zeroX, zeroY, zeroX, zeroY - 1), "right"));
			}

			// left
			if (zeroY < length - 1) {
				addChild(new Node(swapCells(zeroX, zeroY, zeroX, zeroY + 1), "left"));
			}
		}
	}

	private int[][] swapCells(int x1, int y1, int x2, int y2) {
		int[][] newBoard = new int[board.length][board.length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				newBoard[i][j] = board[i][j];
			}
		}

		int temp = newBoard[x1][y1];
		newBoard[x1][y1] = newBoard[x2][y2];
		newBoard[x2][y2] = temp;

		return newBoard;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;
		if (!Arrays.deepEquals(board, other.board)) {
			return false;
		}
		return true;
	}
}