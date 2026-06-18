package com.dswcfsd.day06.q3;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

interface PaymentProcessor {
    void processPayment();
}

@Primary
@Component
class StripeProcessor implements PaymentProcessor {
    @Override
    public void processPayment() {
        System.out.println("StripeProcessor processing payment.");
    }
}

@Component
class SquareProcessor implements PaymentProcessor {
    @Override
    public void processPayment() {
        System.out.println("SquareProcessor processing payment.");
    }
}

class BankXmlProcessor implements PaymentProcessor {
    private final String endpoint;
    private final int retryCount;
    private final boolean secureMode;
    private final String environment;
    private final String merchantId;

    private BankXmlProcessor(Builder builder) {
        this.endpoint = builder.endpoint;
        this.retryCount = builder.retryCount;
        this.secureMode = builder.secureMode;
        this.environment = builder.environment;
        this.merchantId = builder.merchantId;
    }

    @Override
    public void processPayment() {
        System.out.println("BankXmlProcessor processing payment with endpoint=" + endpoint + ", merchant=" + merchantId);
    }

    static class Builder {
        private String endpoint;
        private int retryCount;
        private boolean secureMode;
        private String environment;
        private String merchantId;

        Builder endpoint(String endpoint) { this.endpoint = endpoint; return this; }
        Builder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        Builder secureMode(boolean secureMode) { this.secureMode = secureMode; return this; }
        Builder environment(String environment) { this.environment = environment; return this; }
        Builder merchantId(String merchantId) { this.merchantId = merchantId; return this; }
        BankXmlProcessor build() { return new BankXmlProcessor(this); }
    }
}

@Component("bankXmlProcessor")
class BankXmlProcessorFactoryBean implements FactoryBean<PaymentProcessor> {
    @Override
    public PaymentProcessor getObject() {
        return new BankXmlProcessor.Builder()
                .endpoint("https://legacy-bank.example.com")
                .retryCount(5)
                .secureMode(true)
                .environment("prod")
                .merchantId("BANK-123")
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return PaymentProcessor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

@Component
class CheckoutService {
    private final PaymentProcessor defaultProcessor;
    private final PaymentProcessor legacyBankProcessor;

    public CheckoutService(PaymentProcessor defaultProcessor,
                           @Qualifier("bankXmlProcessor") PaymentProcessor legacyBankProcessor) {
        this.defaultProcessor = defaultProcessor;
        this.legacyBankProcessor = legacyBankProcessor;
    }

    public void checkout() {
        System.out.println("CheckoutService using default processor:");
        defaultProcessor.processPayment();
        System.out.println("CheckoutService using legacy bank processor:");
        legacyBankProcessor.processPayment();
    }
}

@Component
class Q3Runner implements org.springframework.boot.CommandLineRunner {
    private final CheckoutService checkoutService;

    public Q3Runner(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== DAY-06 Q3: FactoryBean and Payment Routing ===");
        checkoutService.checkout();
    }
}
