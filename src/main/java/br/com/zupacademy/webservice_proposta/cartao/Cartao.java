package br.com.zupacademy.webservice_proposta.cartao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.zupacademy.webservice_proposta.cartao.cartao_client.VencimentoResponse;

@Entity
public class Cartao {
	
	@Id
	private String id;
	@NotNull
	private LocalDateTime emitidoEm;
	@NotEmpty
	private String titular;
	@Positive
	private BigDecimal limite;
	@Valid
	@OneToOne(mappedBy = "cartao", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private Vencimento vencimento;
	
	@Deprecated
	public Cartao() {}

	public Cartao(String id, @NotNull LocalDateTime emitidoEm, @NotEmpty String titular, @Positive BigDecimal limite,
			@Valid VencimentoResponse vencimento) {
		super();
		this.id = id;
		this.emitidoEm = emitidoEm;
		this.titular = titular;
		this.limite = limite;
		this.vencimento = vencimento.converter(this);
	}
}
