package org.lxp.jpa.repository;

import org.lxp.jpa.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer>, JpaSpecificationExecutor<Password> {

}
