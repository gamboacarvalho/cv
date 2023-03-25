package orm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcCol {
	
	String value() default "";
	
	boolean isPk() default false;
	
	boolean isIdentity() default false;
	
	Class<?> referencedKeyClass() default JdbcCol.class; 
}
