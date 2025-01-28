package com.learning.__security.persistence.repositories;

import com.learning.__security.persistence.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleName);
}
