package com.mecanicadm.mecanicadm_api.core.user.adapter.api;

import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.AuthenticationResponse;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.ForgotPasswordRequest;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.ResetPasswordRequest;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.dto.UserResponse;
import com.mecanicadm.mecanicadm_api.core.user.adapter.api.openapi.UserOpenApi;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.usecase.*;
import com.mecanicadm.mecanicadm_api.core.user.usecase.command.*;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.AuthenticateUserQuery;
import com.mecanicadm.mecanicadm_api.core.user.usecase.query.FindUserByIdQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController implements UserOpenApi {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final SoftDeleteUserUseCase softDeleteUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @Autowired
    public UserController(CreateUserUseCase createUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase,
                          SoftDeleteUserUseCase softDeleteUserUseCase,
                          GetUserByIdUseCase getUserByIdUseCase,
                          RequestPasswordResetUseCase requestPasswordResetUseCase,
                          ResetPasswordUseCase resetPasswordUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.softDeleteUserUseCase = softDeleteUserUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.requestPasswordResetUseCase = requestPasswordResetUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticateUserQuery query) {
        AuthenticationResponse response = authenticateUserUseCase.handle(query);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        requestPasswordResetUseCase.handle(new RequestPasswordResetCommand(request.email()));
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        resetPasswordUseCase.handle(new ResetPasswordCommand(request.token(), request.newPassword()));
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUser(@PathVariable UUID id) {
        User user = getUserByIdUseCase.handle(new FindUserByIdQuery(id));
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @Override
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody @Valid CreateUserCommand cmd) {
        UUID userId = createUserUseCase.handle(cmd);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userId).toUri();
        return ResponseEntity.created(uri).body(userId);
    }

    @Override
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateUserCommand cmd, @AuthenticationPrincipal User user) {
        updateUserUseCase.handle(cmd.withId(user.getId()));
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user) {
        softDeleteUserUseCase.handle(new SoftDeleteUserCommand(user.getId()));
        return ResponseEntity.noContent().build();
    }
}