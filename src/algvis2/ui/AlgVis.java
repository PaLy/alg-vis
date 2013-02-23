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
import java.util.*;

public class AlgVis extends Application {
	private Stage stage;
	private Scene scene;
	private AlgVisFXMLController controller;
	private String language = "en";
	private final Map<Visualization.Type, Visualization> VISUALIZATIONS = new HashMap<>();
	private Visualization.Type currentVisualization = null;

	public static final AutoAnimsManager autoAnimsManager = new AutoAnimsManager();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setTitle("Gnarley Trees");
		scene = new Scene(createRoot());
		showVisualization(Visualization.Type.FP_STACK);
		stage.setScene(scene);

		//		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		//		stage.setX(primaryScreenBounds.getMinX());
		//		stage.setY(primaryScreenBounds.getMinY());
		//		stage.setWidth(primaryScreenBounds.getWidth());
		//		stage.setHeight(primaryScreenBounds.getHeight());

		stage.show();
	}

	public void showVisualization(Visualization.Type visType) {
		if (visType != currentVisualization) {
			controller.setVis(getVisualization(visType), language);
			currentVisualization = visType;
		}
	}

	public void selectLanguage(String lang) {
		if (!lang.equals(language)) {
			language = lang;
			scene.setRoot(createRoot());
			
			Visualization.Type curVis = currentVisualization;
			
			currentVisualization = null;
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

	private Visualization getVisualization(Visualization.Type x) {
		if (VISUALIZATIONS.get(x) == null) {
			switch (x) {
			case BST:
				VISUALIZATIONS.put(x, new BSTVisualization());
				break;
			case AVL:
				VISUALIZATIONS.put(x, new AVLVisualization());
				break;
			case RB:
				VISUALIZATIONS.put(x, new RBVisualization());
				break;
			case FP_PC_BST:
				VISUALIZATIONS.put(x, new PCBSTVisualization());
				break;
			case FP_STACK:
				VISUALIZATIONS.put(x, new StackVisualization());
				break;
			}
		}
		return VISUALIZATIONS.get(x);
	}
	
	public Stage getStage() {
		return stage;
	}
}
