package com.mecanicadm.mecanicadm_api.core.client.adapter.repository.specification;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ClientSpecificationTest {

    @Test
    @DisplayName("hasName deve retornar null quando nome for nulo ou vazio")
    void hasNameShouldReturnNullWhenNameIsInvalid() {
        Root<Client> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(ClientSpecification.hasName(null).toPredicate(root, query, cb));
        assertNull(ClientSpecification.hasName("").toPredicate(root, query, cb));
    }

    @Test
    @DisplayName("hasName deve retornar predicado quando nome for preenchido")
    void hasNameShouldReturnPredicateWhenNameIsValid() {
        Root<Client> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<String> path = mock(Path.class);
        Expression<String> lowerExpression = mock(Expression.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("name")).thenReturn(path);
        when(cb.lower(path)).thenReturn(lowerExpression);
        when(cb.like(lowerExpression, "%diego%")).thenReturn(predicate);

        assertNotNull(ClientSpecification.hasName("Diego").toPredicate(root, query, cb));
        verify(cb).like(lowerExpression, "%diego%");
    }

    @Test
    @DisplayName("hasDocument deve retornar null quando Documento for nulo ou vazio")
    void hasDocumentShouldReturnNullWhenDocumentIsInvalid() {
        Root<Client> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(ClientSpecification.hasDocument(null).toPredicate(root, query, cb));
        assertNull(ClientSpecification.hasDocument("").toPredicate(root, query, cb));
    }

    @Test
    @DisplayName("hasDocument deve retornar predicado quando Documento for preenchido")
    void hasDocumentShouldReturnPredicateWhenDocumentIsValid() {
        Root<Client> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<String> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("document")).thenReturn(path);
        when(cb.equal(path, "12345678901")).thenReturn(predicate);

        assertNotNull(ClientSpecification.hasDocument("12345678901").toPredicate(root, query, cb));
        verify(cb).equal(path, "12345678901");
    }
}
