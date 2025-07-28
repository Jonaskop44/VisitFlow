package com.jonas.visitflow.product;

import com.jonas.visitflow.product.dto.CreateProductDto;
import com.jonas.visitflow.product.dto.ProductDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{companyId}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCompany(@PathVariable UUID companyId, Principal principal) {
        String userId = principal.getName();
        List<ProductDto> productDto = productService.getAllProductsByCompany(companyId, userId);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping("/{companyId}/create")
    public ResponseEntity<ProductDto> createProduct(@PathVariable UUID companyId,
                                                        @RequestBody @Valid CreateProductDto createProductDto,
                                                        Principal principal) {
        String userId = principal.getName();
        ProductDto productDto = productService.createProduct(createProductDto, companyId, userId);
        return ResponseEntity.ok(productDto);
    }

    @PatchMapping("/{productId}/update")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                        @RequestBody @Valid CreateProductDto createProductDto,
                                                        Principal principal) {
        String userId = principal.getName();
        ProductDto productDto = productService.updateProduct(createProductDto, productId, userId);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId, Principal principal) {
        String userId = principal.getName();
        ProductDto productDto = productService.deleteProduct(productId, userId);
        return ResponseEntity.ok(productDto);
    }

}
