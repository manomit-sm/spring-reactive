package com.bsolz.productservice.controllers;

import com.bsolz.productservice.dtos.ProductDto;
import com.bsolz.productservice.services.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;
    private final Flux<ProductDto> flux;

    public ProductController(ProductService productService, Flux<ProductDto> flux) {

        this.productService = productService;
        this.flux = flux;
    }

    @GetMapping("all")
    public Flux<ProductDto> allProduct() {
        return productService.getAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ProductDto> insertProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return productService.insertProduct(productDtoMono);
    }
    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id, @RequestBody Mono<ProductDto> productDtoMono) {
        return productService.updateProduct(id, productDtoMono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("all")
    public Flux<ProductDto> getByPriceRange(@RequestParam("min") int min, @RequestParam("max") int max) {

        return productService.getProductByPriceRange(min, max);
    }

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProductUpdates() {
        return this.flux;
    }

}
