package algvis2.ds.persistent;

import algvis2.core.Visualization;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.TreeHighlighter;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Collections;

abstract public class PersistentVisualization extends Visualization {
	private final TreeHighlighter versionHighlighter = new TreeHighlighter();

	public PersistentVisualization(URL buttonsFile) {
		super(buttonsFile);
		visPane.addNotStorableVisElem(versionHighlighter);
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
