package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findAllByClubManager(User user);
}
