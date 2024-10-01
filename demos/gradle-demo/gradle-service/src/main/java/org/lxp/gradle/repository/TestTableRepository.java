package org.lxp.gradle.repository;

import org.lxp.gradle.entity.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTableRepository extends JpaRepository<TestTable, Integer>, JpaSpecificationExecutor<TestTable> {

}
