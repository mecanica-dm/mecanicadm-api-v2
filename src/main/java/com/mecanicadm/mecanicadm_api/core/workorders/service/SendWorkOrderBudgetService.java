package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.SendWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SendWorkOrderBudgetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SendWorkOrderBudgetService implements SendWorkOrderBudgetUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SendWorkOrderBudgetService.class);

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

    private final WorkOrderRepository workOrderRepository;

    public SendWorkOrderBudgetService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(SendWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getBudget().ifPresentOrElse(
                budget -> {
                    budget.send();
                    workOrderRepository.save(workOrder);

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