package com.dswcfsd.day06.q5;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

import java.util.Map;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

interface WebhookIntegration {
    void send(String payload);
}

@Component
class SlackIntegration implements WebhookIntegration {
    @Override
    public void send(String payload) {
        System.out.println("SlackIntegration sending: " + payload);
    }
}

@Component
class DiscordIntegration implements WebhookIntegration {
    @Override
    public void send(String payload) {
        System.out.println("DiscordIntegration sending: " + payload);
    }
}

@Component
class EmailIntegration implements WebhookIntegration {
    @Override
    public void send(String payload) {
        System.out.println("EmailIntegration sending: " + payload);
    }
}

@Component
class WebhookDispatcher implements ApplicationContextAware, SmartInitializingSingleton {
    private ApplicationContext applicationContext;
    private Map<String, WebhookIntegration> integrations;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.integrations = applicationContext.getBeansOfType(WebhookIntegration.class);
        System.out.println("WebhookDispatcher routing table ready with " + integrations.size() + " integrations.");
    }

    public void printRoutingTable() {
        if (integrations == null) {
            System.out.println("Routing table not initialized yet.");
            return;
        }
        integrations.forEach((name, integration) -> System.out.println("Integration bean: " + name));
    }
}

@Component
class Q5Runner implements org.springframework.boot.CommandLineRunner {
    private final WebhookDispatcher dispatcher;

    public Q5Runner(WebhookDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== DAY-06 Q5: Late Initialization with SmartInitializingSingleton ===");
        dispatcher.printRoutingTable();
    }
}
