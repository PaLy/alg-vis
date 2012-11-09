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

package algvis2.animation;

import algvis2.scene.Axis;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.util.Duration;

public class ParallelTranslateTransition implements ChangeListener<Number> {
	private final Node node;
	private final Axis axis;
	private TranslateTransition currentTransition;

	public ParallelTranslateTransition(Node node, Axis axis) {
		this.node = node;
		this.axis = axis;
	}

	public ParallelTranslateTransition(Node node, Axis axis, TranslateTransition currentTransition) {
		this(node, axis);
		this.currentTransition = currentTransition;
		currentTransition.play();
	}

	/**
	 * TODO should this method be synchronized? (it seems not) 
	 * @param observableValue    
	 * @param oldValue
	 * @param newValue
	 */
	@Override
	public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
		double d = (Double) newValue - (Double) oldValue;

		TranslateTransition tt = TranslateTransitionBuilder.create()
			.node(node)
			.duration(Duration.seconds(1))
			.build();
		if (axis.equals(Axis.X)) node.translateXProperty().setValue(node.translateXProperty().getValue() - d);
		else if (axis.equals(Axis.Y)) node.translateYProperty().setValue(node.translateYProperty().getValue() - d);
		
		if (currentTransition != null) {
			if (currentTransition.getStatus().equals(Animation.Status.RUNNING)) currentTransition.pause();
			
			if (axis.equals(Axis.X)) tt.setToX(currentTransition.getToX());
			else if (axis.equals(Axis.Y)) tt.setToY(currentTransition.getToY());
		} else {
			if (axis.equals(Axis.X)) tt.setToX(node.translateXProperty().getValue() + d);
			else if (axis.equals(Axis.Y)) tt.setToY(node.translateYProperty().getValue() + d);
		}
		
		currentTransition = tt;
		tt.play();
	}
}
