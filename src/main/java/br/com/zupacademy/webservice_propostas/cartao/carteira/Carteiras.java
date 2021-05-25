package br.com.zupacademy.webservice_propostas.cartao.carteira;

public enum Carteiras {
	
	PAYPAL,
	SAMSUNG_PAY;
	
	public static Carteiras[] getArray() {
		return Carteiras.class.getEnumConstants();
	}
}


