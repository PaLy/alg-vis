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

import algvis2.scene.paint.NodePaint;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

public class Node extends Group {
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
	}
	
	public Shape getShape() {
		return (Shape) getChildren().get(0);
	}
}
