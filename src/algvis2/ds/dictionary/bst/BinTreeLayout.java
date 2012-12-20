/*******************************************************************************
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
public class BinTreeLayout extends Layout {

	@Override
	protected Pane initPane() {
		return new GridPane();
	}

	protected void rebuildNodes(Node root, Node left, Node right) {
		((GridPane) pane).add(root, 1, 0);
		if (left != null)
			((GridPane) pane).add(left, 0, 1);
		if (right != null)
			((GridPane) pane).add(right, 2, 1);
	}

	private void rebuildEdges(BSTNode root, BSTNode left, BSTNode right, Pane leftPane, Pane rightPane) {
		if (left != null) {
			Edge leftEdge = new Edge();
			bindEdgeStart(leftEdge, root);
			bindEdgeEnd(leftEdge, left, leftPane);
			GridPane.setConstraints(leftEdge, 0, 0);
			pane.getChildren().add(leftEdge);
		}
		if (right != null) {
			Edge rightEdge = new Edge();
			bindEdgeStart(rightEdge, root);
			bindEdgeEnd(rightEdge, right, rightPane);
			GridPane.setConstraints(rightEdge, 0, 0);
			pane.getChildren().add(rightEdge);
		}
	}

	public Pane rebuild(BSTNode root) {
		pane.getChildren().clear();
		if (root.layoutXProperty().isBound()) {
			System.out.println("Bound: " + root.getKey());
		}
		BSTNode left = root.getLeft();
		BSTNode right = root.getRight();

		Pane leftPane = null;
		Pane rightPane = null;
		if (left != null) {
			try {
				leftPane = getClass().newInstance().rebuild(left);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (right != null) {
			try {
				rightPane = getClass().newInstance().rebuild(right);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		rebuildEdges(root, left, right, leftPane, rightPane);
		rebuildNodes(root, leftPane, rightPane);
		return pane;
	}

	@Override
	public Pane getPane() {
		return pane;
	}

	protected void bindEdgeStart(Edge edge, BSTNode root) {
		edge.startXProperty().bind(
				root.layoutXProperty().add(root.translateXProperty()));
		edge.startYProperty().bind(
				root.layoutYProperty().add(root.translateYProperty()));
	}

	protected void bindEdgeEnd(Edge edge, BSTNode node, Pane nodePane) {
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
