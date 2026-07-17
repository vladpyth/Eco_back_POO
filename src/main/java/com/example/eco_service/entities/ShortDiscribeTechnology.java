package com.example.eco_service.entities;

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
@Table(name = "ShortDiscribeTechnology")
public class ShortDiscribeTechnology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_short_discribe_technology;

    @Column(nullable = false, unique = true, length = 255)
    private String technology;
}
