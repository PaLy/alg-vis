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

import algvis2.animation.ParallelTranslateTransition;
import algvis2.core.PropertyStateEditable;
import algvis2.scene.Axis;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class VisPane extends StackPane implements PropertyStateEditable {
	private Map<Node, Pane> elementParent = new WeakHashMap<Node, Pane>();
	public static final String ID = "visPane";
	
	public VisPane() {
		super();
		setId(ID);
		ObservableList<Node> children =  getChildren();
		for (int i = 0; i <= ZDepth.TOP; i++) {
			if (i == ZDepth.NODES) {
				children.add(FlowPaneBuilder.create().alignment(Pos.TOP_CENTER).build());
			} else {
				children.add(new Pane());
			}
		}
		
		layoutXProperty().addListener(new ParallelTranslateTransition(this, Axis.X));
		layoutYProperty().addListener(new ParallelTranslateTransition(this, Axis.Y));
	}
	
	public void add(Node node, int zDepth) {
		Pane layer = (Pane) getChildren().get(zDepth); 
		layer.getChildren().add(node);
		elementParent.put(node, layer);
	}

	public void remove(Node node) {
		elementParent.get(node).getChildren().remove(node);
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
        state.put(this, new ArrayList<Node>(getChildren()));
        for (Node child : getChildren()) {
            if (child instanceof PropertyStateEditable) ((PropertyStateEditable) child).storeState(state);
            else if (child instanceof Parent) storeStateR(state, (Parent) child);
        }
	}
	
	private void storeStateR(HashMap<Object, Object> state, Parent parent) {
//        System.out.println("aaa " + parent);
		for (Node child : parent.getChildrenUnmodifiable()) {
//            if (parent instanceof GridPane) System.out.println("GGG " + child);
			if (child instanceof PropertyStateEditable) ((PropertyStateEditable) child).storeState(state);
			else if (child instanceof Parent) storeStateR(state, (Parent) child);
		}
	}
}