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


import algvis2.scene.layout.AbsPosition;
import algvis2.scene.layout.VisPane;
import algvis2.scene.paint.NodePaint;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

public class Node extends Group implements AbsPosition {
	/**
	 * absolute position (relative to visPane)
	 */
	public final DoubleProperty visPaneX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneY = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateX = new SimpleDoubleProperty();
	public final DoubleProperty visPaneTranslateY = new SimpleDoubleProperty();
	
	public static final int RADIUS = 20;
	private NodePaint paint = NodePaint.NORMAL;
	public int key;
	
	public Node(int key) {
		super();
		this.key = key;
		
		Circle circle = new Circle(RADIUS, paint.circlePaint);
		circle.setStroke(Color.BLACK);
		
		Text text = TextBuilder.create()
						.text(Integer.toString(key))
						.font(new Font(RADIUS - 3))
						.fill(paint.textPaint)
						.textOrigin(VPos.CENTER)
						.build();
		text.setX(text.getX() - text.getBoundsInLocal().getWidth() / 2);
		
		getChildren().addAll(circle, text);
		
		parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> observableValue, Parent oldParent, 
			                    Parent newParent) {
				if (newParent != null) recalcAbsPosition();
			}
		});
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
		visPaneTranslateX.bind(vptX);
		visPaneTranslateY.bind(vptY);
	}
}
