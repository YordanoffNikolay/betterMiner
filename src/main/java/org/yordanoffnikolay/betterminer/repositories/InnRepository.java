package org.yordanoffnikolay.betterminer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yordanoffnikolay.betterminer.models.Inn;

@Repository
public interface InnRepository extends JpaRepository<Inn, Long> {

    Inn findByName(String name);
}
