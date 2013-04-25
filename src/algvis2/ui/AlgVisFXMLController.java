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
import algvis2.ds.persistent.PersistentVisualization;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	public MenuItem menu_pc_pbst;
	public MenuItem menu_pstack;
	public MenuItem menu_fn_pbst;
	public Button fullscreenButton;
	public Slider versionSlider;
	public VBox sliderWrapper;

	private Visualization visualization;
	private AlgVis algvis;
	private ChangeListener<Boolean> fullScreenChangeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
				Boolean newValue) {
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
		if (source == menu_bst) {
			algvis.showVisualization(Visualization.Type.BST);
		} else if (source == menu_avl) {
			algvis.showVisualization(Visualization.Type.AVL);
		} else if (source == menu_rb) {
			algvis.showVisualization(Visualization.Type.RB);
		} else if (source == menu_pc_pbst) {
			algvis.showVisualization(Visualization.Type.PC_PBST);
		} else if (source == menu_pstack) {
			algvis.showVisualization(Visualization.Type.PSTACK);
		} else if (source == menu_fn_pbst) {
			algvis.showVisualization(Visualization.Type.FN_PBST);
		}
	}

	public void centerPressed(ActionEvent event) {
		visualization.reLayout();
		visualization.getVisPane().clearPaneTransforms();
	}

	public void snapshotPressed(ActionEvent event) {
		Node node = visualization.getVisPane().getPane();
		WritableImage image = node.snapshot(new SnapshotParameters(), null);

		openStage(image);
	}

	private void openStage(final Image image) {
		StackPane sp = new StackPane();

		final Stage stage = StageBuilder.create().title("Snapshot").scene(new Scene(sp)).build();

		Button saveButton = ButtonBuilder.create().text("Save")
				.onMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						showFileChooser(stage, image);
					}
				}).build();

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

			HashSet<String> writerFormatNames = new HashSet<>();
			Collections.addAll(writerFormatNames, ImageIO.getWriterFormatNames());

			if (writerFormatNames.contains(extension)
					&& (extension.toLowerCase().equals("png") || extension.toLowerCase().equals(
							"gif"))) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), extension, file);
					DialogFX dialog = DialogFXBuilder.create().type(DialogFX.Type.INFO)
							.titleText("Success").message("Image saved successfully.").build();
					dialog.showDialog();
				} catch (Exception e) {
					StringWriter error = new StringWriter();
					e.printStackTrace(new PrintWriter(error));

					DialogFX dialog = DialogFXBuilder
							.create()
							.type(DialogFX.Type.ERROR)
							.titleText("Something went wrong :(")
							.message(
									"An unexpected error has occurred. Image cannot be saved.\n\n"
											+ e.toString()).build();

					dialog.showDialog();
				}
			} else {
				DialogFX dialog = DialogFXBuilder.create().type(DialogFX.Type.ERROR)
						.titleText("Error")
						.message("Bad file format. Use .png or .gif file format.").build();

				dialog.showDialog();
				showFileChooser(stage, image);
			}
		}

	}

	public void setVis(Visualization visualization, String language) {
		if (this.visualization instanceof PersistentVisualization) {
			((PersistentVisualization) this.visualization).unbindVersionSlider(versionSlider);
		}
		this.visualization = visualization;
		rootPane.getChildren().set(0, visualization.getVisPaneWrapper());
		visTitle.setText(visualization.getTitle());
		buttonsTitledPane.setContent(visualization.loadButtons(language));
		if (visualization instanceof PersistentVisualization) {
			sliderWrapper.setVisible(true);
			((PersistentVisualization) visualization).bindVersionSlider(versionSlider);
		} else {
			sliderWrapper.setVisible(false);
		}
	}

	public void setAlgvis(AlgVis algvis) {
		this.algvis = algvis;
		if (algvis.getStage().isFullScreen()) {
			fullscreenButton.setGraphic(new ImageView("algvis2/ui/fullscreen_off.png"));
		}

		algvis.getStage().fullScreenProperty()
				.addListener(new WeakChangeListener<>(fullScreenChangeListener));
	}

	public void fullscreenPressed(ActionEvent event) {
		algvis.getStage().setFullScreen(!algvis.getStage().isFullScreen());
	}
}
