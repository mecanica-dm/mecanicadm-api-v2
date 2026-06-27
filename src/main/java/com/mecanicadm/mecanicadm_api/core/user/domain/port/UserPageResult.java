package com.mecanicadm.mecanicadm_api.core.user.domain.port;

import com.mecanicadm.mecanicadm_api.core.user.domain.User;

import java.util.List;

public record UserPageResult(List<User> items, long totalElements) {
}
