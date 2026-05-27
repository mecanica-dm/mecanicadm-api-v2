package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DeliverWorkOrderCommand;

public interface DeliverWorkOrderUseCase {
    void handle(DeliverWorkOrderCommand cmd);
}
