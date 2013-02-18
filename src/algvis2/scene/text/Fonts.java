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

package algvis2.scene.text;

import algvis2.scene.viselem.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Fonts {
	public static final Font NODE_FONT = new Font(Node.RADIUS - 3);
	public static final Font VER_NO_FONT = new Font(Node.RADIUS - 5);
	public static final Font TOP_TEXT = Font.font("Arial", FontWeight.BOLD, 15);
	public static final Font PURISA_BOLD_30 = Font.loadFont(Fonts.class.getResourceAsStream("Purisa-Bold.ttf"), 30.0);
}
