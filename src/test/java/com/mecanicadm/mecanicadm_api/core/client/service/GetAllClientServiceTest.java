package com.mecanicadm.mecanicadm_api.core.client.service;

import com.mecanicadm.mecanicadm_api.core.client.adapter.api.dto.ClientResponse;
import com.mecanicadm.mecanicadm_api.core.client.adapter.repository.ClientRepository;
import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.core.client.usecase.query.GetAllClientQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private GetAllClientService getAllClientService;

    @Test
    @DisplayName("Deve listar os clientes com sucesso usando filtros e paginação")
    void shouldGetAllClientsSuccessfully() {
        String name = "Test";
        String document = "17871234053";
        Pageable pageable = mock(Pageable.class);
        GetAllClientQuery query = new GetAllClientQuery(name, document, pageable);

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(UUID.randomUUID());
        when(client.getName()).thenReturn("Test Name");
        when(client.getEmail()).thenReturn("test@example.com");
        when(client.getDocument()).thenReturn(document);
        when(client.getPhone()).thenReturn("48999999999");

        Page<Client> pageResult = new PageImpl<>(List.of(client));
        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageResult);

        Page<ClientResponse> result = getAllClientService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Name", result.getContent().get(0).name());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve listar clientes com sucesso quando Documento é nulo")
    void shouldGetAllClientsSuccessfullyWhenDocumentIsNull() {
        String name = "Test";
        String document = null;
        Pageable pageable = mock(Pageable.class);
        GetAllClientQuery query = new GetAllClientQuery(name, document, pageable);

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(UUID.randomUUID());
        when(client.getName()).thenReturn("Test Name");
        when(client.getEmail()).thenReturn("test@example.com");
        when(client.getDocument()).thenReturn("17871234053");
        when(client.getPhone()).thenReturn("48999999999");

        Page<Client> pageResult = new PageImpl<>(List.of(client));
        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageResult);

        Page<ClientResponse> result = getAllClientService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Name", result.getContent().get(0).name());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve listar clientes com sucesso quando Documento é vazio")
    void shouldGetAllClientsSuccessfullyWhenDocumentIsEmpty() {
        String name = "Test";
        String document = "";
        Pageable pageable = mock(Pageable.class);
        GetAllClientQuery query = new GetAllClientQuery(name, document, pageable);

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(UUID.randomUUID());
        when(client.getName()).thenReturn("Test Name");
        when(client.getEmail()).thenReturn("test@example.com");
        when(client.getDocument()).thenReturn("17871234053");
        when(client.getPhone()).thenReturn("48999999999");

        Page<Client> pageResult = new PageImpl<>(List.of(client));
        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageResult);

        Page<ClientResponse> result = getAllClientService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Name", result.getContent().get(0).name());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}
