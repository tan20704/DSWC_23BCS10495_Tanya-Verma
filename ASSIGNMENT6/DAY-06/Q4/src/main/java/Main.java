package com.dswcfsd.day06.q4;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

interface PIIProcessor {
}

@Component
class TransactionService implements PIIProcessor {
    public void processTransaction() {
        System.out.println("TransactionService processing PII transaction.");
    }
}

@Component
class PublicCurrencyService {
    public void publishRate() {
        System.out.println("PublicCurrencyService publishing non-PII currency rate.");
    }
}

@Component
class PiiAuditBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PIIProcessor) {
            System.out.println("Audit log: PIIProcessor bean initialized: " + beanName);
        }
        return bean;
    }
}

@Component
class Q4Runner implements org.springframework.boot.CommandLineRunner {
    private final TransactionService transactionService;
    private final PublicCurrencyService publicCurrencyService;

    public Q4Runner(TransactionService transactionService, PublicCurrencyService publicCurrencyService) {
        this.transactionService = transactionService;
        this.publicCurrencyService = publicCurrencyService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== DAY-06 Q4: BeanPostProcessor Audit ===");
        transactionService.processTransaction();
        publicCurrencyService.publishRate();
    }
}
