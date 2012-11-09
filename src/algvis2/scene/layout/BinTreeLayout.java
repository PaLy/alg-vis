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
	
	public void rebuild(Node root, Node leftPane, Node rightPane, Node left, Node right) {
		pane.getChildren().clear();
		rebuildEdges(root, leftPane, rightPane, left, right);
		rebuildNodes(root, leftPane, rightPane);
	}
	
	protected void rebuildNodes(Node root, Node left, Node right) {
		((GridPane) pane).add(root, 1, 0);
		if (left != null) ((GridPane) pane).add(left, 0, 1);
		if (right != null) ((GridPane) pane).add(right, 2, 1);
	}
	
	private void rebuildEdges(Node root, Node leftPane, Node rightPane, Node left, Node right) {
		if (left != null) {
			Edge leftEdge = new Edge();
			bindEdgeStart(leftEdge, root);
			bindEdgeEnd(leftEdge, leftPane, left);
			GridPane.setConstraints(leftEdge, 0, 0);
			pane.getChildren().add(leftEdge);
		}
		if (right != null) {
			Edge rightEdge = new Edge();
			bindEdgeStart(rightEdge, root);
			bindEdgeEnd(rightEdge, rightPane, right);
			GridPane.setConstraints(rightEdge, 0, 0);
			pane.getChildren().add(rightEdge);
		}
	}

	@Override
	public void rebuild(Node... nodes) {
		switch (nodes.length) {
			case 0:
				break;
			case 1:
				rebuild(nodes[0], null, null, null, null);
				break;
			case 2:
				rebuild(nodes[0], nodes[1], null, null, null);
				break;
			case 3:
				rebuild(nodes[0], nodes[1], nodes[2], null, null);
				break;
			case 4:
				rebuild(nodes[0], nodes[1], nodes[2], nodes[3], null);
				break;
			default:
				rebuild(nodes[0], nodes[1], nodes[2], nodes[3], nodes[4]);
		}
	}

	@Override
	public Pane getPane() {
		return pane;
	}
	
	private void bindEdgeStart(Edge edge, Node root) {
		edge.startXProperty().bind(root.layoutXProperty()
			.add(root.translateXProperty()));
		edge.startYProperty().bind(root.layoutYProperty()
			.add(root.translateYProperty()));
	}

	private void bindEdgeEnd(Edge edge, Node nodePane, Node node) {
		edge.endXProperty().bind(nodePane.layoutXProperty()
			.add(nodePane.translateXProperty())
			.add(node.layoutXProperty())
			.add(node.translateXProperty()));
		edge.endYProperty().bind(nodePane.layoutYProperty()
			.add(nodePane.translateYProperty())
			.add(node.layoutYProperty())
			.add(node.translateYProperty()));
	}
}
