package br.com.zupacademy.webservice_propostas.cartao;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
public class Vencimento {
	
	@Id
	private String id;
	@Range(min = 1, max = 31)
	private Integer dia;
	@NotNull
	private LocalDateTime dataCriacao;
	@OneToOne
	@JoinColumn(name = "cartao_id")
	private Cartao cartao;
	
	@Deprecated
	public Vencimento() {}
	
	public Vencimento(String id, @Range(min = 1, max = 31) Integer dia, @NotNull LocalDateTime dataCriacao,
			Cartao cartao) {
		this.id = id;
		this.dia = dia;
		this.dataCriacao = dataCriacao;
		this.cartao = cartao;
	}
	
	
}
