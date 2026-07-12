package com.mecanicadm.mecanicadm_api.infra.features.workorder.api.renderer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetDecisionPageRendererTest {

    @Test
    @DisplayName("Deve renderizar pagina de formulario para aprovacao")
    void shouldRenderFormPageForApproval() {
        String html = BudgetDecisionPageRenderer.formPage("Aprovar Orçamento", "token-123", "APPROVED", "Observações (opcional):", false);

        assertNotNull(html);
        assertTrue(html.contains("<title>Aprovar Orçamento</title>"));
        assertTrue(html.contains("token-123"));
        assertTrue(html.contains("value=\"APPROVED\""));
        assertTrue(html.contains("Observações (opcional):"));
        assertFalse(html.contains("required"));
        assertTrue(html.contains("MecânicaDM"));
    }

    @Test
    @DisplayName("Deve renderizar pagina de formulario para rejeicao com required")
    void shouldRenderFormPageForRejectionWithRequired() {
        String html = BudgetDecisionPageRenderer.formPage("Rejeitar Orçamento", "token-456", "REJECTED", "Informe o motivo da rejeição:", true);

        assertNotNull(html);
        assertTrue(html.contains("<title>Rejeitar Orçamento</title>"));
        assertTrue(html.contains("token-456"));
        assertTrue(html.contains("value=\"REJECTED\""));
        assertTrue(html.contains("Informe o motivo da rejeição:"));
        assertTrue(html.contains("required"));
    }

    @Test
    @DisplayName("Deve renderizar pagina de formulario para solicitacao de alteracoes")
    void shouldRenderFormPageForChangesRequested() {
        String html = BudgetDecisionPageRenderer.formPage("Solicitar Alterações", "token-789", "CHANGES_REQUESTED", "Descreva as alterações solicitadas:", true);

        assertNotNull(html);
        assertTrue(html.contains("<title>Solicitar Alterações</title>"));
        assertTrue(html.contains("token-789"));
        assertTrue(html.contains("value=\"CHANGES_REQUESTED\""));
        assertTrue(html.contains("Descreva as alterações solicitadas:"));
        assertTrue(html.contains("required"));
    }

    @Test
    @DisplayName("Deve renderizar pagina de sucesso para aprovacao")
    void shouldRenderSuccessPageForApproval() {
        String html = BudgetDecisionPageRenderer.successPage("APPROVED");

        assertNotNull(html);
        assertTrue(html.contains("Resposta Registrada"));
        assertTrue(html.contains("Orçamento aprovado com sucesso!"));
        assertTrue(html.contains("MecânicaDM"));
    }

    @Test
    @DisplayName("Deve renderizar pagina de sucesso para rejeicao")
    void shouldRenderSuccessPageForRejection() {
        String html = BudgetDecisionPageRenderer.successPage("REJECTED");

        assertNotNull(html);
        assertTrue(html.contains("Orçamento rejeitado. Obrigado pela resposta."));
    }

    @Test
    @DisplayName("Deve renderizar pagina de sucesso para solicitacao de alteracoes")
    void shouldRenderSuccessPageForChangesRequested() {
        String html = BudgetDecisionPageRenderer.successPage("CHANGES_REQUESTED");

        assertNotNull(html);
        assertTrue(html.contains("Solicitação de alterações enviada com sucesso!"));
    }

    @Test
    @DisplayName("Deve renderizar pagina de sucesso para acao desconhecida")
    void shouldRenderSuccessPageForUnknownAction() {
        String html = BudgetDecisionPageRenderer.successPage("UNKNOWN");

        assertNotNull(html);
        assertTrue(html.contains("Resposta registrada com sucesso!"));
    }

    @Test
    @DisplayName("Deve renderizar pagina de erro")
    void shouldRenderErrorPage() {
        String html = BudgetDecisionPageRenderer.errorPage("Link já utilizado", "Este link já foi utilizado.");

        assertNotNull(html);
        assertTrue(html.contains("<title>Link já utilizado</title>"));
        assertTrue(html.contains("Este link já foi utilizado."));
        assertTrue(html.contains("MecânicaDM"));
    }
}
