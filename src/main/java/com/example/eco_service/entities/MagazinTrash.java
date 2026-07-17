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
@Table(name = "MagazinTrash")
public class MagazinTrash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_magazin_trash;

    @ManyToOne
    @JoinColumn(name = "id_class_danger")
    private ClassDanger id_class_danger;

    @Column(nullable = false, unique = true, length = 8)
    private int code_trash;

    @Column(nullable = false, length = 255)
    private String name_trash;


}