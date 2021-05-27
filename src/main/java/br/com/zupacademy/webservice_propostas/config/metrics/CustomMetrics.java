package br.com.zupacademy.webservice_propostas.config.metrics;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

@Component
public class CustomMetrics {
	
	private final MeterRegistry meterRegistry;

    public CustomMetrics(MeterRegistry meterRegistry) {
    	this.meterRegistry = meterRegistry;   
    }
    
    public void contadorPropostas() {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", "Mastercard"));
        tags.add(Tag.of("banco", "Itaú"));

        Counter contadorDePropostasCriadas = this.meterRegistry.counter("total_propostas_criadas", tags);
        
        contadorDePropostasCriadas.increment();
    }
}
