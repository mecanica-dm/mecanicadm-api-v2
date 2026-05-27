package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RecordWorkOrderPaymentCommand;

public interface RecordWorkOrderPaymentUseCase {
    void handle(RecordWorkOrderPaymentCommand cmd);
}
