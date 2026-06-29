package com.mecanicadm.mecanicadm_api.infra.features.workorder.config;

import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.port.StockMovementsGateway;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.DeductStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.SoftDeleteStockUseCase;
import com.mecanicadm.mecanicadm_api.core.vehicle.domain.port.VehicleGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderLaborItemGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderMaterialItemGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.AddLaborToWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.AddMaterialToWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.CalculateWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.CreateWorkOrderLaborItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.CreateWorkOrderMaterialItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.CreateWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.DecideWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.DeliverWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.DiagnoseWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.FinishLaborExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.FinishWorkOrderExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.GetAllWorkOrderExecutionTimeReportUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.GetAllWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.GetPrintableBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.GetWorkOrderByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.GetWorkOrderLaborItemByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.ManuallyAdjustWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.RecordWorkOrderPaymentUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.RemoveLaborItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.RemoveMaterialItemFromWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.SendWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.SoftDeleteWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.StartLaborExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.StartWorkOrderExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.UpdateWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.infra.pdf.PdfGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkOrderConfiguration {

    @Bean
    public CreateWorkOrderUseCase createWorkOrderUseCase(WorkOrderGateway gateway) {
        return new CreateWorkOrderUseCase(gateway);
    }

    @Bean
    public UpdateWorkOrderUseCase updateWorkOrderUseCase(WorkOrderGateway gateway) {
        return new UpdateWorkOrderUseCase(gateway);
    }

    @Bean
    public GetWorkOrderByIdUseCase getWorkOrderByIdUseCase(WorkOrderGateway gateway) {
        return new GetWorkOrderByIdUseCase(gateway);
    }

    @Bean
    public GetAllWorkOrderUseCase getAllWorkOrderUseCase(WorkOrderGateway gateway) {
        return new GetAllWorkOrderUseCase(gateway);
    }

    @Bean
    public SoftDeleteWorkOrderUseCase softDeleteWorkOrderUseCase(WorkOrderGateway gateway,
                                                                  WorkOrderMaterialItemGateway materialItemGateway,
                                                                  StockMovementsGateway stockMovementsGateway) {
        return new SoftDeleteWorkOrderUseCase(gateway, materialItemGateway, stockMovementsGateway);
    }

    @Bean
    public CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        return new CalculateWorkOrderBudgetUseCase(gateway);
    }

    @Bean
    public DiagnoseWorkOrderUseCase diagnoseWorkOrderUseCase(WorkOrderGateway gateway,
                                                             ClientGateway clientGateway,
                                                             VehicleGateway vehicleGateway,
                                                             CalculateWorkOrderBudgetUseCase calculateWorkOrderBudgetUseCase) {
        return new DiagnoseWorkOrderUseCase(gateway, clientGateway, vehicleGateway, calculateWorkOrderBudgetUseCase);
    }

    @Bean
    public DecideWorkOrderBudgetUseCase decideWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        return new DecideWorkOrderBudgetUseCase(gateway);
    }

    @Bean
    public CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase(LaborGateway laborGateway) {
        return new CreateWorkOrderLaborItemUseCase(laborGateway);
    }

    @Bean
    public CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase(MaterialGateway materialGateway,
                                                                                 DeductStockUseCase deductStockUseCase) {
        return new CreateWorkOrderMaterialItemUseCase(materialGateway, deductStockUseCase);
    }

    @Bean
    public AddLaborToWorkOrderUseCase addLaborToWorkOrderUseCase(WorkOrderGateway gateway,
                                                                  WorkOrderLaborItemGateway laborItemGateway,
                                                                  CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase) {
        return new AddLaborToWorkOrderUseCase(gateway, laborItemGateway, createWorkOrderLaborItemUseCase);
    }

    @Bean
    public AddMaterialToWorkOrderUseCase addMaterialToWorkOrderUseCase(WorkOrderGateway gateway,
                                                                        WorkOrderMaterialItemGateway materialItemGateway,
                                                                        CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase) {
        return new AddMaterialToWorkOrderUseCase(gateway, materialItemGateway, createWorkOrderMaterialItemUseCase);
    }

    @Bean
    public DeliverWorkOrderUseCase deliverWorkOrderUseCase(WorkOrderGateway gateway) {
        return new DeliverWorkOrderUseCase(gateway);
    }

    @Bean
    public FinishLaborExecutionUseCase finishLaborExecutionUseCase(WorkOrderGateway gateway,
                                                                   WorkOrderLaborItemGateway laborItemGateway) {
        return new FinishLaborExecutionUseCase(gateway, laborItemGateway);
    }

    @Bean
    public FinishWorkOrderExecutionUseCase finishWorkOrderExecutionUseCase(WorkOrderGateway gateway) {
        return new FinishWorkOrderExecutionUseCase(gateway);
    }

    @Bean
    public GetAllWorkOrderExecutionTimeReportUseCase getAllWorkOrderExecutionTimeReportUseCase(WorkOrderGateway gateway) {
        return new GetAllWorkOrderExecutionTimeReportUseCase(gateway);
    }

    @Bean
    public GetPrintableBudgetUseCase getPrintableBudgetUseCase(WorkOrderGateway workOrderGateway,
                                                               ClientGateway clientGateway,
                                                               VehicleGateway vehicleGateway,
                                                               LaborGateway laborGateway,
                                                               MaterialGateway materialGateway,
                                                               PdfGenerator pdfGenerator) {
        return new GetPrintableBudgetUseCase(workOrderGateway, clientGateway, vehicleGateway, laborGateway, materialGateway, pdfGenerator);
    }

    @Bean
    public GetWorkOrderLaborItemByIdUseCase getWorkOrderLaborItemByIdUseCase(WorkOrderGateway gateway) {
        return new GetWorkOrderLaborItemByIdUseCase(gateway);
    }

    @Bean
    public ManuallyAdjustWorkOrderBudgetUseCase manuallyAdjustWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        return new ManuallyAdjustWorkOrderBudgetUseCase(gateway);
    }

    @Bean
    public RecordWorkOrderPaymentUseCase recordWorkOrderPaymentUseCase(WorkOrderGateway gateway) {
        return new RecordWorkOrderPaymentUseCase(gateway);
    }

    @Bean
    public RemoveLaborItemFromWorkOrderUseCase removeLaborItemFromWorkOrderUseCase(WorkOrderLaborItemGateway gateway) {
        return new RemoveLaborItemFromWorkOrderUseCase(gateway);
    }

    @Bean
    public RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase(WorkOrderGateway workOrderGateway,
                                                                                          WorkOrderMaterialItemGateway materialItemGateway,
                                                                                          SoftDeleteStockUseCase softDeleteStockUseCase) {
        return new RemoveMaterialItemFromWorkOrderUseCase(workOrderGateway, materialItemGateway, softDeleteStockUseCase);
    }

    @Bean
    public SendWorkOrderBudgetUseCase sendWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        return new SendWorkOrderBudgetUseCase(gateway);
    }

    @Bean
    public StartLaborExecutionUseCase startLaborExecutionUseCase(WorkOrderGateway gateway,
                                                                 WorkOrderLaborItemGateway laborItemGateway) {
        return new StartLaborExecutionUseCase(gateway, laborItemGateway);
    }

    @Bean
    public StartWorkOrderExecutionUseCase startWorkOrderExecutionUseCase(WorkOrderGateway gateway) {
        return new StartWorkOrderExecutionUseCase(gateway);
    }
}
