package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientGateway;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageQuery;
import com.mecanicadm.mecanicadm_api.core.client.domain.port.ClientPageResult;
import com.mecanicadm.mecanicadm_api.core.client.usecase.GetAllClientUseCase;
import com.mecanicadm.mecanicadm_api.core.client.usecase.query.GetAllClientQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllClientServiceTest {

    private ClientGateway repository;

    @Test
    @DisplayName("Deve listar os clientes com sucesso usando filtros e paginação")
    void shouldGetAllClientsSuccessfully() {
        repository = mock(ClientGateway.class);
        GetAllClientQuery query = new GetAllClientQuery(null, null, 0, 10, "name", "ASC");
        ClientPageResult expectedPage = new ClientPageResult(List.of(mock(Client.class)), 1);
        GetAllClientUseCase useCase = new GetAllClientUseCase(repository);

        when(repository.findAll(argThat(q -> q.page() == 0 && q.size() == 10 && q.sortBy().equals("name") && q.direction().equals("ASC")))).thenReturn(expectedPage);

        ClientPageResult result = useCase.execute(query);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        verify(repository).findAll(any(ClientPageQuery.class));
    }

    @Test
    @DisplayName("Deve aplicar filtro de documento quando fornecido na query")
    void shouldApplyDocumentFilter() {
        repository = mock(ClientGateway.class);
        GetAllClientQuery query = new GetAllClientQuery(null, "17871234053", 0, 10, "name", "ASC");
        ClientPageResult expectedPage = new ClientPageResult(List.of(mock(Client.class)), 1);
        GetAllClientUseCase useCase = new GetAllClientUseCase(repository);

        when(repository.findAll(any(ClientPageQuery.class))).thenReturn(expectedPage);

        ClientPageResult result = useCase.execute(query);

        assertNotNull(result);
        verify(repository).findAll(argThat(q -> "17871234053".equals(q.filter().document())));
    }

    @Test
    @DisplayName("Deve aplicar filtro de nome quando fornecido na query")
    void shouldApplyNameFilter() {
        repository = mock(ClientGateway.class);
        GetAllClientQuery query = new GetAllClientQuery("João", null, 0, 10, "name", "ASC");
        ClientPageResult expectedPage = new ClientPageResult(List.of(mock(Client.class)), 1);
        GetAllClientUseCase useCase = new GetAllClientUseCase(repository);

        when(repository.findAll(any(ClientPageQuery.class))).thenReturn(expectedPage);

        ClientPageResult result = useCase.execute(query);

        assertNotNull(result);
        verify(repository).findAll(argThat(q -> "João".equals(q.filter().name())));
    }
}
