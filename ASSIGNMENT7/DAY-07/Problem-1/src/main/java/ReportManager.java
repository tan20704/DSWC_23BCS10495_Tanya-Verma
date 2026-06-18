package com.stalep;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Service
public class ReportManager {
    private final ObjectFactory<ReportTask> taskFactory;
    
    public ReportManager(ObjectFactory<ReportTask> taskFactory) {
        this.taskFactory = taskFactory;
    }
    
    public void executeReport(String data) {
        // Each call gets a fresh prototype instance via ObjectFactory
        ReportTask task = taskFactory.getObject();
        task.processReport(data);
        System.out.println("Report execution complete for task: " + task.getReportId());
    }
}
