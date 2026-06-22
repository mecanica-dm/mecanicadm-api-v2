package com.mecanicadm.mecanicadm_api.infra.features.user.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.user.domain.PasswordResetToken;
import com.mecanicadm.mecanicadm_api.core.user.domain.User;
import com.mecanicadm.mecanicadm_api.core.user.domain.port.PasswordResetTokenGateway;
import com.mecanicadm.mecanicadm_api.shared.exception.TechnicalException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenGateway {

    private final PasswordResetTokenJpaRepository jpaRepository;

    public PasswordResetTokenRepositoryImpl(PasswordResetTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void deleteByUser(User user) {
        if (isNull(user)) {
            throw new TechnicalException("error.technical.entity.null", "User", "exclusão");
        }
        jpaRepository.deleteByUser(UserJpaMapper.toEntity(user));
    }

    @Override
    public void save(PasswordResetToken token) {
        if (isNull(token)) {
            throw new TechnicalException("error.technical.entity.null", "PasswordResetToken", "salvar");
        }
        jpaRepository.save(PasswordResetTokenJpaMapper.toEntity(token));
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(PasswordResetTokenJpaMapper::toDomain);
    }

    @Override
    public void delete(PasswordResetToken token) {
        if (isNull(token)) {
            throw new TechnicalException("error.technical.entity.null", "PasswordResetToken", "exclusão");
        }
        jpaRepository.delete(PasswordResetTokenJpaMapper.toEntity(token));
    }
}
