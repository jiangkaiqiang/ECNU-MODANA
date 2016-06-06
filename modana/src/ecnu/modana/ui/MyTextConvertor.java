package ecnu.modana.ui;

import javafx.util.StringConverter;

public class MyTextConvertor extends StringConverter<String> {

	@Override
	public String fromString(String string) {
		return string;
	}

	@Override
	public String toString(String string) {
		return string;
	}

}
