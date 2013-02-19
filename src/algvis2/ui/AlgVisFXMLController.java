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
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import jfxtras.labs.dialogs.DialogFX;
import jfxtras.labs.dialogs.DialogFXBuilder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
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
	
		openStage(image);
	}
	
	private void openStage(final Image image){
		StackPane sp = new StackPane();

		final Stage stage = StageBuilder.create()
				.title("Snapshot")
				.scene(new Scene(sp))
				.build();
		
		Button saveButton = ButtonBuilder.create()
				.text("Save")
				.onMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						showFileChooser(stage, image);
					}
				})
				.build();
		
		StackPane.setAlignment(saveButton, Pos.TOP_LEFT);
		StackPane.setMargin(saveButton, new Insets(10));
		
		sp.getChildren().addAll(new ImageView(image), saveButton);		
		
		stage.show();
	}
	
	private void showFileChooser(Stage stage, Image image) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as");
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			String fileName = file.getName();
			String extension = "";
			int i = fileName.lastIndexOf('.');
			if (i > 0) {
				extension = fileName.substring(i + 1);
			}

			HashSet<String> writerFormatNames = new HashSet<String>();
			Collections.addAll(writerFormatNames, ImageIO.getWriterFormatNames());

			if (writerFormatNames.contains(extension) && (extension.toLowerCase().equals("png") || extension
					.toLowerCase().equals("gif"))) {				
				try {
					image = null;
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), extension, file);
				} catch (Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));
					
					DialogFX dialog = DialogFXBuilder.create()
							.type(DialogFX.Type.ERROR)
							.titleText("Something went wrong :(")
							.message("An unexpected error has occurred. Image cannot be saved.")
							.build();
					
					dialog.showDialog();
				}
			} else {
				DialogFX dialog = DialogFXBuilder.create()
						.type(DialogFX.Type.ERROR)
						.titleText("Error")
						.message("Bad file format. Use .png or .gif file format.")
						.build();
				
				dialog.showDialog();
				showFileChooser(stage, image);
			}
		}

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
