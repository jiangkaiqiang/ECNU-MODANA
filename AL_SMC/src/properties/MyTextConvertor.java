package properties;

import javafx.util.StringConverter;

/**
 * @author JKQ
 *
 * 2015å¹?11æœ?29æ—¥ä¸‹å?3:42:47
 */
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
