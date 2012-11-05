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

package algvis2.scene.layout;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * -----------------------
 * |      | root |       |
 * -----------------------
 * | left |      | right |
 * -----------------------
 */
public class BinTreeLayout extends GridPane implements Layout {

	public BinTreeLayout() {
		super();
	}

	public void rebuild(Node root, Node left, Node right) {
		getChildren().clear();
		add(root, 1, 0);
		if (left != null) add(left, 0, 1);
		if (right != null) add(right, 2, 1);
	}

	@Override
	public void rebuild(Node... nodes) {
		switch (nodes.length) {
			case 0:
				break;
			case 1:
				rebuild(nodes[0], null, null);
				break;
			case 2:
				rebuild(nodes[0], nodes[1], null);
				break;
			default:
				rebuild(nodes[0], nodes[1], nodes[2]);
				break;
		}
	}

	@Override
	public Pane getPane() {
		return this;
	}
}
