package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.SendWorkOrderBudgetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendWorkOrderBudgetUseCase implements VoidUseCase<SendWorkOrderBudgetCommand> {
    private static final Logger logger = LoggerFactory.getLogger(SendWorkOrderBudgetUseCase.class);

    private static final String BUDGET_SENT_LOG_MESSAGE = """
            ==========================================================
            ORÇAMENTO ENVIADO PARA O CLIENTE
            Ordem de Serviço ID: {}
            Cliente ID: {}
            Veículo ID: {}
            Valor Total: {}
            Status do Orçamento: {}
            ==========================================================
            """;

    private final WorkOrderGateway gateway;

    public SendWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(SendWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getBudget().ifPresentOrElse(
                budget -> {
                    budget.send();
                    gateway.saveBudget(budget);

                    logger.info(BUDGET_SENT_LOG_MESSAGE,
                            workOrder.getId(),
                            workOrder.getClientId(),
                            workOrder.getVehicleId(),
                            budget.getTotalPrice(),
                            budget.getStatus());
                },
                () -> {
                    throw new WorkOrderExceptions.BudgetNotFound();
                }
        );
    }
}
