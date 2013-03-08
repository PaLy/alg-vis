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

package algvis2.scene.viselem;

import algvis2.scene.layout.ZDepth;
import algvis2.scene.shape.Arrow;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

public class Edge extends VisElem {
	public Edge() {
		super(new Arrow(0));
		setZDepth(ZDepth.EDGES);
	}

	public Edge(double radius) {
		super(new Arrow(radius));
		setZDepth(ZDepth.EDGES);
	}

	@Override
	public Arrow getVisual() {
		return (Arrow) super.getVisual();
	}

	public void bindNodes(Node startNode, Node endNode) {
		getVisual().startXProperty().bind(startNode.visPaneX.add(startNode.visPaneTranslateX));
		getVisual().startYProperty().bind(startNode.visPaneY.add(startNode.visPaneTranslateY));

		getVisual().endXProperty().bind(
				endNode.visPaneX.add(endNode.visPaneTranslateX).subtract(
						getVisual().layoutXProperty()));
		getVisual().endYProperty().bind(
				endNode.visPaneY.add(endNode.visPaneTranslateY).subtract(
						getVisual().layoutYProperty()));
	}

	public void setOnMouseClicked(EventHandler<MouseEvent> eventHandler) {
		getVisual().setOnMouseClicked(eventHandler);
		getVisual().setCursor(Cursor.HAND);
	}
}
