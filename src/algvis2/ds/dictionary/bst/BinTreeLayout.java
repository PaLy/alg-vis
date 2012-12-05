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
import algvis2.scene.shape.Edge;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * -----------------------
 * |      | root |       |
 * -----------------------
 * | left |      | right |
 * -----------------------
 */
public class BinTreeLayout extends Layout {

	@Override
	protected Pane initPane() {
		return new GridPane();
	}

	protected void rebuildNodes(BSTNode root, BSTNode left, BSTNode right) {
		((GridPane) pane).add(root, 1, 0);
		if (left != null)
			((GridPane) pane).add(left.getLayoutPane(), 0, 1);
		if (right != null)
			((GridPane) pane).add(right.getLayoutPane(), 2, 1);
	}

	private void rebuildEdges(BSTNode root, BSTNode left, BSTNode right) {
		if (left != null) {
			Edge leftEdge = new Edge();
			bindEdgeStart(leftEdge, root);
			bindEdgeEnd(leftEdge, left);
			GridPane.setConstraints(leftEdge, 0, 0);
			pane.getChildren().add(leftEdge);
		}
		if (right != null) {
			Edge rightEdge = new Edge();
			bindEdgeStart(rightEdge, root);
			bindEdgeEnd(rightEdge, right);
			GridPane.setConstraints(rightEdge, 0, 0);
			pane.getChildren().add(rightEdge);
		}
	}

	public void rebuild(BSTNode root, BSTNode left, BSTNode right) {
		pane.getChildren().clear();
//		if (root.layoutXProperty().isBound()) {
//			System.out.println(root.getKey() + "ROOT");
//			System.out.println(root.layoutXProperty());
//		}
		root.removeLayoutXYBindings();
		if (left != null) {
			left.removeLayoutXYBindings();
//			if (left.layoutXProperty().isBound()) {
//				System.out.println(left.getKey() + "LEFT");
//			}
			left.reLayout();
		}
		if (right != null) {
			right.removeLayoutXYBindings();
//			if (right.layoutXProperty().isBound()) {
//				System.out.println(right.getKey() + "RIGHT");
//			}
			right.reLayout();
		}

		rebuildEdges(root, left, right);
		rebuildNodes(root, left, right);
	}

	@Override
	public Pane getPane() {
		return pane;
	}

	private void bindEdgeStart(Edge edge, BSTNode root) {
		edge.startXProperty().bind(
				root.layoutXProperty().add(root.translateXProperty()));
		edge.startYProperty().bind(
				root.layoutYProperty().add(root.translateYProperty()));
	}

	private void bindEdgeEnd(Edge edge, BSTNode node) {
		Pane nodePane = node.getLayoutPane();
		edge.endXProperty().bind(
				nodePane.layoutXProperty().add(nodePane.translateXProperty())
						.add(node.layoutXProperty())
						.add(node.translateXProperty()));
		edge.endYProperty().bind(
				nodePane.layoutYProperty().add(nodePane.translateYProperty())
						.add(node.layoutYProperty())
						.add(node.translateYProperty()));
	}
}
