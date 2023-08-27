package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.MonthlyViews;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MonthlyViewsRepository extends JpaRepository<MonthlyViews, Long> {

    Optional<MonthlyViews> findByProjectAndMonth(Project project, YearMonth month);

    List<MonthlyViews> findTop6ByMonthOrderByViewCountDesc(YearMonth month);
}
