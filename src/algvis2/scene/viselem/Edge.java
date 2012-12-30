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
import javafx.scene.shape.Line;

public class Edge extends VisElem {
	public Edge() {
		super(new Line());
		setZDepth(ZDepth.EDGES);
	}

	@Override
	public Line getNode() {
		return (Line) super.getNode();
	}

	public void bindNodes(Node startNode, Node endNode) {
		getNode().startXProperty().bind(startNode.visPaneX.add(startNode.visPaneTranslateX));
		getNode().startYProperty().bind(startNode.visPaneY.add(startNode.visPaneTranslateY));
		getNode().endXProperty().bind(endNode.visPaneX.add(endNode.visPaneTranslateX));
		getNode().endYProperty().bind(endNode.visPaneY.add(endNode.visPaneTranslateY));
	}
}
