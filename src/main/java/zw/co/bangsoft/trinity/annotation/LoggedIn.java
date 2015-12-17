package zw.co.bangsoft.trinity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.ws.BindingType;

@Retention(RetentionPolicy.RUNTIME)

@Target({ElementType.TYPE,
  ElementType.METHOD,
  ElementType.FIELD})

@BindingType
public @interface LoggedIn {}
