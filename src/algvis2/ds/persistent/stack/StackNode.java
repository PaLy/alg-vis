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

package algvis2.ds.persistent.stack;

import algvis2.scene.text.Fonts;
import algvis2.scene.viselem.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StackNode extends Node {
	StackNode nextNode;
	ObjectProperty<LinkedHashMap<Node, Boolean>> parentNodesProperty = new SimpleObjectProperty<LinkedHashMap<Node, 
			Boolean>>();

	public StackNode(int key, StackNode nextNode) {
		super(key);
		this.nextNode = nextNode;
		parentNodesProperty.set(new LinkedHashMap<Node, Boolean>());
		if (nextNode != null) {
			nextNode.parentNodes().put(this, true);
		}
	}
	
	LinkedHashMap<Node, Boolean> parentNodes() {
		return parentNodesProperty.getValue();
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);		
		state.put(parentNodesProperty, parentNodesProperty.getValue().entrySet().toArray());
		for (Map.Entry<Node, Boolean> entry : parentNodes().entrySet()) {
			if (entry.getValue()) {
				entry.getKey().storeState(state);
			}
		}
	}

	public static class NullNode extends StackNode {
		public NullNode(int key, StackNode nextNode) {
			super(key, nextNode);
			if (nextNode != null) {
				nextNode.parentNodes().put(this, true);
				((Group) visual).getChildren().get(0).setVisible(false);
				Text text = (Text) ((Group) visual).getChildren().get(1);
				text.setFont(Fonts.VER_NO_FONT);
				text.setText("v" + text.getText());
				text.setX(-text.getBoundsInLocal().getWidth() / 2);
				text.setY(text.getY() - 10); // TODO inak ten text
			} else {
				((Group) visual).getChildren().set(0, new Line(-Node.RADIUS / 2, 0, Node.RADIUS / 2, 0));
				((Group) visual).getChildren().get(1).setVisible(false);
			}
		}
	}
}
