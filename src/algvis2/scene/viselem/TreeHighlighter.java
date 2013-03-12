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

import algvis2.ds.persistent.BinFatNode;
import algvis2.scene.layout.ZDepth;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.List;

public class TreeHighlighter extends VisElem {
	private final Color color = Color.AQUA;
	
	public TreeHighlighter() {
		super(new Group());
		setZDepth(ZDepth.SHADES);
	}

	@Override
	public Group getVisual() {
		return (Group) super.getVisual();
	}

	public void update(List<Node> list) {
		getVisual().getChildren().clear();
		ObservableList<javafx.scene.Node> visuals = getVisual().getChildren();

		for (int i = 1; i < list.size(); i += 2) {
			Line line = LineBuilder.create().stroke(color).strokeWidth(Node.RADIUS * 0.7).build();
			Node firsNode = list.get(i - 1);
			Node secondNode = list.get(i);
			line.startXProperty().bind(firsNode.visPaneX.add(firsNode.visPaneTranslateX));
			line.startYProperty().bind(firsNode.visPaneY.add(firsNode.visPaneTranslateY));
			line.endXProperty().bind(secondNode.visPaneX.add(secondNode.visPaneTranslateX));
			line.endYProperty().bind(secondNode.visPaneY.add(secondNode.visPaneTranslateY));

			visuals.add(line);
			visuals.add(newShape(firsNode));
			visuals.add(newShape(secondNode));
		}
		if (list.size() % 2 == 1) {
			visuals.add(newShape(list.get(list.size() - 1)));
		}
	}

	private Shape newShape(Node node) {
		Shape shape;
		if (node instanceof BinFatNode.Null) {
			double width = Node.RADIUS * 1.4;
			double height = Node.RADIUS * 0.8;
			shape = new Rectangle(-width / 2, -height / 2, width, height);
			shape.setFill(color);
		} else {
			shape = new Circle(Node.RADIUS * 1.3, color);
		}
		shape.layoutXProperty().bind(node.visPaneX.add(node.visPaneTranslateX));
		shape.layoutYProperty().bind(node.visPaneY.add(node.visPaneTranslateY));
		return shape;
	}
}
