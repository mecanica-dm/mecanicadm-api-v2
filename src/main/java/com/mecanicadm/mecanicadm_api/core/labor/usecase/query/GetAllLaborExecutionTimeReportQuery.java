package com.mecanicadm.mecanicadm_api.core.labor.usecase.query;

import java.time.LocalDate;

public record GetAllLaborExecutionTimeReportQuery(
        LocalDate initialDate,
        LocalDate finalDate
) {
}
