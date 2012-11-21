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


import algvis2.animation.ParallelTranslateTransition;
import algvis2.core.PropertyStateEditable;
import algvis2.scene.Axis;
import algvis2.scene.layout.AbsPosition;
import algvis2.scene.layout.VisPane;
import algvis2.scene.paint.NodePaint;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.util.Duration;

import java.util.HashMap;

public class Node extends Group implements AbsPosition, PropertyStateEditable {
	/**
	 * absolute position (relative to visPane)
	 */
	public final DoubleProperty visPaneX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneY = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateY = new SimpleDoubleProperty();
	public final ParallelTranslateTransition translateXtransition = new ParallelTranslateTransition(this, Axis.X);
	public final ParallelTranslateTransition translateYtransition = new ParallelTranslateTransition(this, Axis.Y);
	
	public static final int RADIUS = 20;
	public final ObjectProperty<NodePaint> paintProperty = new SimpleObjectProperty<NodePaint>(NodePaint.NORMAL);
	public final ObjectProperty<Integer> keyProperty;
	
	public Node(int key) {
		super();
		this.keyProperty = new SimpleObjectProperty<Integer>(key);
		
		Circle circle = new Circle(RADIUS);
		circle.setStroke(Color.BLACK);
		circle.fillProperty().bind(paintProperty.get().background);
		Text text = TextBuilder.create()
						.text(Integer.toString(key))
						.font(new Font(RADIUS - 3))
						.textOrigin(VPos.CENTER)
						.build();
		text.fillProperty().bind(paintProperty.get().text);
		text.setX(text.getX() - text.getBoundsInLocal().getWidth() / 2);
		getChildren().addAll(circle, text);
		
		visPaneX.addListener(translateXtransition);
		visPaneY.addListener(translateYtransition);
	}
	
	public Animation goAbove(Node node) {
		TranslateTransition ttx = TranslateTransitionBuilder.create()
			.node(this)
			.toX(node.getLayoutX())
			.duration(Duration.seconds(1))
			.build();
		TranslateTransition tty = TranslateTransitionBuilder.create()
			.node(this)
			.toY(node.getLayoutY() - 1.5 * Node.RADIUS)
			.duration(Duration.seconds(1))
			.build();
		return new ParallelTransition(ttx, tty);
	}

	public Shape getShape() {
		return (Shape) getChildren().get(0);
	}

	@Override
	public void recalcAbsPosition() {
		DoubleBinding vpX = null;
		DoubleBinding vpY = null;
		DoubleBinding vptX = null;
		DoubleBinding vptY = null;
		Parent parent = getParent();
		while (true) {
			if (parent == null) return;
			if (parent.getId() != null && parent.getId().equals(VisPane.ID)) break;
			
			if (vpX == null) vpX = layoutXProperty().add(parent.layoutXProperty());
			else vpX = vpX.add(parent.layoutXProperty());
			if (vpY == null) vpY = layoutYProperty().add(parent.layoutYProperty());
			else vpY = vpY.add(parent.layoutYProperty());
			if (vptX == null) vptX = translateXProperty().add(parent.translateXProperty());
			else vptX = vptX.add(parent.translateXProperty());
			if (vptY == null) vptY = translateXProperty().add(parent.translateXProperty());
			else vptY = vptY.add(parent.translateXProperty());
			
			parent = parent.getParent();
		}
		visPaneX.bind(vpX);
		visPaneY.bind(vpY);
        vptX.add(translateXProperty());
        vptY.add(translateYProperty());
		visPaneTranslateX.bind(vptX);
		visPaneTranslateY.bind(vptY);
	}
	
	public int getKey() {
		return keyProperty.getValue();
	}

	public void setPaint(NodePaint paint) {
		paintProperty.setValue(paint);
	}

    @Override
    public void storeState(HashMap<Object, Object> state) {
        state.put(paintProperty, paintProperty.get());
        state.put(keyProperty, keyProperty.get());
    }
}
