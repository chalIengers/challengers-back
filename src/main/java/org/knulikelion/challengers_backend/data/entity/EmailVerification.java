package org.knulikelion.challengers_backend.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; /*사용자 이메일*/

    private String code; /*메일 인증 번호*/

    private LocalDateTime expireTime; /*인증 번호 만료 시간*/
}
