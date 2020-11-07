package uni.sofia.fmi.is.leapfrog;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Leapfrog {
	protected static Node solution;

	public static String createFrogs(int n) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < n; i++) {
			sb.append(">");
		}

		sb.append("_");

		for (int i = 0; i < n; i++) {
			sb.append("<");
		}

		return sb.toString();
	}

	public static void branch(Node node) {
		String data = node.getData();

		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == '>') {
				if (i + 1 < data.length()) {
					if (data.charAt(i + 1) == '_') {
						node.addChild(new Node(swapCharacters(data, i, i + 1)));
					}
				}

				if (i + 2 < data.length()) {
					if (data.charAt(i + 2) == '_') {
						node.addChild(new Node(swapCharacters(data, i, i + 2)));
					}
				}
			} else if (data.charAt(i) == '<') {
				if (i - 1 >= 0) {
					if (data.charAt(i - 1) == '_') {
						node.addChild(new Node(swapCharacters(data, i - 1, i)));
					}
				}

				if (i - 2 >= 0) {
					if (data.charAt(i - 2) == '_') {
						node.addChild(new Node(swapCharacters(data, i - 2, i)));
					}
				}
			}
		}

		ArrayList<Node> nodeChildren = node.getChildren();

		for (int i = 0; i < nodeChildren.size(); i++) {
			branch(nodeChildren.get(i));
		}
	}

	public static String swapCharacters(String str, int x, int y) {
		return str.substring(0, x) + str.charAt(y) + str.substring(x + 1, y) + str.charAt(x) + str.substring(y + 1);
	}

	public static void dfs(Node node) {
		if (solution != null) {
			return;
		}

		if (isSolution(node.getData())) {
			solution = node;
		} else {
			ArrayList<Node> nodeChildren = node.getChildren();
			for (int i = 0; i < nodeChildren.size(); i++) {
				dfs(nodeChildren.get(i));
			}
		}
	}

	public static boolean isSolution(String str) {
		if (str.charAt(str.length() / 2) != '_') {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if (i < str.length() / 2) {
				// left side
				if (str.charAt(i) != '<') {
					return false;
				}
			} else if (i > str.length() / 2) {
				// right side
				if (str.charAt(i) != '>') {
					return false;
				}
			}
		}

		return true;
	}

	public static void main(String[] args) {
		int n = -1;
		Scanner sc = new Scanner(System.in);

		while (n <= 0) {
			System.out.print("n: ");
			n = sc.nextInt();
		}

		solution = null;

		String frogs = createFrogs(n);

		Node root = new Node(frogs);

		branch(root);

		dfs(root);

		if (solution != null) {
			Stack<String> stack = new Stack<String>();

			Node parent = solution;

			while (parent != null) {
				stack.push(parent.getData());
				parent = parent.getParent();
			}

			while (!stack.isEmpty()) {
				System.out.println(stack.pop());
			}
		}

		sc.close();
	}
}
