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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class NodePaint {
	public static final NodePaint BLACK = new NodePaint(Color.WHITE,
		Color.BLACK);
	public static final NodePaint BLUE = new NodePaint(Color.WHITE, Color.BLUE);
	public static final NodePaint GREEN = new NodePaint(Color.BLACK,
		Color.LIMEGREEN);
	public static final NodePaint RED = new NodePaint(Color.BLACK, Color.RED);

	public static final NodePaint NORMAL = new NodePaint(Color.BLACK,
		Color.web("0xfecb65"));
	public static final NodePaint DARKER = new NodePaint(Color.BLACK,
		Color.web("0xCDCD00"));

	public static final NodePaint INSERT = new NodePaint(Color.WHITE,
		Color.web("0x3366ff"));
	public static final NodePaint FIND = new NodePaint(Color.BLACK,
		Color.LIGHTGRAY);
	public static final NodePaint FOUND = GREEN;
	public static final NodePaint NOTFOUND = RED;
	public static final NodePaint DELETE = RED;
	public static final NodePaint CACHED = new NodePaint(Color.BLACK,
		Color.PINK);
	public static final NodePaint DELETED = new NodePaint(Color.BLACK,
		Color.DARKGRAY);

	public final ObjectProperty<Paint> background, text;

	public NodePaint(Paint text, Paint background) {
		this.text = new SimpleObjectProperty<Paint>(text);
		this.background = new SimpleObjectProperty<Paint>(background);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NodePaint && background.get().equals(((NodePaint) obj).background.get())
			&& text.get().equals(((NodePaint) obj).text.get());
	}
}
