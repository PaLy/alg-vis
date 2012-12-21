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

import algvis2.core.PropertyStateEditable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class VisPane extends StackPane implements PropertyStateEditable {
	private Map<Node, Pane> elementParent = new WeakHashMap<Node, Pane>();
	public static final String ID = "visPane";

	private double mouseX, mouseY;

	public VisPane() {
		super();
		setId(ID);
		AnchorPane.setLeftAnchor(this, 0.0);
		AnchorPane.setRightAnchor(this, 0.0);
		AnchorPane.setTopAnchor(this, 0.0);
		AnchorPane.setBottomAnchor(this, 0.0);
		ObservableList<Node> children = getChildren();
		for (int i = 0; i <= ZDepth.TOP; i++) {
			if (i == ZDepth.NODES) {
				FlowPane ds = FlowPaneBuilder.create().alignment(Pos.TOP_CENTER)
						.build();
				StackPane.setMargin(ds, new Insets(25 + algvis2.scene.shape.Node.RADIUS * 2.5, 0, 0, 0));
				children.add(ds);
			} else {
				children.add(new Pane());
			}
		}

		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				mouseX = mouseEvent.getSceneX();
				mouseY = mouseEvent.getSceneY();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				setTranslateX(getTranslateX() + mouseEvent.getSceneX() - mouseX);
				mouseX = mouseEvent.getSceneX();
				setTranslateY(getTranslateY() + mouseEvent.getSceneY() - mouseY);
				mouseY = mouseEvent.getSceneY();
			}
		});
	}

	public void add(Node node, int zDepth) {
		Pane layer = (Pane) getChildren().get(zDepth);
		layer.getChildren().add(node);
		elementParent.put(node, layer);
	}

	public void remove(Node node) {
		elementParent.get(node).getChildren().remove(node);
	}

	public void recalcAbsPosition() {
		for(Node child : getChildren()) {
			recalcAbsPosition((Pane) child);
		}
	}

	private void recalcAbsPosition(Pane parent) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			if (node instanceof Pane) {
				recalcAbsPosition((Pane) node);
			} else if (node instanceof AbsPosition) {
				((AbsPosition) node).recalcAbsPosition();
			}
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		for (Node child : getChildren()) {
			if (!(child instanceof FlowPane)) {
				state.put(
						child,
						new ArrayList<Node>(((Parent) child)
								.getChildrenUnmodifiable()));
				storeStateR(state, (Parent) child);
			}
		}
	}

	private void storeStateR(HashMap<Object, Object> state, Parent parent) {
		for (Node child : parent.getChildrenUnmodifiable()) {
			if (child instanceof PropertyStateEditable)
				((PropertyStateEditable) child).storeState(state);
			else if (child instanceof Parent)
				storeStateR(state, (Parent) child);
		}
	}
}
