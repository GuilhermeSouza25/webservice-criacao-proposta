package br.com.zupacademy.webservice_propostas.cartao.bloqueio;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

@Entity
@SuppressWarnings("unused")
public class Bloqueio {
	
	@Id 
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	private Boolean ativo = true;
	
	private LocalDateTime bloqueadoEm = LocalDateTime.now();
	
	private String ipCliente;
	
	private String userAgent;
	
	@ManyToOne
	private Cartao cartao;
	
	@Deprecated
	public Bloqueio() {}
	
	public Bloqueio(String ipCliente, String userAgent, Cartao cartao) {
		super();
		this.ipCliente = ipCliente;
		this.userAgent = userAgent;
		this.cartao = cartao;
	}
	
	
	
	
}
