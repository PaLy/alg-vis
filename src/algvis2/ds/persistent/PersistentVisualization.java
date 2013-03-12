package algvis2.ds.persistent;

import algvis2.core.Visualization;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.TreeHighlighter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

abstract public class PersistentVisualization extends Visualization {
	private final TreeHighlighter versionHighlighter = new TreeHighlighter();
	private final ObjectProperty<TreeHighlighter> algorithmHighligther = new SimpleObjectProperty<>(new 
			TreeHighlighter());

	public PersistentVisualization(URL buttonsFile) {
		super(buttonsFile);
		visPane.addNotStorableVisElem(versionHighlighter);
		algorithmHighligther.addListener(new ChangeListener<TreeHighlighter>() {
			@Override
			public void changed(ObservableValue<? extends TreeHighlighter> observable, TreeHighlighter oldValue, TreeHighlighter newValue) {
				if (newValue == null) {
					versionHighlighter.getVisual().setVisible(true);
				} else {
					versionHighlighter.getVisual().setVisible(false);
				}
			}
		});
	}
	
	TreeHighlighter getAlgorithmHighlighter(int version) {
		algorithmHighligther.setValue(new TreeHighlighter(getDataStructure().dumpVersion(version)));
		return algorithmHighligther.getValue();
	}
	
	void highlightOff() {
		algorithmHighligther.setValue(null);
	}

	@Override
	public void clear() {
		super.clear();
		versionHighlighter.update(Collections.<Node>emptyList());
	}

	@Override
	public PersistentDS getDataStructure() {
		return (PersistentDS) super.getDataStructure();
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(algorithmHighligther, algorithmHighligther.getValue());
	}

	public static class VersionHighlight implements EventHandler<MouseEvent> {
		private final int version;
		private final PersistentVisualization visualization;

		public VersionHighlight(PersistentVisualization visualization, int version) {
			this.visualization = visualization;
			this.version = version;
		}

		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				visualization.versionHighlighter.update(visualization.getDataStructure().dumpVersion(version));
			} else if (event.getButton() == MouseButton.SECONDARY) {
				visualization.versionHighlighter.update(Collections.<Node>emptyList());
			}
		}
	}
}
