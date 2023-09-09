package org.knulikelion.challengers_backend.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "clubs")
@Builder
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {
    private static final long serialVersionUID = 6014984039564979072L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String userName;

    @JsonProperty(access = Access.WRITE_ONLY) /*Json 결과 출력 x*/
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String email; /*서비스 내에서 사용할 uid*/

    @Column
    @ColumnDefault("true")
    private boolean useAble;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>(); /*default 설정 속성 값*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { /*GrantedAuthority 객체 컬렉션으로 변환, @return 계정이 가지고 있는 권한*/
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserClub> clubs = new ArrayList<>();

    @Override
    public String getUsername() {
        return this.email;
    }
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() { /*계정이 만료안됐냐? true -> 만료 안됨.*/
        return true;
    }
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() { /*계정이 잠겨있냐? true -> 안잠김*/
        return true;
    }
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() { /*비밀번호 만료 안됐냐? true -> 만료 안됨.*/
        return true;
    }
    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() { /*계정이 활성화? true -> 활성화*/
        return true;
    }

    public String getUserName() {
        return this.userName;
    }
}
