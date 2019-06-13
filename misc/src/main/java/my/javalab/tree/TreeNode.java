package my.javalab.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * construct the array [5,4,8,11,null,13,4,7,2,null,null,5,1] to this tree 
 *      5
 *     / \
 *    4   8
 *   /   / \
 *  11  13  4
 * /  \    / \
 *7    2  5   1
 *
 */
public class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;
	public TreeNode(int x) { 
		val = x;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(val).append(",");
		Queue<TreeNode> nodeQueue = new LinkedList<TreeNode>();
		nodeQueue.add(this);
		while (!nodeQueue.isEmpty()) {
			TreeNode node = nodeQueue.poll();
			if (node.left != null) {
				nodeQueue.add(node.left);
				sb.append(node.left.val).append(",");
			}else {
				sb.append("null").append(",");
			}
			if (node.right != null) {
				nodeQueue.add(node.right);
				sb.append(node.right.val).append(",");
			} else {
				sb.append("null").append(",");
			}
		}
		return sb.toString();
	}

	public TreeNode(Integer[] array) {
		Queue<TreeNode> nodeQueue = new LinkedList<TreeNode>();
		this.val = array[0];
		nodeQueue.add(this);
		for (int i = 1; i < array.length; i=i+2) {
			TreeNode currentNode = nodeQueue.poll();
//			System.out.println("currentNode val:" + currentNode.val);
			if (array[i] != null) {
				currentNode.left = new TreeNode(array[i]);
//				System.out.println("add " + array[i] + " as left of " + currentNode.val);
				nodeQueue.add(currentNode.left);				
			}
			if (array[i+1] != null) {
				currentNode.right = new TreeNode(array[i+1]);
//				System.out.println("add " + array[i+1] + " as right of " + currentNode.val);
				nodeQueue.add(currentNode.right);				
			}
		}
	}
	
	public static void main(String[] args) {
		TreeNode node = new TreeNode(new Integer[]{5,4,8,11,null,13,4,7,2,null,null,5,1});
		System.out.println(node);
	}
}
