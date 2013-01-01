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

import algvis2.animation.AutoTranslateTransition;
import algvis2.core.DataStructure;
import algvis2.core.PropertyStateEditable;
import algvis2.scene.Axis;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.VisElem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.util.*;

public class VisPane implements PropertyStateEditable, AbsPosition {
	private final Pane pane = new Pane();
	private final DataStructure dataStructure;
	private ArrayList<VisElem> dsElements = new ArrayList<VisElem>();
	private final FlowPane wrappingPane = new FlowPane();
	private final ObjectProperty<Set<VisElem>> children = new SimpleObjectProperty<Set<VisElem>>();
	public static final String ID = "visPane";
	private HashSet<ZDepth> doNotShow = new HashSet<ZDepth>();
	private final Set<VisElem> notStorableChildren = new TreeSet<VisElem>();

	private double mouseX, mouseY;
	private final AutoTranslateTransition xTranslation = new AutoTranslateTransition(pane, Axis.X);
	private final AutoTranslateTransition yTranslation = new AutoTranslateTransition(pane, Axis.Y);
	private boolean canTranslate;

	public VisPane(DataStructure dataStructure) {
		this.dataStructure = dataStructure;
		
		children.set(new TreeSet<VisElem>());
		pane.setId(ID);
		AnchorPane.setLeftAnchor(wrappingPane, 0.0);
		AnchorPane.setRightAnchor(wrappingPane, 0.0);
		AnchorPane.setTopAnchor(wrappingPane, 0.0);
		AnchorPane.setBottomAnchor(wrappingPane, 0.0);
		wrappingPane.setAlignment(Pos.TOP_CENTER);
		wrappingPane.getChildren().add(pane);

		wrappingPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				mouseX = mouseEvent.getSceneX();
				mouseY = mouseEvent.getSceneY();
				canTranslate = !pane.getChildren().isEmpty();
			}
		});

		wrappingPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (canTranslate) {
					xTranslation.translate(mouseEvent.getSceneX() - mouseX);
					mouseX = mouseEvent.getSceneX();
					yTranslation.translate(mouseEvent.getSceneY() - mouseY);
					mouseY = mouseEvent.getSceneY();
				}
			}
		});
		
		pane.layoutXProperty().addListener(xTranslation);
		pane.layoutYProperty().addListener(yTranslation);
		
		add(dataStructure);
	}
	
	public Pane getWrappingPane() {
		return wrappingPane;
	}
	
	public void setDsElements(ArrayList<VisElem> elements) {
		dsElements = elements;
	}
	
	public void refresh() {
		Set<VisElem> allChildren = new TreeSet<VisElem>(children.get());
		allChildren.addAll(notStorableChildren);
		
		List<Node> paneChildren = pane.getChildren();
		dataStructure.getNode().getChildren().clear();
		for (VisElem elem : dsElements) {
			dataStructure.getNode().getChildren().add(elem.getNode());
		}
		paneChildren.clear();
		for (VisElem elem : allChildren) {
			if (!doNotShow.contains(elem.getZDepth())) {
				paneChildren.add(elem.getNode());
			}
		}
		recalcAbsPosition();
	}

	public void add(VisElem elem) {
		children.get().add(elem);
	}

	public void remove(VisElem elem) {
		children.get().remove(elem);
	}
	
	public void hideLayer(ZDepth zDepth) {
		doNotShow.add(zDepth);
	}

	public void showLayer(ZDepth zDepth) {
		doNotShow.remove(zDepth);
	}

	public void clearLayer(ZDepth zDepth) {
		if (zDepth.equals(ZDepth.EDGES)) notStorableChildren.clear();
		else {
			ArrayList<VisElem> toRemove = new ArrayList<VisElem>();
			for (VisElem elem : children.get()) {
				if (elem.getZDepth().equals(zDepth)) {
					toRemove.add(elem);
				}
			}
			for (VisElem elem : toRemove) {
				children.get().remove(elem);
			}
		}
	}

	public void recalcAbsPosition() {
		for (VisElem elem : children.get()) {
			if (elem instanceof AbsPosition) {
				((AbsPosition) elem).recalcAbsPosition();
			}
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(children, children.get().toArray());
		
		for (VisElem elem : children.get()) {
			if (elem instanceof PropertyStateEditable) {
				((PropertyStateEditable) elem).storeState(state);
			}
		}
	}

	public void addNotStorableVisElem(Edge edge) {
		notStorableChildren.add(edge);
	}

	public void clearPane() {
		pane.getChildren().clear();
	}

	public void setTranslatePos(int x, int y) {
		pane.setTranslateX(x);
		pane.setTranslateY(y);
	}
}
