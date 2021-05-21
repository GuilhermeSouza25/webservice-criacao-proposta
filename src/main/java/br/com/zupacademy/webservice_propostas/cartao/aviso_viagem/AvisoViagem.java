package br.com.zupacademy.webservice_propostas.cartao.aviso_viagem;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import br.com.zupacademy.webservice_propostas.cartao.Cartao;

@Entity
@SuppressWarnings("unused")
public class AvisoViagem {
	
	@Id 
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	private LocalDateTime instanteAviso = LocalDateTime.now();
	
	private String ipCliente;
	
	private String userAgent;
	
	@ManyToOne
	private Cartao cartao;
	
	public AvisoViagem(String ipCliente, String userAgent, Cartao cartao) {
		super();
		this.ipCliente = ipCliente;
		this.userAgent = userAgent;
		this.cartao = cartao;
	}
	
	
}
