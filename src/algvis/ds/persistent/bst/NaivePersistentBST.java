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

package algvis.ds.persistent.bst;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.ArrayOfPointers;
import algvis.core.visual.Scene;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTDelete;
import algvis.ds.dictionaries.bst.BSTInsert;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.persistent.PersistentDS;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class NaivePersistentBST extends PersistentDS {
	public static String dsName = "npbst";
	private ArrayList<BST> array = new ArrayList<BST>();
	private ArrayOfPointers arrayOfPointers;
	
	protected NaivePersistentBST(VisPanel panel) {
		super(panel);
	}

	@Override
	public void find(int x) {
		// TODO
	}

	@Override
	public String getName() {
		return dsName;
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
		start(new Insert(x));
	}

	@Override
	public void delete(int x) {
		start(new Delete(x));
	}

	@Override
	public void clear() {
		array = new ArrayList<BST>();
		panel.scene.clear();
		panel.scene.add(this);
		setStats();
		panel.screen.V.resetView();
	}

	@Override
	public void draw(View v) throws ConcurrentModificationException {
		for (BST bst : array) {
			bst.draw(v);
		}
	}

	@Override
	protected void move() throws ConcurrentModificationException {
		for (BST bst : array) {
			bst.move();
		}
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null; // TODO
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "array", array.clone());
		for (BST bst : array) {
			bst.storeState(state);
		}
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object array = state.get(hash + "array");
		if (array != null) {
			this.array = (ArrayList<BST>) HashtableStoreSupport.restore(array);
		}
		if (this.array != null) {
			for (BST bst : this.array) {
				bst.restoreState(state);
			}
		}
	}

	private abstract class Update extends Algorithm {
		protected Update(VisPanel panel) {
			super(panel);
		}
		
		protected BST addBST() {
			if (array.size() > 0) {
				addStep("npbst-copy-bst");
				BST newBST = array.get(array.size() - 1).clone();
				addToPreState(newBST);
				Rectangle2D newBSTBoundingBox = newBST.getBoundingBox();
				int sx = (int) newBSTBoundingBox.getWidth() + 10;
				for (BST bst : array) {
					bst.translate(-sx, 0);
				}
				newBST.getRoot().setTreeColor(NodeColor.NORMAL);
				array.add(newBST);
				return newBST;
			} else {
				BST newBST = new BST(NaivePersistentBST.this.panel); 
				array.add(newBST);
				addToPreState(newBST);
				arrayOfPointers = new ArrayOfPointers(0, -50, 20, Scene.MAXZ - 1);
				addToScene(arrayOfPointers);
				return newBST;
			}
		}
		
		protected void reposAtEnd() {
			BST newBST = array.get(array.size() - 1);
			arrayOfPointers.add("v" + array.size(), newBST.getRoot());
			
			if (array.size() > 1) {
				BST lastBST = array.get(array.size() - 2);
				Rectangle2D intersection = lastBST.getBoundingBox().createIntersection(newBST.getBoundingBox());
				if (intersection.getWidth() > 0) {
					int sx = (int) (intersection.getWidth() + 10);
					for (int i = 0; i < array.size() - 1; i++) {
						array.get(i).translate(-sx, 0);
					}
				}
			}
		}
	}
	
	private class Insert extends Update {
		private final int x;

		protected Insert(int x) {
			super(NaivePersistentBST.this.panel);
			this.x = x;
		}

		@Override
		public void runAlgorithm() throws InterruptedException {
			BST newBST = addBST();
			
			BSTInsert insert = new BSTInsert(newBST, new BSTNode(newBST, x), curAlgorithm);
			insert.runAlgorithm();
			BSTNode inserted = (BSTNode) insert.getResult().get("v");
			inserted.setColor(NodeColor.INSERT);
			
			reposAtEnd();
		}
	}

	private class Delete extends Update {
		private final int x;

		protected Delete(int x) {
			super(NaivePersistentBST.this.panel);
			this.x = x;
		}

		@Override
		public void runAlgorithm() throws InterruptedException {
			BST newBST = addBST();
			new BSTDelete(newBST, x, curAlgorithm).runAlgorithm();
			reposAtEnd();
		}
	}
}
