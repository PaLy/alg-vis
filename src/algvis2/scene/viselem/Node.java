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

import algvis2.animation.AutoNodePaintTransition;
import algvis2.animation.AutoTranslateTransition;
import algvis2.core.PropertyStateEditable;
import algvis2.scene.Axis;
import algvis2.scene.effect.Effects;
import algvis2.scene.layout.AbsPosition;
import algvis2.scene.layout.VisPane;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.text.Fonts;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import java.util.HashMap;

public class Node extends VisElem implements AbsPosition, PropertyStateEditable {
	/**
	 * absolute position (relative to visPane)
	 */
	public final DoubleProperty visPaneX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneY = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateY = new SimpleDoubleProperty();

	public static final int RADIUS = 20;
	public static final int INF = 99999;
	private final ObjectProperty<NodePaint> paintProperty = new SimpleObjectProperty<>(NodePaint.NORMAL);
	protected ObjectProperty<Integer> keyProperty;

	// TODO node can be bound only if it is not managed?
	/**
	 * These properties contain node binding to another node. If node is bound, then node's parent cannot
	 * change layoutX and layoutY of this node.
	 */
	private ObjectProperty<DoubleBinding> layoutXBindingProperty = new SimpleObjectProperty<>();
	private ObjectProperty<DoubleBinding> layoutYBindingProperty = new SimpleObjectProperty<>();

	public static final int NULL = 100000;
	private final AutoTranslateTransition autoTranslateXTransition = new AutoTranslateTransition(getVisual(), Axis.X);
	private final AutoTranslateTransition autoTranslateYTransition = new AutoTranslateTransition(getVisual(), Axis.Y);

	public Node(int key) {
		super(new Group());
		this.keyProperty = new SimpleObjectProperty<>(key);
		init();
	}

	public Node(int key, NodePaint p) {
		super(new Group());
		this.keyProperty = new SimpleObjectProperty<>(key);
		setPaint(p);
		init();
	}

	@Deprecated
	public Node(javafx.scene.Node node) {
		super(node);
	}

	private void init() {
		final Circle circle = CircleBuilder.create().radius(RADIUS)
				.fill(getPaint().getBackground()).stroke(Color.BLACK)
				.effect(Effects.NODE_SHADOW).build();

		String key;
		switch (getKey()) {
			case Node.INF:
				key = "\u221e";
				break;
			case -Node.INF:
				key = "-\u221e";
				break;
			default:
				key = Integer.toString(getKey());
		}
		Text text = TextBuilder.create().text(key)
				.font(Fonts.NODE_FONT).textOrigin(VPos.CENTER)
				.fill(getPaint().getText()).build();
		text.setX(text.getX() - text.getBoundsInLocal().getWidth() / 2);

		paintProperty.addListener(new AutoNodePaintTransition(circle, text));
		getVisual().getChildren().addAll(circle, text);

		layoutXBindingProperty.addListener(new ChangeListener<DoubleBinding>() {
			@Override
			public void changed(
					ObservableValue<? extends DoubleBinding> observableValue,
					DoubleBinding oldBinding, DoubleBinding newBinding) {
                if (newBinding != null) {
					getVisual().layoutXProperty().bind(new When(refreshAllowed).then(newBinding).otherwise(getVisual
							().getLayoutX()));
                } else {
                    getVisual().layoutXProperty().unbind();
                }
			}
		});
		layoutYBindingProperty.addListener(new ChangeListener<DoubleBinding>() {
			@Override
			public void changed(
					ObservableValue<? extends DoubleBinding> observableValue,
					DoubleBinding oldBinding, DoubleBinding newBinding) {
                if (newBinding != null) {
					getVisual().layoutYProperty().bind(new When(refreshAllowed).then(newBinding).otherwise(getVisual
							().getLayoutY()));
                } else {
                    getVisual().layoutYProperty().unbind();
                }
			}
		});
		layoutXBindingProperty.set(new SimpleDoubleProperty(0).add(0));
		layoutYBindingProperty.set(new SimpleDoubleProperty(0).add(0));

		bindVisPanePos();

		visPaneX.addListener(autoTranslateXTransition);
		visPaneY.addListener(autoTranslateYTransition);
	}

	@Override
	public Group getVisual() {
		return (Group) super.getVisual();
	}

	public void removeAutoTranslations() {
		visPaneX.removeListener(autoTranslateXTransition);
		visPaneY.removeListener(autoTranslateYTransition);
	}
	
	public void addAutoTranslations() {
		visPaneX.removeListener(autoTranslateXTransition);
		visPaneY.removeListener(autoTranslateYTransition);
		visPaneX.addListener(autoTranslateXTransition);
		visPaneY.addListener(autoTranslateYTransition);
	}

	public void setTopText(String s) {
		Text text = TextBuilder.create()
				.text(s)
				.font(Fonts.TOP_TEXT)
				.stroke(Color.WHITE)
				.strokeType(StrokeType.OUTSIDE)
				.strokeWidth(2)
				.build();
		text.setX(text.getX() - text.getBoundsInLocal().getWidth() / 2);
		text.setY(text.getY() - Node.RADIUS * 1.2);
		
		getVisual().getChildren().add(text);
	}

	public void goAbove(Node node) {
		layoutXBindingProperty.set(node.visPaneX.add(0));
		layoutYBindingProperty.set(node.visPaneY
				.subtract(node.getVisual().getBoundsInLocal().getHeight() / 2)
				.subtract(getVisual().getBoundsInLocal().getHeight() / 2));
	}

	public void goTo(Node node) {
		layoutXBindingProperty.set(node.visPaneX.add(0));
		layoutYBindingProperty.set(node.visPaneY.add(0));
	}
	
	public void goNextTo(Node node) {
		layoutXBindingProperty.set(node.visPaneX
				.add(node.getVisual().getBoundsInLocal().getWidth() / 2)
				.add(getVisual().getBoundsInLocal().getWidth() / 2));
		layoutYBindingProperty.set(node.visPaneY.add(0));
	}

	public void removePosBinding() {
		layoutXBindingProperty.set(null);
		layoutYBindingProperty.set(null);
	}

	@Override
	public void recalcAbsPosition() {
		bindVisPanePos();
	}

	private void bindVisPanePos() {
		DoubleBinding vpX = getVisual().layoutXProperty().add(0);
		DoubleBinding vpY = getVisual().layoutYProperty().add(0);
		DoubleBinding vptX = getVisual().translateXProperty().add(0);
		DoubleBinding vptY = getVisual().translateYProperty().add(0);
		Parent parent = getVisual().getParent();
		while (true) {
			if (parent == null) {
				break;
			}
			if (parent.getId() != null && parent.getId().equals(VisPane.ID)) {
				break;
			}

			vpX = vpX.add(parent.layoutXProperty());
			vpY = vpY.add(parent.layoutYProperty());
			vptX = vptX.add(parent.translateXProperty());
			vptY = vptY.add(parent.translateYProperty());

			parent = parent.getParent();
		}
		visPaneX.bind(vpX);
		visPaneY.bind(vpY);
		visPaneTranslateX.bind(vptX);
		visPaneTranslateY.bind(vptY);
	}

	public int getKey() {
		return keyProperty.getValue();
	}
	
	public NodePaint getPaint() {
		return paintProperty.get();
	}

	public void setPaint(NodePaint paint) {
		paintProperty.set(paint);
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(paintProperty, paintProperty.get());
		if (keyProperty != null) state.put(keyProperty, keyProperty.get());
		state.put(layoutXBindingProperty, layoutXBindingProperty.get());
		state.put(layoutYBindingProperty, layoutYBindingProperty.get());
	}
}
