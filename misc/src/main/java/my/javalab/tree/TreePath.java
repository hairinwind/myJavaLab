package my.javalab.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreePath {
	
	public List<String> getBreadCrumb(Category root, String categoryName) {
		List<String> treePathFound = this.getMatchedSubCategory(root, categoryName);
		if (treePathFound.size() > 0) {
			treePathFound.add(root.getName());
			// the sequence in the list is from leaf to the root, reverse it to get it from root to leaf
			Collections.reverse(treePathFound);
		}
		return treePathFound;
	}
	
	private List<String> getMatchedSubCategory(Category root, String categoryName) {
		List<String> result = new ArrayList<String>();
		if (root.getSubCategories() == null) {
			return result;
		}
		for (Category subCategory : root.getSubCategories()) {
			if (subCategory.isEqual(categoryName)){
				result.add(subCategory.getName());
				return result;
			} else {
				List<String> hasSubCategoryMatched =  getMatchedSubCategory(subCategory, categoryName);
				if (hasSubCategoryMatched.size() == 0) {
					continue;
				} else {
					result.addAll(hasSubCategoryMatched);
					result.add(subCategory.getName());
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		//construct the tree
		// root
		//  |- c11
		//     | - c111
		//  |- c12
		//     | - c121
		//         | - c1211
		//     | - c122
		Category root = new Category("root");
		Category c11 = new Category("c11");
		Category c12 = new Category("c12");
		root.addSubCategory(c11);
		root.addSubCategory(c12); //c11 and c12 are on the same level
		Category c111 = new Category("c111");
		c11.addSubCategory(c111);
		Category c121 = new Category("c121");
		c12.addSubCategory(c121);
		Category c122 = new Category("c122");
		c12.addSubCategory(c122);
		Category c1211 = new Category("c1211");
		c121.addSubCategory(c1211); // this outputs [root, c12, c121, c1211]
//		c12.addSubCategory(c1211); // if I add c1211 as the child of c12, it outputs [root, c12, c1211]
		
		TreePath treePath = new TreePath();
		List<String> path = treePath.getBreadCrumb(root, "c1211");
		System.out.println(path);
	}

}

class Category {
	private String name;
	private List<Category> subCategories;
	public Category(String category) {
		this.name = category;
	}
	public void addSubCategory(Category subCat) {
		if (subCategories == null) {
			subCategories = new ArrayList<Category>();
		}
		subCategories.add(subCat);
	}
	public boolean isEqual(String name) {
		return this.name.equals(name);
	}
	public String getName() {
		return name;
	}
	public List<Category> getSubCategories() {
		return subCategories;
	}
}
