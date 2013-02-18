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
import algvis2.ds.persistent.partially.bst.PCBSTVisualization;
import algvis2.ds.persistent.stack.StackVisualization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AlgVis extends Application {
	private Stage stage;
	private Scene scene;
	private AlgVisFXMLController controller;
	private String language = "en";
	private final int NUMBER_OF_VISUALIZATIONS = 5;
	private final Visualization[] VISUALIZATIONS = new Visualization[NUMBER_OF_VISUALIZATIONS];
	private int currentVisualization = -1;

	public static final AutoAnimsManager autoAnimsManager = new AutoAnimsManager();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setTitle("Gnarley Trees");
		scene = new Scene(createRoot());
		showVisualization(4);
		stage.setScene(scene);

		//		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		//		stage.setX(primaryScreenBounds.getMinX());
		//		stage.setY(primaryScreenBounds.getMinY());
		//		stage.setWidth(primaryScreenBounds.getWidth());
		//		stage.setHeight(primaryScreenBounds.getHeight());

		stage.show();
	}

	public void showVisualization(int x) {
		if (x != currentVisualization) {
			controller.setVis(getVisualization(x), language);
			currentVisualization = x;
		}
	}

	public void selectLanguage(String lang) {
		if (!lang.equals(language)) {
			language = lang;
			scene.setRoot(createRoot());
			
			int curVis = currentVisualization;
			
			currentVisualization = -1;
			showVisualization(curVis);
		}
	}

	private Parent createRoot() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(ResourceBundle.getBundle("Messages",
				new Locale(language)));
		fxmlLoader.setLocation(getClass().getResource(
				"/algvis2/ui/AlgVis.fxml"));
		Parent parent = null;
		try {
			parent = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller = fxmlLoader.getController();
		controller.setAlgvis(this);
		
		return parent;
		// possible static way:
		// parent = FXMLLoader.load(getClass().getResource(
		//		"AlgVis.fxml"), ResourceBundle.getBundle("Messages",
		//		new Locale(language)));
	}

	private Visualization getVisualization(int x) {
		if (VISUALIZATIONS[x] == null) {
			switch (x) {
			case 0:
				VISUALIZATIONS[x] = new BSTVisualization();
				break;
			case 1:
				VISUALIZATIONS[x] = new AVLVisualization();
				break;
			case 2:
				VISUALIZATIONS[x] = new RBVisualization();
				break;
			case 3:
				VISUALIZATIONS[x] = new PCBSTVisualization();
				break;
			case 4:
				VISUALIZATIONS[x] = new StackVisualization();
				break;
			default:
				VISUALIZATIONS[x] = null;
			}
		}
		return VISUALIZATIONS[x];
	}
	
	public Stage getStage() {
		return stage;
	}
}
