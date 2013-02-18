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

package algvis2.ui;

import algvis2.core.Algorithm;
import algvis2.ds.dictionary.Dictionary;
import algvis2.scene.control.InputField;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import java.util.Vector;

public class DictButtonsController extends ButtonsController {
	public TextField findField;
	public TextField deleteField;
	
	public void insertPressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		
		Vector<Integer> input = new InputField(insertField).getNonEmptyVI();

		Algorithm algorithm = ((Dictionary) visualization.getDataStructure()).insert(input.get(0));
		visualization.animManager.add(algorithm.allSteps, false);

		SequentialTransition back = SequentialTransitionBuilder.create()
				.children(algorithm.startEndTransition())
				.rate(-1)
				.onFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						visualization.reLayout(); // kvoli tomu, ze sa to moze zle bindnut,
						visualization.animManager.playNext();
					}
				}).build();
		back.jumpTo("end");
		back.play();
	}

	public void deletePressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		
		Vector<Integer> input = new InputField(deleteField).getNonEmptyVI();

		Algorithm algorithm = ((Dictionary) visualization.getDataStructure()).delete(input.get(0));
		visualization.animManager.add(algorithm.allSteps, false);

		SequentialTransition back = SequentialTransitionBuilder.create()
				.children(algorithm.startEndTransition())
				.rate(-1)
				.onFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						visualization.reLayout(); // kvoli tomu, ze sa to moze zle bindnut,
						visualization.animManager.playNext();
					}
				}).build();
		back.jumpTo("end");
		back.play();
	}

	public void findPressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
				
		Vector<Integer> input = new InputField(findField).getNonEmptyVI();

		Algorithm algorithm = ((Dictionary) visualization.getDataStructure()).find(input.get(0));
		visualization.animManager.add(algorithm.allSteps, false);

		SequentialTransition back = SequentialTransitionBuilder.create()
				.children(algorithm.startEndTransition())
				.rate(-1)
				.onFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						visualization.reLayout(); // kvoli tomu, ze sa to moze zle bindnut,
						visualization.animManager.playNext();
					}
				}).build();
		back.jumpTo("end");
		back.play();

	}
}
