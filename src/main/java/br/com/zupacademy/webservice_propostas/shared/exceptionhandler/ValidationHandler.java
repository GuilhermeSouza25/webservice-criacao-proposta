package br.com.zupacademy.webservice_propostas.shared.exceptionhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestControllerAdvice
public class ValidationHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	//@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public ResponseEntity<List<ErroFormulario>> handle(MethodArgumentNotValidException exception, HttpServletResponse response) {
		
		List<ErroFormulario> listaErros400 = new ArrayList<>();
		List<ErroFormulario> listaErros422 = new ArrayList<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(e -> {
			
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroFormulario erroFormulario = new ErroFormulario(e.getField(), mensagem);
			
			if(e.getCode().equals("UniqueValue")) {
				listaErros422.add(erroFormulario);
			} else {
				listaErros400.add(erroFormulario);
			}
		});
		
		if(!listaErros422.isEmpty()) {
			return ResponseEntity.unprocessableEntity().body(listaErros422);
		} else {
			response.setStatus(400);
			return ResponseEntity.unprocessableEntity().body(listaErros400);
		}
	}
	
	@ApiResponse(responseCode = "400", description = "Erros de validação")
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class) 
	public List<Erro> handle(ConstraintViolationException exception) {
		
//		ConstraintViolationExceptionMessage cv = 
//		new ConstraintViolationExceptionMessage(exception, messageSource, LocaleContextHolder.getLocale());
		
		List<Erro> listaErros = new ArrayList<>();
		Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
		
		constraintViolations.forEach(c -> {
			String campo = c.getPropertyPath().toString().split("\\.")[1];
			Erro erro = new Erro(campo + " " + c.getMessage());
			listaErros.add(erro);
		});
		return listaErros;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class) 
	public Erro handle(MissingServletRequestParameterException exception) {
		
		return new Erro("O parâmetro " + exception.getParameterName() + " é obrigatório");
	}
	
}
