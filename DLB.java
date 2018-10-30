import java.util.*;

public class DLB {//creating a DLB with the english dictionary

	private Node root;
	private Node current;
	private int i = 0;
	private boolean done;
	List<String> results;
	List<String> list;

	private class Node {
		private Node child;
		private Node sibling;
		private char value;

		public Node(char value) {
			this.value = value;
			this.child = null;
			this.sibling = null;
		}

		public Node() {
		}

		public boolean hasSibling() {
			return sibling != null;
		}

		public boolean hasChild() {
			return child != null;
		}
	}

	public DLB() {
		root = new Node();
	}

	public void clearList() {
		results.clear();
	}

	private Node addNewNode(char value) {
		return new Node(value);
	}

	public void add(String word) {
		word += "^";//terminator character
		current = root;//go back to the top for every word
		if (word == null) {
			throw new UnsupportedOperationException("Word is null");
		} else {
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				current = addChild(c, current);//moving the current reference for every letter
			}
		}
	}

	public Node addChild(char c, Node current) {
		if (current.child == null) {//go down
			current.child = addNewNode(c);
			return current.child;
		}
		else {//if child already exists then we go right
			return addSibling(c, current.child);
		}
	}

	public Node addSibling(char c, Node current) {
		if (current.value == c) {//move to next letter
			return current;
		}
		else {//need to go to sibling
			while (current.hasSibling()) {//finding the correct sibling
				current = current.sibling;
				if (current.value == c) {//if we found the correct sibling
					return current;
				}
			}
			current.sibling = addNewNode(c);//if the correct sibling wasn't there yet
			return current.sibling;
		}
	}

	private Node get(Node node, String prefix, int depth) {//finding the last node of the prefix
        if (node == null) {
        	System.out.println("No Predictions Found");
        	return null;
        }
        if (depth == prefix.length()) {
        	return node;
        }
        char c = prefix.charAt(depth);
        if (c == node.value) {//progressing through the prefix
        	return get(node.child, prefix, depth + 1);
        }
        else {
        	return get(node.sibling, prefix, depth);
        }
    }

	public List<String> keysWithPrefix(String prefix) {//finding the prefix in the DLB
		results = new LinkedList<String>();//instantiating a new deque so we can forget about the last word
		Node node = get(root.child, prefix, 0);//node should be the last node of the prefix
		collect(node, new StringBuilder(prefix), results);//this should get the words that start with this prefix
		return results;
	}

	private node collect(Node node, StringBuilder prefix, List<String> results) {//collecting words with the given prefix
		if (node == null) {//should get here when we've reached the end of a branch
			return node;
		}
		if (node.value == '^') {//base case
			results.add(prefix.toString());//if empty node then our word is done
			collect(node.sibling, prefix, results);

		}
		for (char c = 39; c <= 122; c++) {//Iterating through ASCII characters
			prefix.append(c);
			if (c == node.value)  {//check if we should go down 
				node.child = collect(node.child, prefix, results);
			}
			else {//if it's not there we go right
				node.sibling = collect(node.sibling, prefix.deleteCharAt(prefix.length() - 1), results);
			}
		}
		return node;
	}
}