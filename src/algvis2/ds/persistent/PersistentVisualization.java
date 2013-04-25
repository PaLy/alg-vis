package algvis2.ds.persistent;

import algvis2.core.Visualization;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.TreeHighlighter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

abstract public class PersistentVisualization extends Visualization {
	private final IntegerProperty curVersionProperty = new SimpleIntegerProperty(-1);
	private final IntegerProperty curAlgVersionProperty = new SimpleIntegerProperty(-1);
	private final TreeHighlighter versionHighlighter = new TreeHighlighter();

	public PersistentVisualization(URL buttonsFile) {
		super(buttonsFile);
		visPane.addNotStorableVisElem(versionHighlighter);
		curAlgVersionProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) {
				if (newValue == -1) {
					curVersionProperty.setValue(oldValue);
				} else {
					curVersionProperty.setValue(newValue);
				}
			}
		});
		curVersionProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (!newValue.equals(oldValue)) {
					if (newValue == -1) {
						versionHighlighter.update(Collections.<Node> emptyList());
					} else {
						versionHighlighter.update(getDataStructure().dumpVersion((Integer) newValue));
					}
				}
			}
		});
	}
	
	void setCurAlgVersion(int version) {
		curAlgVersionProperty.setValue(version);
	}

	@Override
	public void clear() {
		super.clear();
		curVersionProperty.setValue(-1);
	}

	@Override
	public PersistentDS getDataStructure() {
		return (PersistentDS) super.getDataStructure();
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(curAlgVersionProperty, curAlgVersionProperty.getValue());
	}

	public void bindVersionSlider(final Slider versionSlider) {
		versionSlider.maxProperty().bind(getDataStructure().versionsCountProperty.subtract(1));
		versionSlider.valueProperty().bindBidirectional(curVersionProperty);
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
				visualization.curVersionProperty.setValue(version);
			} else if (event.getButton() == MouseButton.SECONDARY) {
				visualization.curVersionProperty.setValue(-1);
			}
		}
	}
}
