package com.example.eco_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "NumberPhone")
public class NumberPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_phone_number;

    @OneToMany(mappedBy = "id_phone_number", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private java.util.List<NumberPhoneCount> numberPhoneCounts;

    @Column(nullable = false, unique = true, length = 255)
    private String number;
}