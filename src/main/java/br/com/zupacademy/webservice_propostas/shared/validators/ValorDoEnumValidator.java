package br.com.zupacademy.webservice_propostas.shared.validators;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValorDoEnumValidator implements ConstraintValidator<ValorDoEnum, String> {

	// private String message;
	private List<String> acceptedValues;
	private Enum<?>[] enumConstants;
	
	@Override
	public void initialize(ValorDoEnum params) {
		enumConstants = params.enumClass().getEnumConstants();
		
		acceptedValues = Stream.of(enumConstants)
				.map(Enum::name)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
		if (value == null) {
			return true;
		}
		
		constraintContext.disableDefaultConstraintViolation();
		constraintContext
			.buildConstraintViolationWithTemplate("deve ser algum dos valores: " + acceptedValues)
			.addConstraintViolation();
		
		return acceptedValues.contains(value.toString());
	}

}