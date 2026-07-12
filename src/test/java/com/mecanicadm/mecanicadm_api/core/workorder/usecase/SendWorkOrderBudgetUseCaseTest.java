package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.shared.domain.port.EmailAttachment;
import com.mecanicadm.mecanicadm_api.core.shared.domain.port.EmailService;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.BudgetDecisionToken;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.BudgetDecisionTokenGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.SendWorkOrderBudgetCommand;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.dto.PrintableBudgetResponse;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.query.GetPrintableBudgetQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendWorkOrderBudgetUseCaseTest {

    @Mock
    private WorkOrderGateway gateway;

    @Mock
    private BudgetDecisionTokenGateway tokenGateway;

    @Mock
    private ClientGateway clientGateway;

    @Mock
    private EmailService emailService;

    @Mock
    private GetPrintableBudgetUseCase getPrintableBudgetUseCase;

    @InjectMocks
    private SendWorkOrderBudgetUseCase useCase;

    @Test
    @DisplayName("Deve enviar o orçamento com sucesso")
    void shouldSendBudgetSuccessfully() {
        UUID workOrderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        String baseUrl = "http://localhost:8080";

        WorkOrder workOrder = mock(WorkOrder.class);
        WorkOrderBudget budget = mock(WorkOrderBudget.class);
        Client client = mock(Client.class);

        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.getBudget()).thenReturn(Optional.of(budget));
        when(workOrder.getId()).thenReturn(workOrderId);
        when(workOrder.getClientId()).thenReturn(clientId);
        when(workOrder.getVehicleId()).thenReturn(vehicleId.toString());
        when(workOrder.getDescription()).thenReturn("Troca de óleo");
        when(budget.getTotalPrice()).thenReturn(BigDecimal.valueOf(500));
        when(clientGateway.findById(clientId)).thenReturn(Optional.of(client));
        when(client.getEmail()).thenReturn("cliente@email.com");
        when(getPrintableBudgetUseCase.execute(any(GetPrintableBudgetQuery.class)))
                .thenReturn(new PrintableBudgetResponse("orcamento.pdf", java.util.Base64.getEncoder().encodeToString("pdf-content".getBytes())));

        useCase.execute(new SendWorkOrderBudgetCommand(workOrderId, baseUrl));

        verify(budget).send();
        verify(gateway).saveBudget(budget);
        verify(tokenGateway).create(any(BudgetDecisionToken.class));
        ArgumentCaptor<EmailAttachment> attachmentCaptor = ArgumentCaptor.forClass(EmailAttachment.class);
        verify(emailService).send(eq("cliente@email.com"), anyString(), anyString(), attachmentCaptor.capture());
        assertEquals("orcamento.pdf", attachmentCaptor.getValue().fileName());
    }

    @Test
    @DisplayName("Deve enviar o orçamento sem email quando cliente não possui email")
    void shouldSendBudgetWithoutEmailWhenClientHasNoEmail() {
        UUID workOrderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        String baseUrl = "http://localhost:8080";

        WorkOrder workOrder = mock(WorkOrder.class);
        WorkOrderBudget budget = mock(WorkOrderBudget.class);

        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.getBudget()).thenReturn(Optional.of(budget));
        when(workOrder.getId()).thenReturn(workOrderId);
        when(workOrder.getClientId()).thenReturn(clientId);
        when(clientGateway.findById(clientId)).thenReturn(Optional.empty());

        useCase.execute(new SendWorkOrderBudgetCommand(workOrderId, baseUrl));

        verify(budget).send();
        verify(gateway).saveBudget(budget);
        verify(tokenGateway).create(any(BudgetDecisionToken.class));
        verify(emailService, never()).send(anyString(), anyString(), anyString(), any(EmailAttachment.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o orçamento não for encontrado")
    void shouldThrowExceptionWhenBudgetNotFound() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = mock(WorkOrder.class);
        when(gateway.findById(workOrderId)).thenReturn(Optional.of(workOrder));
        when(workOrder.getBudget()).thenReturn(Optional.empty());

        assertThrows(WorkOrderExceptions.BudgetNotFound.class,
                () -> useCase.execute(new SendWorkOrderBudgetCommand(workOrderId, "http://localhost:8080")));

        verify(gateway, never()).saveBudget(any());
        verify(tokenGateway, never()).create(any());
    }
}
