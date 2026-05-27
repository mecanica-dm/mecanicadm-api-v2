package com.mecanicadm.mecanicadm_api.core.user.adapter.api.openapi;

import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.AuthenticationResponse;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.ForgotPasswordRequest;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.ResetPasswordRequest;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.UserResponse;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.CreateUserCommand;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.UpdateUserCommand;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.AuthenticateUserQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e autenticação")
public interface UserOpenApi {

    @Operation(summary = "Autenticar usuário", description = "Realiza o login do usuário e retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    ResponseEntity<AuthenticationResponse> login(AuthenticateUserQuery query);

    @Operation(summary = "Solicitar recuperação de senha", description = "Envia um e-mail com link para redefinição de senha")
    @ApiResponse(responseCode = "200", description = "E-mail de recuperação enviado")
    ResponseEntity<Void> forgotPassword(ForgotPasswordRequest request);

    @Operation(summary = "Redefinir senha", description = "Altera a senha do usuário utilizando o token enviado por e-mail")
    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso")
    ResponseEntity<Void> resetPassword(ResetPasswordRequest request);

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    ResponseEntity<UserResponse> findUser(UUID id);

    @Operation(summary = "Criar novo usuário", description = "Cadastra um novo usuário no sistema")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    ResponseEntity<UUID> create(CreateUserCommand cmd);

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    ResponseEntity<Void> update(UpdateUserCommand cmd, User user);

    @Operation(summary = "Excluir usuário", description = "Realiza a exclusão lógica do usuário autenticado")
    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso")
    ResponseEntity<Void> deleteUser(User user);
}
