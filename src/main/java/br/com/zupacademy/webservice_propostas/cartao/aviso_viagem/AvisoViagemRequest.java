package br.com.zupacademy.webservice_propostas.cartao.aviso_viagem;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class AvisoViagemRequest {
	
	@NotBlank
	private String destino;
	
	@NotNull
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private LocalDate validoAte;
	
	public AvisoViagemRequest(@NotBlank String destino, @NotNull @FutureOrPresent LocalDate validoAte) {
		super();
		this.destino = destino;
		this.validoAte = validoAte;
	}
	
	public String getDestino() {
		return destino;
	}
	
	public LocalDate getValidoAte() {
		return validoAte;
	}
}	
