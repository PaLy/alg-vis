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

package algvis2.scene.paint;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class NodePaint {
	public static final NodePaint BLACK = new NodePaint(Color.BLACK, Color.WHITE);
	public static final NodePaint BLUE = new NodePaint(Color.BLUE, Color.WHITE);
	public static final NodePaint GREEN = new NodePaint(Color.LIMEGREEN, Color.BLACK);
	public static final NodePaint RED = new NodePaint(Color.RED, Color.WHITE);

	public static final NodePaint NORMAL = new NodePaint(Color.web("0xfecb65"), Color.BLACK);
	public static final NodePaint DARKER = new NodePaint(Color.web("0xCDCD00"), Color.BLACK);

	public static final NodePaint INSERT = new NodePaint(Color.LIGHTBLUE, Color.BLACK);
	public static final NodePaint FIND = new NodePaint(Color.LIGHTGRAY, Color.BLACK);
	public static final NodePaint FOUND = GREEN;
	public static final NodePaint NOTFOUND = RED;
	public static final NodePaint DELETE = RED;
	public static final NodePaint CACHED = new NodePaint(Color.PINK, Color.BLACK);
	public static final NodePaint DELETED = new NodePaint(Color.DARKGRAY, Color.BLACK);

	private final Paint background, text;

	public NodePaint(Paint background, Paint text) {
		this.text = text;
		this.background = background;
	}

	public NodePaint(NodePaint paint) {
		this.text = paint.text;
		this.background = paint.background;
	}

	public Paint getBackground() {
		return background;
	}
	
	public Paint getText() {
		return text;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof NodePaint
				&& background.equals(((NodePaint) obj).background)
				&& text.equals(((NodePaint) obj).text);
	}

	@Override
	public int hashCode() {
		int result = 27;
		
		result = 31 * result;
		int inc = background == null ? 0 : background.hashCode();
		result += inc;
		
		result = 31 * result;
		inc = text == null ? 0 : text.hashCode();
		result += inc;
		
		return result;
	}
}
