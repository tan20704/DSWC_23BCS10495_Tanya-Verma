package com.dswcfsd.day06.q2;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

@Component
@Lazy
class ImageRenderingEngine {
    public ImageRenderingEngine() {
        System.out.println("ImageRenderingEngine initialized lazily.");
    }

    public void render() {
        System.out.println("Rendering image data for the scan.");
    }
}

@Component
@Scope(SCOPE_PROTOTYPE)
class PatientContext {
    public PatientContext() {
        System.out.println("New PatientContext instance created: " + this);
    }

    public void initialize() {
        System.out.println("PatientContext initialized for new scan.");
    }
}

@Component
class ScanOrchestrator {
    private final ImageRenderingEngine renderingEngine;
    private final ObjectProvider<PatientContext> patientContextProvider;

    public ScanOrchestrator(@Lazy ImageRenderingEngine renderingEngine,
                             ObjectProvider<PatientContext> patientContextProvider) {
        this.renderingEngine = renderingEngine;
        this.patientContextProvider = patientContextProvider;
    }

    public void processScan() {
        System.out.println("Processing new scan...");
        PatientContext context = patientContextProvider.getObject();
        context.initialize();
        renderingEngine.render();
    }
}

@Component
class Q2Runner implements org.springframework.boot.CommandLineRunner {
    private final ScanOrchestrator orchestrator;

    public Q2Runner(ScanOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== DAY-06 Q2: Lazy and Prototype Injection ===");
        orchestrator.processScan();
        orchestrator.processScan();
    }
}
