package com.mecanicadm.mecanicadm_api.core.workorder.usecase.command;

import java.util.UUID;

public record StartLaborExecutionCommand(UUID workOrderId, UUID laborItemId) { }
