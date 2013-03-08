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

package algvis2.core;

import algvis2.scene.layout.VisPane;
import algvis2.ui.ButtonsController;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public abstract class Visualization implements PropertyStateEditable {
	public final VisPane visPane;

	private ButtonsController buttonsController;
	private final URL buttonsFile;
	private ButtonsState buttonsState;

	private final DataStructure dataStructure;
	public final AnimationManager animManager = new AnimationManager(this);

	public Visualization(URL buttonsFile) {
		this.buttonsFile = buttonsFile;
		this.dataStructure = initDS();
		visPane = new VisPane(this.dataStructure);
	}
	
	abstract protected DataStructure initDS();

	public Pane getVisPaneWrapper() {
		return visPane.getWrappingPane();
	}

	public VisPane getVisPane() {
		return visPane;
	}

	public Pane loadButtons(String lang) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(ResourceBundle.getBundle("Messages", new Locale(lang)));
		Pane parent = null;
		try {
			parent = (Pane) fxmlLoader.load(buttonsFile.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		buttonsController = fxmlLoader.getController();
		buttonsController.setVis(this);
		if (buttonsState == null) {
			buttonsState = new ButtonsState();
		}
		buttonsState.updateButtons();

		return parent;
	}

	public ButtonsController getButtonsController() {
		return buttonsController;
	}

	public DataStructure getDataStructure() {
		return dataStructure;
	}

	public void random(int x) {
		Animation animation = dataStructure.random(this, x);
		animation.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				reLayout();
			}
		});
		animation.jumpTo("end");

		List<Animation> wrapper = new ArrayList<>();
		wrapper.add(new SequentialTransition(animation));
		animManager.add(wrapper, true);

		visPane.refresh();
		reLayout();
	}

	public void clear() {
		dataStructure.clear();
		visPane.clearPane();
		reLayout();
		animManager.clear();
		visPane.setTranslatePos(0, 0);
	}

	public abstract void reLayout();

	@Override
	public void storeState(HashMap<Object, Object> state) {
		visPane.storeState(state);
	}

	public abstract String getTitle();

	protected final void addAndPlay(Algorithm algorithm) {
		animManager.add(algorithm.allSteps, false);

		SequentialTransition back = SequentialTransitionBuilder.create()
				.children(algorithm.startEndTransition()).rate(-1)
				.onFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						animManager.playNext();
					}
				}).build();
		back.jumpTo("end");
		back.play();
	}

	private final class ButtonsState {
		private BooleanProperty disableOperations = new SimpleBooleanProperty();
		private BooleanProperty disablePrevious = new SimpleBooleanProperty();
		private BooleanProperty disableNext = new SimpleBooleanProperty();
		private BooleanProperty pauseSelected = new SimpleBooleanProperty();

		private ButtonsState() {
			bindButtons();
		}

		private void updateButtons() {
			buttonsController.disableOperations(disableOperations.getValue());
			buttonsController.disableNext(disableNext.getValue());
			buttonsController.disablePrevious(disablePrevious.getValue());
			buttonsController.setPausesSelected(pauseSelected.getValue());

			bindButtons();
		}

		private void bindButtons() {
			disableOperations.bind(buttonsController.operationsButtons.disableProperty());
			disablePrevious.bind(buttonsController.buttonPrevious.disableProperty());
			disableNext.bind(buttonsController.buttonNext.disableProperty());
			pauseSelected.bind(buttonsController.buttonPause.selectedProperty());
		}
	}

	public enum Type {
		BST, AVL, RB,

		FN_PBST, PC_PBST, PSTACK
	}
}
