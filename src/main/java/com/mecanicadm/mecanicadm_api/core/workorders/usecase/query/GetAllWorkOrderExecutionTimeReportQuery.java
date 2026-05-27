package com.mecanicadm.mecanicadm_api.core.workorders.usecase.query;

import java.time.LocalDate;

public record GetAllWorkOrderExecutionTimeReportQuery(
        LocalDate initialDate,
        LocalDate finalDate
) {
}

