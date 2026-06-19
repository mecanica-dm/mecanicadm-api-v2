package com.mecanicadm.mecanicadm_api.core.material.domain.port;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MaterialGateway {
    Material create(Material material);

    Material update(Material material);

    void deleteById(UUID id);

    Optional<Material> findById(UUID id);

    boolean existsById(UUID id);

    MaterialPageResult findAll(MaterialPageQuery query);

    List<Material> findAllByIds(@Param("ids") Set<UUID> ids);
}
