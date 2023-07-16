package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}
