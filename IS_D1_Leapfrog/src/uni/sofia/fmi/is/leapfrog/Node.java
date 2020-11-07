package uni.sofia.fmi.is.leapfrog;

import java.util.ArrayList;

public class Node {
	private String data;
	private ArrayList<Node> children;
	private Node parent;

	public Node() {
		data = "";
		children = new ArrayList<Node>();
		parent = null;
	}

	public Node(String data) {
		this.data = data;
		children = new ArrayList<Node>();
		parent = null;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		child.parent = this;
		children.add(child);
	}

	public String getData() {
		return data;
	}

	public Node getParent() {
		return parent;
	}
}
