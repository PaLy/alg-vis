package algvis.internationalization;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class ChLabel extends JLabel {
	private static final long serialVersionUID = 8534784764906070592L;

	public ChLabel(String text) {
		super(text);
	}

	public void refresh() {
		FontMetrics metrics = getFontMetrics(getFont());
		int width = metrics.stringWidth(getText());
		int height = metrics.getHeight();
		Dimension newDimension = new Dimension(width, height + 10);
		setPreferredSize(newDimension);
		setBounds(new Rectangle(getLocation(), getPreferredSize()));
	}
}
