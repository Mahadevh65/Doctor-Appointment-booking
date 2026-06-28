package com.hospital.management.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DashboardStatsResponse {
    private long totalDoctors;
    private long totalPatients;
    private long totalAppointments;
    private long pendingAppointments;
    private long approvedAppointments;
    private long completedAppointments;
    private long todaysAppointments;
}
