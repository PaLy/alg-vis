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

import algvis2.core.Visualization;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.StageBuilder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlgVisFXMLController implements Initializable {
	public AnchorPane rootPane;
	public TitledPane buttonsTitledPane;
	public Text visTitle;
	public MenuItem menu_bst;
	public MenuItem menu_avl;
	public MenuItem menu_rb;
	public MenuItem menu_pcbst;
	public MenuItem menu_pstack;
	public Button fullscreenButton;
	
	private Visualization visualization;
	private AlgVis algvis;
	private ChangeListener<Boolean> fullScreenChangeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (newValue) {
				fullscreenButton.setGraphic(new ImageView("algvis2/ui/fullscreen_off.png"));
			} else {
				fullscreenButton.setGraphic(new ImageView("algvis2/ui/fullscreen_on.png"));
			}
		}
	};

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	public void handleTitledPaneMouseEntered(MouseEvent event) {
		((TitledPane) event.getSource()).setExpanded(true);
	}
	
	public void handleTitlePaneMouseExited(MouseEvent event) {
		((TitledPane) event.getSource()).setExpanded(false);
	}

	public void selectLanguageEN(ActionEvent event) {
		algvis.selectLanguage("en");
	}
	
	public void selectLanguageSK(ActionEvent event) {
		algvis.selectLanguage("sk");
	}

	public void selectVisualization(ActionEvent event) {
		Object source = event.getSource();
		if (source.equals(menu_bst)) {
			algvis.showVisualization(0);
		} else if (source.equals(menu_avl)) {
			algvis.showVisualization(1);
		} else if (source.equals(menu_rb)) {
			algvis.showVisualization(2);
		} else if (source.equals(menu_pcbst)) {
			algvis.showVisualization(3);
		} else if (source.equals(menu_pstack)) {
			algvis.showVisualization(4);
		}
	}
	
	public void centerPressed(ActionEvent event) {
		visualization.getVisPane().setTranslatePos(0, 0);
	}
	
	public void snapshotPressed(ActionEvent event) {		
		Node node = visualization.getVisPane().getPane();
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

	public void setVis(Visualization visualization, String language) {
		this.visualization = visualization;
		rootPane.getChildren().set(0, visualization.getVisPaneWrapper());
		visTitle.setText(visualization.getTitle());
		buttonsTitledPane.setContent(visualization.loadButtons(language));
	}

	public void setAlgvis(AlgVis algvis) {
		this.algvis = algvis;
		if (algvis.getStage().isFullScreen()) {
			fullscreenButton.setGraphic(new ImageView("algvis2/ui/fullscreen_off.png"));
		}
		
		algvis.getStage().fullScreenProperty().addListener(new WeakChangeListener<Boolean>(fullScreenChangeListener));
	}

	public void fullscreenPressed(ActionEvent event) {
		algvis.getStage().setFullScreen(!algvis.getStage().isFullScreen());
	}
}
