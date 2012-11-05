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

import algvis2.scene.layout.Layout;
import algvis2.scene.shape.Node;

public class BSTNode extends Node {
	private Layout layout;
	private BSTNode left, right;

	public BSTNode(int key) {
		this(key, BST.DEF_LAYOUT);
	}
	
	public BSTNode(int key, String layoutName) {
		super(key);
		layout = Layout.Creator.get(layoutName);
		rebuildLayout();
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
	
	public Layout getLayout() {
		return layout;
	}

	public void setLayoutRec(String layoutName) {
		layout = Layout.Creator.get(layoutName);
		if (left != null) left.setLayoutRec(layoutName);
		if (right != null) right.setLayoutRec(layoutName);
		rebuildLayout();
	}
	
	private void rebuildLayout() {
		layout.rebuild(
			this,
			left == null ? null : left.getLayout().getPane(),
			right == null ? null : right.getLayout().getPane()
		);
	}
}
