package algvis.internationalization;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Languages {
	final int N = 2;
	int current_lang;
	Locale[] all_locales = new Locale[N];
	ResourceBundle[] all_msgs = new ResourceBundle[N];
	Locale locale;
	ResourceBundle msg;
	List<LanguageListener> listeners = new LinkedList<LanguageListener>();

	public Languages() {
		all_locales[0] = new Locale("en");
		all_msgs[0] = ResourceBundle.getBundle("Messages", all_locales[0]);
		all_locales[1] = new Locale("sk");
		all_msgs[1] = ResourceBundle.getBundle("Messages_sk", all_locales[1]);
	}

	public Languages(int i) {
		this();
		selectLanguage(i);
	}

	public Languages(String s) {
		this();
		selectLanguage(s);
	}

	public void addListener(LanguageListener l) {
		listeners.add(l);
	}

	public void selectLanguage(int i) {
		if (i < 0 || i >= N) {
			i = 0;
		}
		current_lang = i;
		locale = all_locales[current_lang];
		msg = all_msgs[current_lang];
		// notify all listeners
		for (LanguageListener l : listeners) {
			l.languageChanged();
		}
	}

	public void selectLanguage(String s) {
		int i;
		if ("sk".equals(s)) {
			i = 1;
		} else {
			i = 0;
		}
		selectLanguage(i);
	}

	public String getString(String s) {
		try {
			return all_msgs[current_lang].getString(s);
		} catch (MissingResourceException e) {
			System.out.println(e.getMessage() + ": " + s);
			return "???";
		}
	}
}
