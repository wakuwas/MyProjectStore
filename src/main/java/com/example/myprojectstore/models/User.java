package com.example.myprojectstore.models;

import com.example.myprojectstore.models.enums.Role;
import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;


    @Column(name = "email",unique = true)
    @Email(message = "некорректно введен email")
    @NotEmpty(message = "Необходимо ввести email")
    private String email;

    @Size(min = 11,max=11, message = "Номер должен состоять из 11 цифр")
    @Column(name = "phone")
    private String phone;

    @NotEmpty(message = "Необходимо ввести имя")
    @Column(name = "name")
    private String name;


    @Column(name = "active")
    private boolean active;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;


    @Size(min = 6 ,message = "Пароль должен содержать не менее 6 символов")
    @Column(name = "password" ,length = 1000)
    private String password;

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
    private List<Product> products = new ArrayList<>();


    public boolean isAdmin(){
        return roles.contains(Role.ROLE_ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


}
