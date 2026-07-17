package com.example.eco_service.entities;

import com.example.eco_service.dto.response.PhoneOnObjectResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "MagasinFactory")
public class MagasinFactory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_magasin_factory;

    @Column(nullable = false, unique = true, length = 10)
    private String id_registration;

    @Column()
    private LocalDate date_register;

    @ManyToOne
    @JoinColumn(name = "id_сities",nullable = true)
    private Cities id_cities;



    @ManyToOne
    @JoinColumn(name = "id_short_discribe_technology",nullable = true)// ne to
    private ShortDiscribeTechnology id_short_discribe_technology ;

    /**
     * Устаревшая «одна» технология на предприятие.
     * Основная связь M2M — через список {@link #technologies} (таблица Technology).
     */
    @ManyToOne
    @JoinColumn(name = "id_technology",nullable = true)// ne to
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Technology id_technology ;

    /** Промежуточные связи предприятие ↔ отход/состояние/класс (многие ко многим). */
    @OneToMany(mappedBy = "id_magasin_factory")
    @JsonIgnoreProperties({"id_magasin_factory"})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private List<Technology> technologies = new ArrayList<>();

    @OneToMany(mappedBy = "id_object_place_trash",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private List<NumberPhoneCount> numberPhoneCounts = new ArrayList<>();

    @Transient
    @JsonProperty("phones")
    public List<PhoneOnObjectResponse> getPhones() {
        if (numberPhoneCounts == null || numberPhoneCounts.isEmpty()) return new ArrayList<>();
        return numberPhoneCounts.stream()
                .filter(c -> c.getId_phone_number() != null)
                .map(PhoneOnObjectResponse::from)
                .toList();
    }


    @Column(nullable = false)
    private String name_obj;

    @Column(nullable = false)
    private String name_own;

    @Column(nullable = false)
    private String address_own;

    @Column(nullable = false)
    private String address_obj;

    @Column()
    private String develop_organization;

    @Column()
    private String confirmed_project;

    @Column()
    private LocalDate date_approve;

    @Column()
    private Boolean conclusion_documentation;

    @Column()
    private String act_use;

    @Column()
    private String requirements_acts;

    @Column()
    private Boolean obj_use_trash;

    @Column()
    private Boolean obj_accept_trash;

    @Column()
    private String character_prod;

    @Column()
    private String project_power_yer;

    @Column()
    private String project_power_hr;

    @Column()
    private String facticheskay_power;


    @Column()
    private String YNP;

    @Column()
    private int value;
}