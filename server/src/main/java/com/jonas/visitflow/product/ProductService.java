package com.jonas.visitflow.product;

import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Product;
import com.jonas.visitflow.product.dto.CreateProductDto;
import com.jonas.visitflow.product.dto.ProductDto;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.ProductRepository;
import com.jonas.visitflow.stripe.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final StripeService stripeService;


    public List<ProductDto> getAllProductsByCompany(UUID companyId, String userId) {
        Company company = companyRepository.findByIdAndUserId(companyId, userId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        List<Product> productsList = productRepository.findAllByCompany(company);

        return productsList.stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    public ProductDto createProduct(CreateProductDto createProductDto, UUID companyId, String userId) {
        Company company = companyRepository.findByIdAndUserId(companyId, userId).
                orElseThrow(() -> new NotFoundException("Company not found"));

        //Create Stripe Product
        String productId = stripeService.createProduct(createProductDto.getName());
        String priceId = stripeService.createPrice(productId, createProductDto.getPrice());

        Product product = Product.builder()
                .name(createProductDto.getName())
                .duration(createProductDto.getDuration())
                .price(createProductDto.getPrice())
                .stripeProductId(productId)
                .stripePriceId(priceId)
                .company(company)
                .build();

        product = productRepository.save(product);
        return ProductDto.fromEntity(product);
    }

    public ProductDto updateProduct(CreateProductDto createProductDto, Long productId, String userId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Product not found"));

        if(!product.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this product");
        }

        String updatedProduct = stripeService.updateProduct(
                product.getStripeProductId(),
                createProductDto.getName(),
                product.getStripePriceId(),
                createProductDto.getPrice());

        product.setName(createProductDto.getName());
        product.setDuration(createProductDto.getDuration());
        product.setPrice(createProductDto.getPrice());
        product.setStripePriceId(updatedProduct);

        product = productRepository.save(product);
        return ProductDto.fromEntity(product);
    }

    public ProductDto deleteProduct(Long productId, String userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if(!product.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this product");
        }

        stripeService.deleteProduct(product.getStripeProductId(), product.getStripePriceId());

        productRepository.delete(product);
        return ProductDto.fromEntity(product);
    }

}
