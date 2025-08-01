package com.jonas.visitflow.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createProduct(String name) {
        try {
            ProductCreateParams params = ProductCreateParams.builder()
                    .setName(name)
                    .build();

            Product product = Product.create(params);
            return product.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Stripe Product creation failed: " + e.getMessage());
        }
    }

    public String createPrice(String productId, BigDecimal priceInCents) {
        try {
            Long unitAmount = priceInCents
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValue();

            PriceCreateParams params = PriceCreateParams.builder()
                    .setProduct(productId)
                    .setCurrency("eur")
                    .setUnitAmount(unitAmount) // cents
                    .build();
            Price price = Price.create(params);
            return price.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Stripe Price creation failed: " + e.getMessage());
        }
    }

    public void deleteProduct(String productId, String priceId) {
        try {
            Price price = Price.retrieve(priceId);
            price.update(PriceUpdateParams.builder().setActive(false).build());

            Product product = Product.retrieve(productId);
            product.update(ProductUpdateParams.builder().setActive(false).build());

        } catch (StripeException e) {
            throw new RuntimeException("Stripe Product deletion failed: " + e.getMessage());
        }
    }

    public String updateProduct(String productId, String name, String oldPriceId, BigDecimal newPriceAmount) {
        try {
            Product resource = Product.retrieve(productId);
            ProductUpdateParams productParams = ProductUpdateParams.builder()
                    .setName(name)
                    .build();
            resource.update(productParams);

            Price oldPrice = Price.retrieve(oldPriceId);
            PriceUpdateParams priceParams = PriceUpdateParams.builder()
                    .setActive(false)
                    .build();
            oldPrice.update(priceParams);

            long amountInCents = newPriceAmount.multiply(BigDecimal.valueOf(100)).longValueExact();
            PriceCreateParams newPriceParams = PriceCreateParams.builder()
                    .setUnitAmount(amountInCents)
                    .setCurrency("eur")
                    .setProduct(productId)
                    .build();
            Price newPrice = Price.create(newPriceParams);

            return newPrice.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Stripe Product update failed: " + e.getMessage());
        }
    }

    public Map<String, String> createCheckoutSession(String priceId, String successUrl, String cancelUrl) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice(priceId)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            return Map.of(
                    "sessionId", session.getId(),
                    "url", session.getUrl()
            );
        } catch (StripeException e) {
            throw new RuntimeException("Stripe Checkout Session creation failed: " + e.getMessage());
        }
    }

}
