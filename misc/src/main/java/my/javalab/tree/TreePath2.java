package my.javalab.tree;

import java.util.ArrayList;
import java.util.List;

public class TreePath2 {
	
	public List<Integer> getBreadCrumb(TreeNode root, int nodeVal) {
		if (root == null) return null;
		if (root.val == nodeVal) {
			List<Integer> result = new ArrayList<>(); 
			result.add(root.val);
			return result;
		}
		List<Integer> leftMatched = getBreadCrumb(root.left, nodeVal);
		if (leftMatched != null) {
			leftMatched.add(root.val);
			return leftMatched;
		}
		List<Integer> rightMatched = getBreadCrumb(root.right, nodeVal);
		if (rightMatched != null) {
			rightMatched.add(root.val);
			return rightMatched;
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println("running...");
		//construct the tree
		// 1
		//  |- 11
		//     | - 111
		//  |- 12
		//     | - 121
		//         | - 1211
		//     | - 122
		TreeNode root = new TreeNode(1);
		root.left = new TreeNode(11);
		root.right = new TreeNode(12);
		root.left.left = new TreeNode(111);
		root.right.left = new TreeNode(121);
		root.right.right = new TreeNode(122);
		root.right.left.left = new TreeNode(1211);

		TreePath2 treePath = new TreePath2();
		List<Integer> path = treePath.getBreadCrumb(root, 1211);
		System.out.println("path...." + path);
	}

}
