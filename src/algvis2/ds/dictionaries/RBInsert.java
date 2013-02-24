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

package algvis2.ds.dictionaries;

import algvis2.scene.viselem.Marker;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

class RBInsert extends BSTInsert {
	protected RBInsert(RB D, RBNode newNode) {
		super(D, newNode);
	}

	@Override
	public void runAlgorithm() {
		super.runAlgorithm();
		pause(true);

		// bubleme nahor
		ObjectProperty<RBNode> w = new SimpleObjectProperty<>();
		w.set((RBNode) newNode);
		RBNode pw = w.get().getParent2();
		
		Marker wMarker = new Marker();
		wMarker.elem.bind(w);
		addVisElem(wMarker);
		
		while (!w.get().isRoot() && pw.isRed()) {
			boolean isLeft = pw.isLeft();
			RBNode ppw = pw.getParent2();
			RBNode y = (isLeft ? ppw.getRight() : ppw.getLeft());
			if (y == null) {
				y = RB.NULL;
			}
			if (y.isRed()) {
				// case 1
				pause(false);
				pw.setRed(false);
				y.setRed(false);
				ppw.setRed(true);
				w.set(ppw);
				pw = w.get().getParent2();
				pause(false);
			} else {
				// case 2
				if (isLeft != w.get().isLeft()) {
					pause(false);
					D.rotate(w.get());
					pause(true);
				} else {
					w.set(w.get().getParent2());
				}
				pw = w.get().getParent2();
				// case 3
				pause(false);
				w.get().setRed(false);
				pw.setRed(true);
				D.rotate(w.get());
				pause(true);
				break;
			}
		}
		
		((RB) D).getRoot().setRed(false);

		wMarker.elem.unbind();
		removeVisElem(wMarker);
	}
}
