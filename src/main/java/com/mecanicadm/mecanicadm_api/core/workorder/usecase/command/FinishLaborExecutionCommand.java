package com.mecanicadm.mecanicadm_api.core.workorder.usecase.command;

import java.util.UUID;

public record FinishLaborExecutionCommand(UUID workOrderId, UUID laborItemId) { }
