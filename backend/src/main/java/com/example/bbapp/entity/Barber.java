package com.example.bbapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "barber")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barberId;

    @Column(name = "barber_name", nullable = false)
    private String barberName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "barber", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    //cascade -> whenever save a barber, we save and its role (to child)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "barber_role",
            joinColumns = @JoinColumn(name = "barber_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private Set<Role> roles;
}
