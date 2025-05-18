package com.saurabh.securenotesapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name", length = 20)
    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private AppRole roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonBackReference
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
