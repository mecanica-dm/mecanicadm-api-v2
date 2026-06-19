package com.mecanicadm.mecanicadm_api.infra.features.labor.config;

import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.CreateLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.DeleteLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetAllLaborsUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetAllLaborExecutionTimeReportUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.GetLaborByIdUseCase;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.UpdateLaborUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderLaborItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaborConfiguration {

    @Bean
    public CreateLaborUseCase createLaborUseCase(LaborGateway gateway) {
        return new CreateLaborUseCase(gateway);
    }

    @Bean
    public UpdateLaborUseCase updateLaborUseCase(LaborGateway gateway) {
        return new UpdateLaborUseCase(gateway);
    }

    @Bean
    public DeleteLaborUseCase deleteLaborUseCase(LaborGateway gateway) {
        return new DeleteLaborUseCase(gateway);
    }

    @Bean
    public GetLaborByIdUseCase getLaborByIdUseCase(LaborGateway gateway) {
        return new GetLaborByIdUseCase(gateway);
    }

    @Bean
    public GetAllLaborsUseCase getAllLaborsUseCase(LaborGateway gateway) {
        return new GetAllLaborsUseCase(gateway);
    }

    @Bean
    public GetAllLaborExecutionTimeReportUseCase getAllLaborExecutionTimeReportUseCase(WorkOrderLaborItemRepository workOrderLaborItemRepository) {
        return new GetAllLaborExecutionTimeReportUseCase(workOrderLaborItemRepository);
    }
}

