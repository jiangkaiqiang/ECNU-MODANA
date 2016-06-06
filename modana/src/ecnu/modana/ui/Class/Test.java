package ecnu.modana.ui.Class;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

public class Test
{
    StringProperty stringProperty1;
    public void test()
    {
    	stringProperty1.set("ah");
    	//stringProperty1.addListener(ChangeListener<String>);
    }
}
