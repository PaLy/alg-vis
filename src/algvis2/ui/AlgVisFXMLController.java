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

import algvis2.ds.dictionary.Dictionary;
import algvis2.ds.dictionary.bst.BST;
import algvis2.scene.control.InputField;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AlgVisFXMLController implements Initializable {
	@FXML
	private MenuItem menu_bst;
	@FXML
	private MenuItem menu_avl;
	@FXML
	private MenuItem menu_rb;
	@FXML
	private TextField inputField;
	@FXML
	private ChoiceBox<String> button_layout;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// TODO preco sa tato funkcia na zaciatku zavola 3 krat?
		// pri 1. volani button_layout = false
		// pri 2. volani button_layout = true
		// pri 3. volani button_layout = false
		if (button_layout != null) {
			button_layout.setValue(BST.DEF_LAYOUT);
			button_layout.valueProperty().addListener(
					new ChangeListener<String>() {
						@Override
						public void changed(
								ObservableValue<? extends String> observableValue,
								String s, String s1) {
							AlgVis.getCurrentVis().getDataStructure()
									.setLayout(s1);
						}
					});
		}
	}

	@FXML
	protected void handleTitledPaneMouseEntered(MouseEvent event) {
		((TitledPane) event.getSource()).setExpanded(true);
	}

	@FXML
	protected void handleTitlePaneMouseExited(MouseEvent event) {
		((TitledPane) event.getSource()).setExpanded(false);
	}

	@FXML
	protected void selectLanguageEN(ActionEvent event) {
		AlgVis.that.selectLanguage("en");
	}

	@FXML
	protected void selectLanguageSK(ActionEvent event) {
		AlgVis.that.selectLanguage("sk");
	}

	@FXML
	protected void selectVisualization(ActionEvent event) {
		Object source = event.getSource();
		if (source.equals(menu_bst)) {
			AlgVis.that.showVisualization(0);
		} else if (source.equals(menu_avl)) {
			AlgVis.that.showVisualization(2);
		} else if (source.equals(menu_rb)) {
			AlgVis.that.showVisualization(6);
		}
	}

	@FXML
	protected void insertPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getButtons().setDisabled(true);
		final SequentialTransition animation = new SequentialTransition();
		SequentialTransition back = new SequentialTransition();
		for (int x : new InputField(inputField).getNonEmptyVI()) {
			Animation[] animations = AlgVis.getCurrentVis().getDataStructure()
					.insert(x);
			animation.getChildren().add(animations[0]);
			back.getChildren().add(animations[1]);
		}

		System.out.println("BEFORE BACK");
		back.setRate(-back.getRate());
		back.jumpTo("end");
		back.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("AFTER BACK");
				AlgVis.autoAnimsManager.endAll();
				SequentialTransitionBuilder.create().children(animation)
						.onFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								AlgVis.getCurrentVis().getButtons()
										.setDisabled(false);
							}
						}).build().play();
			}
		});
		back.play();
	}

	@FXML
	protected void findPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getButtons().setDisabled(true);
		final SequentialTransition animation = new SequentialTransition();
		SequentialTransition back = new SequentialTransition();
		for (int x : new InputField(inputField).getNonEmptyVI()) {
			Animation[] animations = ((Dictionary) AlgVis.getCurrentVis()
					.getDataStructure()).find(x);
			animation.getChildren().add(animations[0]);
			back.getChildren().add(animations[1]);
		}
		back.setRate(-back.getRate());
		back.jumpTo("end");
		back.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AlgVis.autoAnimsManager.endAll();
				SequentialTransitionBuilder.create().children(animation)
						.onFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								AlgVis.getCurrentVis().getButtons()
										.setDisabled(false);
							}
						}).build().play();
			}
		});
		back.play();
	}

	@FXML
	protected void randomPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getDataStructure()
				.random(new InputField(inputField).getInt(10));
		AlgVis.getCurrentVis().getDataStructure().reLayout();
	}

	@FXML
	protected void clearPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getDataStructure().clear();
		AlgVis.getCurrentVis().getDataStructure().reLayout();
	}
}
