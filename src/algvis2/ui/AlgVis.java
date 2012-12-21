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

import algvis2.animation.AutoAnimsManager;
import algvis2.core.Visualization;
import algvis2.ds.dictionary.avl.AVLVisualization;
import algvis2.ds.dictionary.bst.BSTVisualization;
import algvis2.ds.dictionary.rb.RBVisualization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AlgVis extends Application {
	public static AlgVis that;
	private static Scene scene;
	private static AlgVisFXMLController controller;
	private static String language = "en";
	private static final int NUMBER_OF_VISUALIZATIONS = 7;
	private static final Visualization[] VISUALIZATIONS = new Visualization[NUMBER_OF_VISUALIZATIONS];
	private static int currentVisualization = -1;

	public static final AutoAnimsManager autoAnimsManager = new AutoAnimsManager();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Gnarley Trees");
		scene = new Scene(createRoot());
		showVisualization(6);
		stage.setScene(scene);
		//		stage.setFullScreen(true);

		//		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		//		stage.setX(primaryScreenBounds.getMinX());
		//		stage.setY(primaryScreenBounds.getMinY());
		//		stage.setWidth(primaryScreenBounds.getWidth());
		//		stage.setHeight(primaryScreenBounds.getHeight());

		stage.show();

		that = this;
	}

	public void showVisualization(int x) {
		if (x != currentVisualization) {
			if (VISUALIZATIONS[x] == null)
				VISUALIZATIONS[x] = createVisualization(x);

			AnchorPane root = (AnchorPane) scene.getRoot();
			root.getChildren().set(0, VISUALIZATIONS[x].getVisPane());

			TitledPane buttons = (TitledPane) scene
					.lookup("#buttonsTitledPane");
			buttons.setContent(VISUALIZATIONS[x].getButtonsPane(language));

			currentVisualization = x;
		}
	}

	public void selectLanguage(String lang) {
		language = lang;
		scene.setRoot(createRoot());
		int curVis = currentVisualization;
		currentVisualization = -1;
		showVisualization(curVis);
	}

	private Parent createRoot() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(ResourceBundle.getBundle("Messages",
				new Locale(language)));
		Parent parent = null;
		try {
			parent = (Parent) fxmlLoader.load(getClass().getResource(
					"AlgVis.fxml").openStream());
			controller = fxmlLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parent;
	}

	private Visualization createVisualization(int x) {
		switch (x) {
		case 0:
			return new BSTVisualization();
		case 2:
			return new AVLVisualization();
		case 6:
			return new RBVisualization();
		default:
			return null;
		}
	}

	public static Visualization getCurrentVis() {
		return VISUALIZATIONS[currentVisualization];
	}
}
