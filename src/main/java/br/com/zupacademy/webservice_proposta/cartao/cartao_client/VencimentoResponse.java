package br.com.zupacademy.webservice_proposta.cartao.cartao_client;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import br.com.zupacademy.webservice_proposta.cartao.Cartao;
import br.com.zupacademy.webservice_proposta.cartao.Vencimento;

public class VencimentoResponse {
	
	@NotBlank
	private String id;
	
	@Range(min = 1, max = 31)
	private Integer dia;
	
	@NotNull
	private LocalDateTime dataDeCriacao;

	public VencimentoResponse(@NotBlank String id, @Range(min = 1, max = 31) Integer dia,
			@NotNull LocalDateTime dataDeCriacao) {
		this.id = id;
		this.dia = dia;
		this.dataDeCriacao = dataDeCriacao;
	}
	
	public Vencimento converter(Cartao cartao) {
		return new Vencimento(id, dia, dataDeCriacao, cartao);
	}
}
