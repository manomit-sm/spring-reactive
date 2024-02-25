package com.bsolz.productservice.config;

import com.bsolz.productservice.dtos.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<ProductDto> sink() {
        return Sinks.many().replay().limit(1);
    }

    @Bean
    public Flux<ProductDto> productBroadCast(Sinks.Many<ProductDto> sink) {
        return sink.asFlux();
    }
}
