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

import algvis2.scene.layout.BinTreeLayout;
import algvis2.scene.layout.Layouts;
import algvis2.scene.layout.LeftBinTreeLayout;
import algvis2.scene.layout.RightBinTreeLayout;
import algvis2.scene.shape.Node;

public class BSTNode extends Node {
	private BinTreeLayout layout;
	private BSTNode left, right;
	
	public BSTNode(int key, String layoutName) {
		super(key);
		layout = createLayout(layoutName);
		layout.rebuild(this, null, null);
	}
	
	public void setLeft(BSTNode left) {
		this.left = left;
		layout.rebuild(this, left.getLayout(), right == null ? null : right.getLayout());
	}
	
	public void setRight(BSTNode right) {
		this.right = right;
		layout.rebuild(this, left == null ? null : left.getLayout(), right.getLayout());
	}

	public BSTNode getLeft() {
		return left;
	}

	public BSTNode getRight() {
		return right;
	}
	
	public BinTreeLayout getLayout() {
		return layout;
	}

	public void setLayoutRec(String layoutName) {
		layout = createLayout(layoutName);
		if (left != null) left.setLayoutRec(layoutName);
		if (right != null) right.setLayoutRec(layoutName);
		layout.rebuild(
			this,
			left == null ? null : left.getLayout(),
			right == null ? null : right.getLayout()
		);
	}
	
	private BinTreeLayout createLayout(String layoutName) {
		if (layoutName.equals(Layouts.BIN_TREE_LAYOUT)) return new BinTreeLayout();
		else if (layoutName.equals(Layouts.LEFT_BIN_TREE_LAYOUT)) return new LeftBinTreeLayout();
		else if (layoutName.equals(Layouts.RIGHT_BIN_TREE_LAYOUT)) return new RightBinTreeLayout();
		else return null;
	}
}
