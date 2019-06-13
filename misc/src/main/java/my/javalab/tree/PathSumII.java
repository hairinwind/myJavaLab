package my.javalab.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// https://leetcode.com/problems/path-sum-ii/
public class PathSumII {

	public static List<List<Integer>> pathSum(TreeNode root, int sum) {
		List<List<Integer>> result = new LinkedList<List<Integer>>();
        if (root == null) {
            return result;
        }
        if (!isLeaf(root)) {
            List<List<Integer>> leftSum = pathSum(root.left, sum-root.val);
            List<List<Integer>> rightSum = pathSum(root.right, sum-root.val);
            result.addAll(addCurrentNode(root, leftSum));
            result.addAll(addCurrentNode(root, rightSum));
        } else { 
            if (root.val == sum) {
        		List<Integer> leaf = new ArrayList<Integer>();
        		leaf.add(root.val);
        		result.add(leaf);
        	}
        }
        return result;
    }
	
	public static List<List<Integer>> addCurrentNode(TreeNode node, List<List<Integer>> subPathSum) {
		if (subPathSum != null && !subPathSum.isEmpty()) {
			for(List<Integer> pathSum : subPathSum) {
				pathSum.add(0, node.val);
			}
			return subPathSum;
		}
		return new ArrayList<List<Integer>>();
	}
	
	public static boolean isLeaf(TreeNode node) {
        return node.left == null && node.right == null;
    }
	
	public static void main(String[] args) {
		TreeNode treeNode = new TreeNode(new Integer[]{1,0,1,1,2,0,-1,0,1,-1,0,-1,0,1,0});
		List<List<Integer>> result = pathSum(treeNode, 2);
		System.out.println(result);
	}

}
