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

import javafx.scene.layout.GridPane;

/**
 * ----------------
 * | root |       |
 * ----------------
 * | left | right |
 * ----------------
 */
public class LeftBinTreeLayout extends BinTreeLayout {
	
	public LeftBinTreeLayout() {
		super();
		((GridPane) pane).setVgap(5);
		((GridPane) pane).setHgap(5);
	}

	@Override
	protected void rebuildNodes(BSTNode root, BSTNode left, BSTNode right) {
		((GridPane) pane).add(root, 0, 0);
		if (left != null) ((GridPane) pane).add(left.getLayout().getPane(), 0, 1);
		if (right != null) ((GridPane) pane).add(right.getLayout().getPane(), 1, 1);
	}
}
