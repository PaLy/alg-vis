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

import algvis2.core.Dictionary;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;

public class BST extends Dictionary {
	private BSTNode root = null;
	
	public BST(VisPane visPane) {
		super(visPane);
	}

	@Override
	public void find(int x) {
		// TODO
	}

	@Override
	public void delete(int x) {
		// TODO
	}

	@Override
	public String getStats() {
		return null; // TODO
	}

	@Override
	public void insert(int x) {
		BSTNode newNode = new BSTNode(x);
		if (root == null) {
			setRoot(newNode);
		} else {
			BSTNode cur = root;
			while (true) {
				if (x > cur.key) {
					if (cur.getRight() == null) {
						cur.setRight(newNode);
						break;
					} else {
						cur = cur.getRight();
					}
				} else {
					if (cur.getLeft() == null) {
						cur.setLeft(newNode);
						break;
					} else {
						cur = cur.getLeft();
					}
				}
			}
		}
	}

	private void setRoot(BSTNode root) {
		this.root = root;
		visPaneLayers[ZDepth.NODES].getChildren().clear();
		if (root != null) visPaneLayers[ZDepth.NODES].getChildren().add(root.getLayout());
	}

	@Override
	public void clear() {
		setRoot(null);
	}
}
