package br.com.zupacademy.webservice_propostas.cartao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.GenericGenerator;

import br.com.zupacademy.webservice_propostas.cartao.cartao_client.VencimentoResponse;

@Entity
public class Cartao {
	
	@Id 
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@NotBlank
	private String numero;
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
		this.numero = id;
		this.emitidoEm = emitidoEm;
		this.titular = titular;
		this.limite = limite;
		this.vencimento = vencimento.converter(this);
	}
	
	public String getId() {
		return numero;
	}
}
