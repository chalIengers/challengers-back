package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClubRepository extends JpaRepository<UserClub,Long> {
}
