/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package algvis2.ds.dictionary.bst;

import algvis2.scene.shape.Node;
import javafx.scene.layout.GridPane;

public class BSTNode extends Node {
	/**
	 * Contains this layout:
	 * -----------------------
	 * |      | this |       |
	 * -----------------------
	 * | left |      | right |
	 * -----------------------
	 */
	private GridPane layout = new GridPane();
	private BSTNode left, right;
	
	public BSTNode(int key) {
		super(key);
		layout.add(this, 1, 0);
	}
	
	public void setLeft(BSTNode left) {
		this.left = left;
		rebuildLayout();
	}
	
	public void setRight(BSTNode right) {
		this.right = right;
		rebuildLayout();
	}

	public BSTNode getLeft() {
		return left;
	}

	public BSTNode getRight() {
		return right;
	}
	
	public GridPane getLayout() {
		return layout;
	}
	
	private void rebuildLayout() {
		layout.getChildren().clear();
		layout.add(this, 1, 0);
		if (left != null) layout.add(left.layout, 0, 1);
		if (right != null) layout.add(right.layout, 2, 1);
	}
}
