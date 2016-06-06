package ecnu.modana.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * specify a plugin implementation class which is unnecessary to be instantiated
 * (usually used in development stage)
 * @author cb
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface NonPlugin {

}
