package com.bsolz.productservice.services;

import com.bsolz.productservice.dtos.ProductDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class DataSetupService implements CommandLineRunner {

    private final ProductService productService;

    public DataSetupService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        ProductDto p1 = new ProductDto("4k-tv", 1000);
        ProductDto p2 = new ProductDto("slr-camera", 750);
        ProductDto p3 = new ProductDto("iphone", 800);

        Flux.just(p1, p2, p3)
                .flatMap(p -> productService.insertProduct(Mono.just(p)))
                .subscribe(System.out::println);
    }
}
