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

import algvis2.core.AlgVis;
import algvis2.core.Algorithm;
import algvis2.ds.dictionary.Dictionary;
import algvis2.scene.control.InputField;
import algvis2.scene.layout.Layout;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.StageBuilder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class AlgVisFXMLController implements Initializable {
	@FXML
	private MenuItem menu_bst;
	@FXML
	private MenuItem menu_avl;
	@FXML
	private MenuItem menu_rb;
	@FXML
	private MenuItem menu_pcbst;
	@FXML
	private MenuItem menu_pstack;
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
			button_layout.setValue(Layout.COMPACT_LAYOUT);
//			button_layout.valueProperty().addListener(
//					new ChangeListener<String>() {
//						@Override
//						public void changed(
//								ObservableValue<? extends String> observableValue,
//								String s, String s1) {
//							AlgVis.getCurrentVis().getDataStructure()
//									.setLayout(s1);
//						}
//					});
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
			AlgVis.that.showVisualization(1);
		} else if (source.equals(menu_rb)) {
			AlgVis.that.showVisualization(2);
		} else if (source.equals(menu_pcbst)) {
			AlgVis.that.showVisualization(3);
		} else if (source.equals(menu_pstack)) {
			AlgVis.that.showVisualization(4);
		}
	}

	@FXML
	protected void centerPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getVisPane().setTranslatePos(0, 0);
	}
	
	@FXML
	protected void snapshotPressed(ActionEvent event) {		
		Node node = AlgVis.getCurrentVis().getVisPane().getPane();
		WritableImage image = node.snapshot(new SnapshotParameters(), null);
		
		// TODO: probably use a file chooser here
		File file = new File("snapshot.png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		openStage(image);
	}
	
	private void openStage(Image image){
		StackPane sp = new StackPane();
		sp.getChildren().add(new ImageView(image));

		StageBuilder.create()
				.title("Snapshot - snapshot.png")
				.scene(new Scene(sp))
				.build()
				.show();
	}

	@FXML
	protected void insertPressed(ActionEvent event) {
		Vector<Integer> inputs = new InputField(inputField).getNonEmptyVI();
		if (inputs.size() > 1) {
			handleMultiOperation("insert", inputs);
		} else {
			handleSingleOperation("insert", inputs.get(0));
		}
	}

	@FXML
	protected void deletePressed(ActionEvent event) {
		Vector<Integer> inputs = new InputField(inputField).getNonEmptyVI();
		if (inputs.size() > 1) {
			handleMultiOperation("delete", inputs);
		} else {
			handleSingleOperation("delete", inputs.get(0));
		}
	}

	@FXML
	protected void findPressed(ActionEvent event) {
		Vector<Integer> inputs = new InputField(inputField).getNonEmptyVI();
		if (inputs.size() == 1) {
			handleSingleOperation("find", inputs.get(0));
		}
	}

	@FXML
	protected void randomPressed(ActionEvent event) {
		handleMultiOperation("random", null);
	}
	
	private void handleMultiOperation(String operation, Vector<Integer> inputs) {
		SequentialTransition st = new SequentialTransition();
		if (operation.equals("insert")) {
			for (int x : inputs) {
				st.getChildren().add(AlgVis.getCurrentVis().getDataStructure().insert(x).getBackToStart());
			}
		} else if (operation.equals("delete")) {
			for (int x : inputs) {
				Algorithm delete = AlgVis.getCurrentVis().getDataStructure().delete(x);
				if (delete != null) {
					st.getChildren().add(delete.getBackToStart());
				} else {
					// TODO napr. vypisat, ze zasobnik je prazdny
				}
			}
		} else if (operation.equals("random")) {
			Animation[] animations = AlgVis.getCurrentVis().getDataStructure()
					.random(new InputField(inputField).getInt(10));
			for (Animation animation : animations) {
				st.getChildren().add(animation);
			}
		}
		
		List<Animation> list = new ArrayList<Animation>();
		st.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AlgVis.getCurrentVis().reLayout();
			}
		});
		list.add(new SequentialTransition(st));
		st.jumpTo("end");
		AlgVis.getCurrentVis().animManager.add(list, true);
		AlgVis.getCurrentVis().getButtons().disablePrevious(false);
		AlgVis.getCurrentVis().visPane.refresh(); // TODO co?? ...jaaaj to kvoli tomu blbemu pcBST; casom to pojde prec
		AlgVis.getCurrentVis().reLayout();
	}
	
	private void handleSingleOperation(String operation, Integer input) {
		AlgVis.getCurrentVis().getButtons().setDisabled(true);
		AlgVis.getCurrentVis().getButtons().disableNext(true);
		AlgVis.getCurrentVis().getButtons().disablePrevious(true);

		SequentialTransition back = new SequentialTransition();

		Algorithm algorithm = null;
		if (operation.equals("delete")) {
			algorithm = AlgVis.getCurrentVis().getDataStructure().delete(input);
		} else if (operation.equals("insert")) {
			algorithm = AlgVis.getCurrentVis().getDataStructure().insert(input);
		} else if (operation.equals("find")) {
			algorithm = ((Dictionary) AlgVis.getCurrentVis().getDataStructure()).find(input);
		}
		
		if (algorithm == null) { // TODO
			AlgVis.getCurrentVis().getButtons().setDisabled(false);
			AlgVis.getCurrentVis().getButtons().disableNext(!AlgVis.getCurrentVis().animManager.hasNext());
			AlgVis.getCurrentVis().getButtons().disablePrevious(!AlgVis.getCurrentVis().animManager.hasPrevious());
			return;
		}

		AlgVis.getCurrentVis().animManager.add(algorithm.allSteps, false);
		back.getChildren().add(algorithm.getBackToStart());

		// System.out.println("BEFORE BACK");
		back.setRate(-back.getRate());
		back.jumpTo("end");
		back.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// System.out.println("AFTER BACK");
				AlgVis.getCurrentVis().reLayout(); // kvoli tomu, ze sa to moze zle bindnut, 
				// a potom sa posunie vrchol
				AlgVis.getCurrentVis().animManager.playNext();
			}
		});
		back.play();
	}

	@FXML
	protected void clearPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getDataStructure().clear();
		AlgVis.getCurrentVis().visPane.clearPane();
		AlgVis.getCurrentVis().reLayout();
		AlgVis.getCurrentVis().animManager.clear();
		AlgVis.getCurrentVis().getButtons().disablePrevious(true);
		AlgVis.getCurrentVis().getButtons().disableNext(true);
		AlgVis.getCurrentVis().getVisPane().setTranslatePos(0, 0);
	}

	@FXML
	protected void previousPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getButtons().setDisabled(true);
		AlgVis.getCurrentVis().getButtons().disablePrevious(true);
		AlgVis.getCurrentVis().getButtons().disableNext(true);
		AlgVis.getCurrentVis().animManager.playPrevious();
	}

	@FXML
	protected void nextPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getButtons().setDisabled(true);
		AlgVis.getCurrentVis().getButtons().disableNext(true);
		AlgVis.getCurrentVis().getButtons().disablePrevious(true);
		AlgVis.getCurrentVis().animManager.playNext();
	}
}
