package br.com.zupacademy.webservice_proposta.shared.validators;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.Assert;

public class ExistsIdValidator implements ConstraintValidator<ExistsId, String> {

    
	private String field;
	private Class<?> clazz;
	@PersistenceContext
	EntityManager manager;
	
	
	@Override
	public void initialize(ExistsId params) {
		field = params.fieldName();
		clazz = params.domainClass();
	}
	
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
    	
    	if(value == null) {
    		return true;
    	}
    	
    	if(value.matches("[\\s]*[0-9]*[1-9]+")) {
    		Long id = Long.valueOf(value);
    		
    		List<?> resultList = manager
    				.createQuery("SELECT 1 FROM " +clazz.getName()+ " WHERE " +field+ "=:id")
    				.setParameter("id", id)
    				.getResultList();
    		
    		Assert.state(resultList.size() <= 1, "Foi encontrado mais de um "+clazz.getName()+" com o atributo "+field+" = "+value);
    		
    		return !resultList.isEmpty();
    		
    	} else {
    		
    		return false;
    	}
    }	
}