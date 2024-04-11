package org.yordanoffnikolay.betterminer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yordanoffnikolay.betterminer.models.Query;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
    Query findQueriesByInnName(String inn);
}
