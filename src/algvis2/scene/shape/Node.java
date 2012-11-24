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

package algvis2.scene.shape;

import algvis2.animation.AutoFillTransition;
import algvis2.animation.AutoTranslateTransition;
import algvis2.core.PropertyStateEditable;
import algvis2.scene.Axis;
import algvis2.scene.effect.Effects;
import algvis2.scene.layout.AbsPosition;
import algvis2.scene.layout.VisPane;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.text.Fonts;
import javafx.beans.binding.DoubleBinding;
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
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import java.util.HashMap;

public class Node extends Group implements AbsPosition, PropertyStateEditable {
	/**
	 * absolute position (relative to visPane)
	 */
	public final DoubleProperty visPaneX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneY = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateY = new SimpleDoubleProperty();

	public static final int RADIUS = 20;
	private final NodePaint paint = new NodePaint(NodePaint.NORMAL);
	public final ObjectProperty<Integer> keyProperty;

	// TODO node can be bound only if it is not managed?
	private ObjectProperty<DoubleBinding> layoutXBindingProperty = new SimpleObjectProperty<DoubleBinding>();
	private ObjectProperty<DoubleBinding> layoutYBindingProperty = new SimpleObjectProperty<DoubleBinding>();

	public Node(int key) {
		super();
		this.keyProperty = new SimpleObjectProperty<Integer>(key);
		init();
	}

	public Node(int key, NodePaint p) {
		super();
		this.keyProperty = new SimpleObjectProperty<Integer>(key);
		setPaint(p);
		init();
	}

	public Node(Node v) {
		super();
		this.keyProperty = new SimpleObjectProperty<Integer>(v.getKey());
		visPaneX.set(v.visPaneX.get() + v.visPaneTranslateX.get());
		visPaneY.set(v.visPaneY.get() + v.visPaneTranslateY.get());
		init();
	}

	private void init() {
		final Circle circle = CircleBuilder.create().radius(RADIUS)
				.fill(paint.background.get()).stroke(Color.BLACK)
				.effect(Effects.NODE_SHADOW).build();

		Text text = TextBuilder.create().text(Integer.toString(getKey()))
				.font(Fonts.NODE_FONT).textOrigin(VPos.CENTER)
				.fill(paint.text.get()).build();
		text.setX(text.getX() - text.getBoundsInLocal().getWidth() / 2);

		getChildren().addAll(circle, text);

		visPaneX.addListener(new AutoTranslateTransition(this, Axis.X));
		visPaneY.addListener(new AutoTranslateTransition(this, Axis.Y));
		paint.background.addListener(new AutoFillTransition(
				(Shape) getChildren().get(0)));
		paint.text.addListener(new AutoFillTransition((Shape) getChildren()
				.get(1)));

		layoutXBindingProperty.addListener(new ChangeListener<DoubleBinding>() {
			@Override
			public void changed(
					ObservableValue<? extends DoubleBinding> observableValue,
					DoubleBinding oldBinding, DoubleBinding newBinding) {
				//                if (newBinding == null) 
				//                    System.out.println(getKey() + " X " + null);
				//                else
				//                    System.out.println(getKey() + " X " + newBinding.get());

				if (newBinding != null)
					layoutXProperty().bind(newBinding);
				else
					layoutXProperty().unbind();
				recalcAbsPosition();
			}
		});
		layoutYBindingProperty.addListener(new ChangeListener<DoubleBinding>() {
			@Override
			public void changed(
					ObservableValue<? extends DoubleBinding> observableValue,
					DoubleBinding oldBinding, DoubleBinding newBinding) {
				//                if (newBinding == null)
				//                    System.out.println(getKey() + " Y " + null);
				//                else
				//                    System.out.println(getKey() + " Y " + newBinding.get());

				if (newBinding != null)
					layoutYProperty().bind(newBinding);
				else
					layoutYProperty().unbind();
				recalcAbsPosition();
			}
		});

		layoutXBindingProperty.set(new SimpleDoubleProperty(0).add(0));
		layoutYBindingProperty.set(new SimpleDoubleProperty(0).add(0));

		parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(
					ObservableValue<? extends Parent> observableValue,
					Parent parent, Parent parent1) {
				recalcAbsPosition();
			}
		});

		recalcAbsPosition();
	}

	public void goAbove(Node node) {
		layoutXBindingProperty.set(node.visPaneX.add(node.visPaneTranslateX));
		layoutYBindingProperty.set(node.visPaneY.add(node.visPaneTranslateY)
				.subtract(Node.RADIUS * 2.4));
	}

	public void goTo(Node node) {
		layoutXBindingProperty.set(node.visPaneX.add(node.visPaneTranslateX));
		layoutYBindingProperty.set(node.visPaneY.add(node.visPaneTranslateY));
	}

	public void removeLayoutXYBindings() {
		layoutXBindingProperty.set(null);
		layoutYBindingProperty.set(null);
	}

	public Shape getShape() {
		return (Shape) getChildren().get(0);
	}

	@Override
	public void recalcAbsPosition() {
		DoubleBinding vpX = layoutXProperty().add(0);
		DoubleBinding vpY = layoutYProperty().add(0);
		DoubleBinding vptX = translateXProperty().add(0);
		DoubleBinding vptY = translateYProperty().add(0);
		Parent parent = getParent();
		while (true) {
			if (parent == null)
				break;
			if (parent.getId() != null && parent.getId().equals(VisPane.ID))
				break;

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

	public void setPaint(NodePaint p) {
		paint.background.set(p.background.get());
		paint.text.set(p.text.get());
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(paint.background, paint.background.get());
		state.put(paint.text, paint.text.get());
		state.put(keyProperty, keyProperty.get());
		state.put(layoutXBindingProperty, layoutXBindingProperty.get());
		state.put(layoutYBindingProperty, layoutYBindingProperty.get());
	}
}
