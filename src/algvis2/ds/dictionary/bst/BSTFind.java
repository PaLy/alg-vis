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

import algvis2.core.Algorithm;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;

public class BSTFind extends Algorithm {
	private final BST D;
	private final int x;

	protected BSTFind(BST D, int x) {
		super(D);
		this.D = D;
		this.x = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		BSTNode newNode = new BSTNode(x, NodePaint.FIND);
		addNode(newNode, ZDepth.TOP);

		BSTNode cur = D.getRoot();
		while (cur != null) {
			newNode.goAbove(cur);
			pause();
			if (cur.getKey() == x) {
				newNode.goTo(cur);
				pause();
				break;
			} else {
				if (cur.getKey() < x)
					cur = cur.getRight();
				else
					cur = cur.getLeft();
			}
		}

		removeNode(newNode);
	}
}
