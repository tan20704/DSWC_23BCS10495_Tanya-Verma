package com.dswcfsd.day06.q1;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

interface TradingStrategy {
    void executeTrade();
}

abstract class AbstractStrategy implements TradingStrategy {
    protected final String assetClass;

    protected AbstractStrategy(String assetClass) {
        this.assetClass = assetClass;
    }
}

@Component
class MomentumStrategy extends AbstractStrategy {
    public MomentumStrategy() {
        super("EQUITY");
    }

    @Override
    public void executeTrade() {
        System.out.println("MomentumStrategy executing on " + assetClass);
    }
}

@Component
class ArbitrageStrategy extends AbstractStrategy {
    public ArbitrageStrategy() {
        super("FX");
    }

    @Override
    public void executeTrade() {
        System.out.println("ArbitrageStrategy executing on " + assetClass);
    }
}

@Service
class MarketDataService {
    public void warmCache() {
        System.out.println("MarketDataService cache warmed up.");
    }
}

@Service
class AlertService {
    public void alert(String message) {
        System.out.println("ALERT: " + message);
    }
}

@Component
class TradingEngine implements BeanNameAware, InitializingBean {
    private final MarketDataService marketDataService;
    private final List<TradingStrategy> strategies;
    private AlertService alertService;
    private String beanName;

    @Autowired
    public TradingEngine(MarketDataService marketDataService, List<TradingStrategy> strategies) {
        this.marketDataService = marketDataService;
        this.strategies = strategies;
    }

    @Autowired(required = false)
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
        System.out.println("Optional AlertService injected: " + (alertService != null));
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("TradingEngine bean name set to: " + beanName);
    }

    @PostConstruct
    public void warmUpCache() {
        System.out.println("TradingEngine warming up cache...");
        marketDataService.warmCache();
    }

    @Override
    public void afterPropertiesSet() {
        if (marketDataService == null || strategies == null || strategies.isEmpty()) {
            throw new IllegalStateException("TradingEngine is not configured correctly.");
        }
        System.out.println("TradingEngine safety validation passed. Strategies loaded: " + strategies.size());
    }

    public void executeStrategies() {
        System.out.println("TradingEngine starting strategy execution...");
        strategies.forEach(TradingStrategy::executeTrade);
        if (alertService != null) {
            alertService.alert("High-risk trade executed.");
        }
    }

    @PreDestroy
    public void closePositions() {
        System.out.println("TradingEngine closing all open market positions.");
    }
}

@Component
class Q1Runner implements org.springframework.boot.CommandLineRunner {
    private final TradingEngine tradingEngine;

    public Q1Runner(TradingEngine tradingEngine) {
        this.tradingEngine = tradingEngine;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== DAY-06 Q1: Trading Engine Lifecycle ===");
        tradingEngine.executeStrategies();
    }
}
