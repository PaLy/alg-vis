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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	
	public void setTransition(TranslateTransition tt) {
		if (currentTransition != null) {
			if (currentTransition.getStatus().equals(Animation.Status.RUNNING)) currentTransition.pause();
			
			if (axis.equals(Axis.X)) tt.setFromX(node.getTranslateX());
			else if (axis.equals(Axis.Y)) tt.setFromY(node.getTranslateY());
		}
		
		currentTransition = tt;
        currentTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hura: " + node + " " + node.getTranslateX() + " " + node.getTranslateY());
            }
        });
		tt.play();
		
	}

	/**
	 * TODO should this method be synchronized? (it seems not) 
	 * @param observableValue    
	 * @param oldValue
	 * @param newValue
	 */
	@Override
	public synchronized void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
		double d = (Double) newValue - (Double) oldValue;

		TranslateTransition tt = TranslateTransitionBuilder.create()
			.node(node)
			.duration(Duration.seconds(1))
			.build();
        
        synchronized (this) {
            if (axis.equals(Axis.X)) node.translateXProperty().setValue(node.getTranslateX() - d);
            else if (axis.equals(Axis.Y)) node.translateYProperty().setValue(node.getTranslateY() - d);

//            System.out.println(node + " " + oldValue + " " + newValue);
            if (currentTransition != null) {
                if (currentTransition.getStatus().equals(Animation.Status.RUNNING)) currentTransition.pause();
//                System.out.println(currentTransition.getToX() + " " + currentTransition.getToY());
                                
                if (axis.equals(Axis.X)) {
//                    tt.setFromX(currentTransition.getFromX() - d);
                    tt.setToX(currentTransition.getToX());
                } else if (axis.equals(Axis.Y)) {
//                    tt.setFromY(currentTransition.getFromY() - d);
                    tt.setToY(currentTransition.getToY());
                }
//                tt.jumpTo(currentTransition.getCurrentTime());
                currentTransition = null;
            } else {
                if (axis.equals(Axis.X)) {
//                    tt.setFromX(node.getTranslateX() - d);
                    tt.setToX(node.getTranslateX() + d);
                } else if (axis.equals(Axis.Y)) {
//                    tt.setFromX(node.getTranslateY() - d);
                    tt.setToY(node.getTranslateY() + d);
                }
            }

//            System.out.println(node.getTranslateX() + " B " + node.getTranslateY());
//            System.out.println("******");
            
            setTransition(tt);
        }
	}
}
