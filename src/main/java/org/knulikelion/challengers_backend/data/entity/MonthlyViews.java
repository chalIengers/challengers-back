package org.knulikelion.challengers_backend.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.YearMonth;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "monthly_views")
public class MonthlyViews {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // 월간 조회가 여러번 일어날 수 있으므로 ManyToOne 관계 설정
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private YearMonth month;

    @Column(nullable = false)
    private Integer viewCount = 0;

}
