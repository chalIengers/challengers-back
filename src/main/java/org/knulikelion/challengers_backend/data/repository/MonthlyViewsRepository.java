package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.MonthlyViews;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.expression.spel.ast.OpAnd;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MonthlyViewsRepository extends JpaRepository<MonthlyViews, Long>, JpaSpecificationExecutor<MonthlyViews> {

    Optional<MonthlyViews> findByProjectAndMonth(Project project, YearMonth month);

    Page<MonthlyViews> findByMonthOrderByViewCountDesc(YearMonth month, Pageable pageable);
    MonthlyViews findByProject(Project project);
}
