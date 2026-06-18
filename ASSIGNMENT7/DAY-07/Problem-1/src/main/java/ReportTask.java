package com.stalep;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ReportTask {
    private String reportId;
    private String userData;
    
    public ReportTask() {
        this.reportId = java.util.UUID.randomUUID().toString();
        System.out.println("ReportTask created with ID: " + reportId);
    }
    
    public void processReport(String data) {
        this.userData = data;
        System.out.println("ReportTask [" + reportId + "] processing: " + data);
    }
    
    public String getReportId() { return reportId; }
    public String getUserData() { return userData; }
}
