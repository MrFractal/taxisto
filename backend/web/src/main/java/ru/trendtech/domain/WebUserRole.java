package ru.trendtech.domain;

import ru.trendtech.domain.admin.WebUser;

import javax.persistence.*;

/**
 * Created by petr on 12.05.2015.
 */
@Entity
@Table(name = "web_user_role")
public class WebUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private WebUser webUser;

    @OneToOne
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
