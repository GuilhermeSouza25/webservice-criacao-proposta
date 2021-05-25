package br.com.zupacademy.webservice_propostas.shared.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValorDoEnumValidator.class)
@Documented
//@Repeatable(List.class)
public @interface ValorDoEnum {
	
	Class<? extends Enum<?>> enumClass();
	
    String message() default "não é um valor válido";
            
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    
    //String message();
    
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
    	ValorDoEnum[] value();
    }
}

