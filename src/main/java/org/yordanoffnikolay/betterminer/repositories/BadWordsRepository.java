package org.yordanoffnikolay.betterminer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yordanoffnikolay.betterminer.models.BadWords;

@Repository
public interface BadWordsRepository extends JpaRepository<BadWords, Long> {
}
