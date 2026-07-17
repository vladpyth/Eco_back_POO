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
@Table(name = "NameDropAirTrash")
public class NameDropAirTrash {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_name_grope_air;

    @Column(nullable = false, length = 255)
    private String name_drop_air_trash;



}