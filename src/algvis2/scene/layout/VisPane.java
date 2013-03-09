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

import algvis2.core.DataStructure;
import algvis2.core.PropertyStateEditable;
import algvis2.ds.persistent.FN_PBST;
import algvis2.scene.viselem.VisElem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.util.*;

public class VisPane implements PropertyStateEditable, AbsPosition {
	private final FlowPane pane = new FlowPane();
	private final DataStructure dataStructure;
	private List<VisElem> dsElements = new ArrayList<>();
	private final AnchorPane wrappingPane = new AnchorPane();
	/**
	 * Sorted elements (by layers) which should be on scene.
	 */
	private final ObjectProperty<HashSet<VisElem>> children = new SimpleObjectProperty<>();
	public static final String ID = "visPane";
	private HashSet<ZDepth> doNotShow = new HashSet<>();
	private final Set<VisElem> notStorableChildren = new HashSet<>();

	private double mouseX, mouseY;
	private boolean canTranslate;

	public VisPane(DataStructure dataStructure) {
		this.dataStructure = dataStructure;

		children.set(new HashSet<VisElem>());
		pane.setId(ID);
		AnchorPane.setLeftAnchor(wrappingPane, 0.0);
		AnchorPane.setRightAnchor(wrappingPane, 0.0);
		AnchorPane.setTopAnchor(wrappingPane, 0.0);
		AnchorPane.setBottomAnchor(wrappingPane, 0.0);
		AnchorPane.setLeftAnchor(pane, 0.0);
		AnchorPane.setRightAnchor(pane, 0.0);
		AnchorPane.setTopAnchor(pane, 0.0);
		AnchorPane.setBottomAnchor(pane, 0.0);
		pane.setAlignment(Pos.TOP_CENTER);
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
					pane.getTransforms().add(
							0,
							new Translate(mouseEvent.getSceneX() - mouseX, mouseEvent.getSceneY()
									- mouseY));
					mouseX = mouseEvent.getSceneX();
					mouseY = mouseEvent.getSceneY();
				}
			}
		});

		wrappingPane.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double scaleFactor = 1.1;
				if (event.getDeltaY() < 0) {
					scaleFactor = 1 / scaleFactor;
				}

				//double pivotX = wrappingPane.getWidth() / 2;
				//double pivotY = wrappingPane.getHeight() / 2;
				//double dx = (scaleFactor - 1) * (pivotX - event.getX());
				//double dy = (scaleFactor - 1) * (pivotY - event.getY());
				//pane.setTranslateX(pane.getTranslateX() + dx);
				//pane.setTranslateY(pane.getTranslateY() + dy);
				//pane.setScaleX(pane.getScaleX() * scaleFactor);
				//pane.setScaleY(pane.getScaleY() * scaleFactor);

				// TODO memory (and time?) inefficient
				pane.getTransforms().add(0, new Translate(-event.getX(), -event.getY()));
				pane.getTransforms().add(0, new Scale(scaleFactor, scaleFactor));
				pane.getTransforms().add(0, new Translate(event.getX(), event.getY()));
			}
		});

		FlowPane.setMargin(dataStructure.getVisual(), new Insets(
				75 + algvis2.scene.viselem.Node.RADIUS * 2.5, 0, 0, 0));
		add(dataStructure);
	}

	public Pane getPane() {
		return pane;
	}

	public Pane getWrappingPane() {
		return wrappingPane;
	}

	public void setDsElements(List<VisElem> elements) {
		dsElements = elements;
	}

	public void refresh() {
		List<Node> paneChildren = pane.getChildren();
		dataStructure.getVisual().getChildren().clear();
		for (VisElem elem : dsElements) {
			//			if (elem instanceof GroupOfBSTNodes) {
			//				// TODO namiesto tohto radsej moze byt hashmapa, v ktorej sa budu aktualizovat prvky hned pocas 
			//				// algoritmu...?
			//				ArrayList<Node> toAdd = new ArrayList<>();
			//				for (BSTNode node : ((GroupOfBSTNodes) elem).getNodes()) {
			//					if (!((GroupOfBSTNodes) elem).getVisual().getChildren().contains(node.getVisual())) {
			//						toAdd.add(node.getVisual());
			//						node.getVisual().setManaged(true);// TODO pozor na setManage
			//					}
			//				}
			//				ArrayList<Node> toRemove = new ArrayList<>();
			//				for (Node node : ((GroupOfBSTNodes) elem).getVisual().getChildren()) {
			//					boolean found = false;
			//					for (BSTNode node2 : ((GroupOfBSTNodes) elem).getNodes()) {
			//						if (node2.getVisual().equals(node)) {
			//							found = true;
			//							break;
			//						}
			//					}
			//					if (!found) {
			//						toRemove.add(node);
			//					}
			//				}
			//				((GroupOfBSTNodes) elem).getVisual().getChildren().removeAll(toRemove);
			//				((GroupOfBSTNodes) elem).getVisual().getChildren().addAll(toAdd);
			//			}
			dataStructure.getVisual().getChildren().add(elem.getVisual());
			elem.getVisual().setManaged(true);
		}

		ArrayList<VisElem> allChildren = new ArrayList<>(children.get());
		allChildren.addAll(notStorableChildren);
		Collections.sort(allChildren, VisElem.ZDEPTH_COMPARATOR);

		paneChildren.clear();
		for (VisElem elem : allChildren) {
			if (!doNotShow.contains(elem.getZDepth())) {
				elem.allowRefresh(true);
				paneChildren.add(elem.getVisual());
				elem.getVisual().setManaged(false);
			}
		}
		for (VisElem elem : dsElements) {
			elem.allowRefresh(true);
		}
		dataStructure.getVisual().setManaged(true);
		if (dataStructure instanceof FN_PBST) {
			dataStructure.getVisual().setMouseTransparent(true);
		}

		recalcAbsPosition();
	}

	public void disableVisualsRefresh() {
		for (VisElem elem : children.get()) {
			if (!doNotShow.contains(elem.getZDepth())) {
				elem.allowRefresh(false);
			}
		}
		for (VisElem elem : dsElements) {
			elem.allowRefresh(false);
		}
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
		clearLayer(zDepth, notStorableChildren);
		clearLayer(zDepth, children.get());
	}

	private void clearLayer(ZDepth zDepth, Set<VisElem> elems) {
		ArrayList<VisElem> toRemove = new ArrayList<>();
		for (VisElem elem : elems) {
			if (elem.getZDepth() == zDepth) {
				toRemove.add(elem);
			}
		}
		elems.removeAll(toRemove);
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

	public void addNotStorableVisElem(VisElem elem) {
		notStorableChildren.add(elem);
	}

	public void addNotStorableVisElemAll(Collection<? extends VisElem> c) {
		notStorableChildren.addAll(c);
	}

	public void clearPane() {
		pane.getChildren().clear();
	}

	public void clearPaneTransforms() {
		pane.getTransforms().clear();
	}
}
