package com.mecanicadm.mecanicadm_api.core.user.domain.port;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    User create(User user);

    User update(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    void save(User user);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    UserPageResult findAll(UserPageQuery query);
}
