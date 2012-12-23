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

package algvis2.ds.dictionary.rb;

import algvis2.ds.dictionary.bst.BSTInsert;

public class RBInsert extends BSTInsert {
	protected RBInsert(RB D, RBNode newNode) {
		super(D, newNode);
	}

	@Override
	public void runAlgorithm() {
		super.runAlgorithm();
		pause();

		// bubleme nahor
		RBNode w = (RBNode) newNode;
		RBNode pw = w.getParentNode2();
		while (!w.isRoot() && pw.isRed()) {
			w.mark();
			final boolean isLeft = pw.isLeft();
			RBNode ppw = pw.getParentNode2();
			RBNode y = (isLeft ? ppw.getRight() : ppw.getLeft());
			if (y == null) {
				y = ((RB) D).NULL;
			}
			if (y.isRed()) {
				// case 1
				pause();
				pw.setRed(false);
				y.setRed(false);
				ppw.setRed(true);
				w.unmark();
				w = ppw;
				w.mark();
				pw = w.getParentNode2();
				pause();
			} else {
				// case 2
				if (isLeft != w.isLeft()) {
					pause();
					D.rotate(w);
					pause();
				} else {
					w.unmark();
					w = w.getParentNode2();
					w.mark();
				}
				pw = w.getParentNode2();
				// case 3
				pause();
				w.setRed(false);
				pw.setRed(true);
				D.rotate(w);
				pause();
				w.unmark();
				break;
			}
		}

		w.unmark();
		((RB) D).getRoot().setRed(false);
	}
}
