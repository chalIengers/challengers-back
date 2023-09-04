package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ExtraUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtraUserMappingRepository extends JpaRepository<ExtraUserMapping, Long> {
    ExtraUserMapping getByUserId(Long id);
}
