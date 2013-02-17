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

package algvis2.scene.control;

import algvis.core.MyRandom;
import algvis.core.WordGenerator;
import javafx.scene.control.TextField;

import java.util.Vector;

public class InputField {
	public final static int MAX_VALUE = 999;
	private final TextField textField;

	public InputField(TextField textField) {
		this.textField = textField;
	}

	/**
	 * Returns an integer in the range 1..MAX. If no input is given, a default
	 * value def is returned.
	 */
	public int getInt(int def) {
		return getInt(def, 1, MAX_VALUE);
	}

	/**
	 * Returns an integer in the range min..max. If no input is given, a default
	 * value def is returned.
	 */
	public int getInt(int def, int min, int max) {
		int n = def;
		String firstWord = textField.getText().split("(\\s|,)")[0];
		try {
			n = Integer.parseInt(firstWord);
			if (n < min) {
				n = min;
				// TODO vyskoci cervena bublina (mozno oranzova)
				//				sb.setText("value too small; using the minimum value " + min
				//					+ " instead");
			}
			if (n > max) {
				n = max;
				// TODO
				//				sb.setText("value too high; using the maximum value " + max
				//					+ " instead");
			}
		} catch (NumberFormatException e) {
			// TODO
			//			sb.setText("couldn't parse an integer; using the default value "
			//				+ def);
		}
		textField.setText("");
		return n;
	}

	/**
	 * Returns a vector of integers in the range 1..MAX. Numbers in the input
	 * may be delimited by whitespaces and/or commas.
	 */
	public Vector<Integer> getVI() {
		return getVI(1, MAX_VALUE);
	}

	/**
	 * Returns a vector of integers in the range min..max. Numbers in the input
	 * may be delimited by whitespaces and/or commas.
	 */
	public Vector<Integer> getVI(int min, int max) {
		boolean range = false;
		Vector<Integer> args = new Vector<Integer>();
		String[] tokens = textField.getText().replaceAll("\\.{2,}", " .. ")
				.split("(\\s|,)+");
		for (String t : tokens) {
			if ("..".equals(t)) {
				range = true;
			} else {
				int x = min;
				try {
					x = Integer.parseInt(t);
					if (x < min) {
						x = min;
						// TODO
						//						sb.setText("value too small; using the minimum value instead");
					}
					if (x > max) {
						x = max;
						// TODO
						//						sb.setText("value too high; using the maximum value instead");
					}
					if (range) {
						int a = args.lastElement();
						for (int i = a + 1; i < x; ++i) {
							args.add(i);
						}
						for (int i = a - 1; i > x; --i) {
							args.add(i);
						}
						range = false;
					}
					args.add(x);
				} catch (NumberFormatException e) {
					// TODO
					//					sb.setText("couldn't parse an integer");
				}
			}
		}
		textField.setText("");
		return args;
	}

	/**
	 * Returns a non-empty vector of integers in the range 1..MAX. Numbers in
	 * the input may be delimited by whitespaces and/or commas. If no input is
	 * given, a vector with 1 random value in the range 1..MAX is returned.
	 */
	public Vector<Integer> getNonEmptyVI() {
		return getNonEmptyVI(1, MAX_VALUE);
	}

	/**
	 * Returns a non-empty vector of integers in the range min..max. Numbers in
	 * the input may be delimited by whitespaces and/or commas. If no input is
	 * given, a vector with 1 random value in the range min..max is returned.
	 */
	public Vector<Integer> getNonEmptyVI(int min, int max) {
		Vector<Integer> args = getVI();
		if (args.size() == 0) {
			args.add(MyRandom.Int(min, max));
			// TODO
			//			sb.setText("no input; using random value");
		}
		return args;
	}

	/**
	 * Returns a vector of strings parsed from input line delimited by spaces.
	 * [a-z] -> [A-Z], All chars except [A-Z] are lost.
	 */
	public Vector<String> getVS() {
		String ss = textField.getText();
		Vector<String> result = new Vector<String>();
		if (ss.compareTo("") == 0) {
			result.add(WordGenerator.getWord());
		} else {
			result = WordGenerator.parseString(ss);
		}
		textField.setText("");
		return result;
	}

	public Vector<String> getVABS() {
		String ss = textField.getText();
		Vector<String> result = new Vector<String>();
		if (ss.compareTo("") == 0) {
			result.add(WordGenerator.getABWord(20));
		} else {
			result = WordGenerator.parseString(ss);
		}
		textField.setText("");
		return result;
	}
}
