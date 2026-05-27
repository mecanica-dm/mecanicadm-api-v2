package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.dto.PrintableBudgetResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetPrintableBudgetQuery;

public interface GetPrintableBudgetUseCase {
    PrintableBudgetResponse handle(GetPrintableBudgetQuery query);
}
