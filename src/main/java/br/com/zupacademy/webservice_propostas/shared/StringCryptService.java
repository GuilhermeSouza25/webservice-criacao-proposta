package br.com.zupacademy.webservice_propostas.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class StringCryptService {
	
	private static String secretKey;
	
	private static String salt;
	
	@Value("${spring.security.crypt.secretKey}")
	public void setSecretKey(String secretKey) {
		StringCryptService.secretKey = secretKey;
	}
	
	@Value("${spring.security.crypt.salt}")
	public void setSalt(String salt) {
		StringCryptService.salt = salt;
	}
	
	public static String encrypt(String value) {
		TextEncryptor encryptor = Encryptors.text(secretKey, salt);
		return encryptor.encrypt(value);
	}
	
	public static String dencrypt(String value) {
		TextEncryptor encryptor = Encryptors.text(secretKey, salt);
		return encryptor.decrypt(value);
	}
}
