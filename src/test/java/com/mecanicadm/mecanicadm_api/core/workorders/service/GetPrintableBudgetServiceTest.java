package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.Vehicle;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.dto.PrintableBudgetDTO;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.dto.PrintableBudgetResponse;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.query.GetPrintableBudgetQuery;
import com.mecanicadm.mecanicadm_api.infra.pdf.PdfGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetPrintableBudgetServiceTest {

    private WorkOrderRepository workOrderRepository;
    private ClientGateway clientRepository;
    private VehicleGateway vehicleRepository;
    private LaborGateway laborGateway;
    private MaterialGateway materialGateway;
    private PdfGenerator pdfGenerator;

    private GetPrintableBudgetService getPrintableBudgetService;

    @BeforeEach
    void setUp() {
        workOrderRepository = mock(WorkOrderRepository.class);
        clientRepository = mock(ClientGateway.class);
        vehicleRepository = mock(VehicleGateway.class);
        laborGateway = mock(LaborGateway.class);
        materialGateway = mock(MaterialGateway.class);
        pdfGenerator = mock(PdfGenerator.class);
        getPrintableBudgetService = new GetPrintableBudgetService(workOrderRepository, clientRepository, vehicleRepository, laborGateway, materialGateway, pdfGenerator);
    }

    @Test
    @DisplayName("Deve retornar os dados do orcamento em Base64 formatando CPF e telefone 11 digitos")
    void shouldReturnPrintableBudgetBase64WithCPF() {
        UUID workOrderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        String vehicleId = "ABC1234";

        WorkOrder workOrder = WorkOrder.create(clientId, vehicleId, "Teste CPF");

        Client client = mock(Client.class);
        when(client.getName()).thenReturn("Cliente Teste");
        when(client.getDocument()).thenReturn("12345678901");
        when(client.getPhone()).thenReturn("48999999999");

        Vehicle vehicle = mock(Vehicle.class);
        when(vehicle.getLicensePlate()).thenReturn(vehicleId);

        when(workOrderRepository.findByIdWithItems(any())).thenReturn(Optional.of(workOrder));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(vehicle));

        byte[] fakePdfBytes = "fake-pdf-content".getBytes();

        ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
        when(pdfGenerator.generatePdfFromHtml(eq("budget-template"), variablesCaptor.capture())).thenReturn(fakePdfBytes);

        GetPrintableBudgetQuery query = new GetPrintableBudgetQuery(workOrderId);
        PrintableBudgetResponse result = getPrintableBudgetService.handle(query);

        assertNotNull(result);

        Map<String, Object> capturedVars = variablesCaptor.getValue();
        PrintableBudgetDTO capturedBudget = (PrintableBudgetDTO) capturedVars.get("budget");

        assertEquals("123.456.789-01", capturedBudget.client().document());
        assertEquals("(48) 99999-9999", capturedBudget.client().phone());
    }

    @Test
    @DisplayName("Deve retornar os dados do orcamento formatando CNPJ e telefone 10 digitos")
    void shouldReturnPrintableBudgetBase64WithCNPJ() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC1234", "Teste CNPJ");

        Client client = mock(Client.class);
        when(client.getDocument()).thenReturn("12345678000199");
        when(client.getPhone()).thenReturn("4833334444");

        Vehicle vehicle = mock(Vehicle.class);

        when(workOrderRepository.findByIdWithItems(any())).thenReturn(Optional.of(workOrder));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(vehicle));
        when(pdfGenerator.generatePdfFromHtml(anyString(), anyMap())).thenReturn(new byte[]{});

        ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
        when(pdfGenerator.generatePdfFromHtml(anyString(), variablesCaptor.capture())).thenReturn(new byte[]{});

        getPrintableBudgetService.handle(new GetPrintableBudgetQuery(workOrderId));

        PrintableBudgetDTO capturedBudget = (PrintableBudgetDTO) variablesCaptor.getValue().get("budget");
        assertEquals("12.345.678/0001-99", capturedBudget.client().document());
        assertEquals("(48) 3333-4444", capturedBudget.client().phone());
    }

    @Test
    @DisplayName("Deve retornar documento e telefone vazios/sem formato caso o formato nao seja o padrao")
    void shouldReturnDocumentAndPhoneUnformattedOrEmpty() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC1234", "Teste Bad Format");

        Client client = mock(Client.class);
        when(client.getDocument()).thenReturn("12345"); // neither 11 nor 14
        when(client.getPhone()).thenReturn("123"); // neither 10 nor 11

        Vehicle vehicle = mock(Vehicle.class);

        when(workOrderRepository.findByIdWithItems(any())).thenReturn(Optional.of(workOrder));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(vehicle));

        ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
        when(pdfGenerator.generatePdfFromHtml(anyString(), variablesCaptor.capture())).thenReturn(new byte[]{});

        getPrintableBudgetService.handle(new GetPrintableBudgetQuery(workOrderId));

        PrintableBudgetDTO capturedBudget = (PrintableBudgetDTO) variablesCaptor.getValue().get("budget");
        assertEquals("12345", capturedBudget.client().document());
        assertEquals("123", capturedBudget.client().phone());
    }

    @Test
    @DisplayName("Deve retornar vazio se documento e telefone forem nulos")
    void shouldReturnEmptyIfDocumentAndPhoneNull() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC1234", "Teste Null");

        Client client = mock(Client.class);
        when(client.getDocument()).thenReturn(null);
        when(client.getPhone()).thenReturn(null);

        Vehicle vehicle = mock(Vehicle.class);

        when(workOrderRepository.findByIdWithItems(any())).thenReturn(Optional.of(workOrder));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(vehicle));

        ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
        when(pdfGenerator.generatePdfFromHtml(anyString(), variablesCaptor.capture())).thenReturn(new byte[]{});

        getPrintableBudgetService.handle(new GetPrintableBudgetQuery(workOrderId));

        PrintableBudgetDTO capturedBudget = (PrintableBudgetDTO) variablesCaptor.getValue().get("budget");
        assertEquals("", capturedBudget.client().document());
        assertEquals("", capturedBudget.client().phone());
    }

    @Test
    @DisplayName("Deve lancar erro se work order nao existir")
    void shouldThrowExceptionIfWorkOrderNotFound() {
        when(workOrderRepository.findByIdWithItems(any())).thenReturn(Optional.empty());
        GetPrintableBudgetQuery query = new GetPrintableBudgetQuery(UUID.randomUUID());

        assertThrows(WorkOrderExceptions.NotFound.class, () -> getPrintableBudgetService.handle(query));
    }

    @Test
    @DisplayName("Deve calcular totais baseando em materiais e servicos corretamente")
    void shouldCalculateTotalsCorrectly() {
        UUID workOrderId = UUID.randomUUID();
        WorkOrder workOrder = WorkOrder.create(UUID.randomUUID(), "ABC", "Desc");

        UUID laborId = UUID.randomUUID();
        WorkOrderLaborItem woli = mock(WorkOrderLaborItem.class);
        when(woli.getLaborId()).thenReturn(laborId);

        Set<WorkOrderLaborItem> laborItems = new HashSet<>();
        laborItems.add(woli);
        ReflectionTestUtils.setField(workOrder, "laborItems", laborItems);

        UUID materialId = UUID.randomUUID();
        WorkOrderMaterialItem womi = mock(WorkOrderMaterialItem.class);
        when(womi.getMaterialId()).thenReturn(materialId);
        when(womi.getQuantity()).thenReturn(2);

        Set<WorkOrderMaterialItem> materialItems = new HashSet<>();
        materialItems.add(womi);
        ReflectionTestUtils.setField(workOrder, "materialItems", materialItems);

        Client client = mock(Client.class);
        Vehicle vehicle = mock(Vehicle.class);
        Labor labor = mock(Labor.class);
        when(labor.getId()).thenReturn(laborId);
        when(labor.getPrice()).thenReturn(new BigDecimal("100.00"));

        Material material = mock(Material.class);
        when(material.getId()).thenReturn(materialId);
        when(material.getPrice()).thenReturn(new BigDecimal("50.00"));

        when(workOrderRepository.findByIdWithItems(any())).thenReturn(Optional.of(workOrder));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(vehicleRepository.findByLicensePlate(any())).thenReturn(Optional.of(vehicle));
        when(laborGateway.findAllByIds(any())).thenReturn(List.of(labor));
        when(materialGateway.findAllByIds(any())).thenReturn(List.of(material));

        ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
        when(pdfGenerator.generatePdfFromHtml(anyString(), variablesCaptor.capture())).thenReturn(new byte[]{});

        getPrintableBudgetService.handle(new GetPrintableBudgetQuery(workOrderId));

        PrintableBudgetDTO capturedBudget = (PrintableBudgetDTO) variablesCaptor.getValue().get("budget");
        assertEquals(0, new BigDecimal("100.00").compareTo(capturedBudget.totalLaborPrice()));
        assertEquals(0, new BigDecimal("100.00").compareTo(capturedBudget.totalMaterialPrice())); // 2 * 50
        assertEquals(0, new BigDecimal("200.00").compareTo(capturedBudget.totalBudgetPrice()));
    }
}
