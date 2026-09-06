package com.example.eco_service.dto.response;

import com.example.eco_service.entities.*;

import java.time.LocalDate;

/** Lightweight response models used only by paginated table endpoints. */
public final class ListDtos {

    private ListDtos() {}

    public record ClassDangerDto(Long id_class_danger, int class_danger) {}
    public record RegionDto(Long id_region, String name_region) {}
    public record DistrictDto(Long id_district, String name_district) {}
    public record CitiesDto(
            Long id_cities,
            RegionDto id_region,
            String index,
            DistrictDto id_district,
            String name_cities) {}
    public record ShortTechnologyDto(Long id_short_discribe_technology, String technology) {}
    public record MagazinTrashDto(
            Long id_magazin_trash,
            ClassDangerDto id_class_danger,
            int code_trash,
            String name_trash) {}
    public record PhysStateTrashDto(Long id_mame_group, String name_group) {}
    public record DropAirNameDto(Long id_name_grope_air, String name_drop_air_trash) {}
    public record FactoryRefDto(Long id_magasin_factory, String id_registration, String name_obj) {}

    public record MagasinFactoryListDto(
            Long id_magasin_factory,
            String id_registration,
            LocalDate date_register,
            CitiesDto id_cities,
            ShortTechnologyDto id_short_discribe_technology,
            String name_obj,
            String name_own,
            String address_own,
            String address_obj,
            String develop_organization,
            String confirmed_project,
            LocalDate date_approve,
            Boolean conclusion_documentation,
            String act_use,
            String requirements_acts,
            Boolean obj_use_trash,
            Boolean obj_accept_trash,
            String character_prod,
            String project_power_yer,
            String project_power_hr,
            String facticheskay_power,
            String YNP,
            int value) {}

    public record TechnologyListDto(
            ClassDangerDto id_class_danger,
            MagazinTrashDto id_magazin_trash,
            PhysStateTrashDto id_phys_trash,
            FactoryRefDto id_magasin_factory,
            Long id_technology) {}

    public record MyTrashListDto(
            ClassDangerDto id_class_danger,
            MagazinTrashDto id_magazin_trash,
            FactoryRefDto id_magasin_factory,
            Long id_my_trash,
            float value_trash) {}

    public record DropAirListDto(
            ClassDangerDto id_class_danger,
            DropAirNameDto id_name_grope_air,
            FactoryRefDto id_magasin_factory,
            Long id_drop_air,
            float value_drop_trash) {}

    public record NumberPhoneListDto(Long id_phone_number, String number) {}

    public static MagasinFactoryListDto from(MagasinFactory value) {
        return new MagasinFactoryListDto(
                value.getId_magasin_factory(), value.getId_registration(), value.getDate_register(),
                city(value.getId_cities()), shortTechnology(value.getId_short_discribe_technology()),
                value.getName_obj(), value.getName_own(), value.getAddress_own(), value.getAddress_obj(),
                value.getDevelop_organization(), value.getConfirmed_project(), value.getDate_approve(),
                value.getConclusion_documentation(), value.getAct_use(), value.getRequirements_acts(),
                value.getObj_use_trash(), value.getObj_accept_trash(), value.getCharacter_prod(),
                value.getProject_power_yer(), value.getProject_power_hr(), value.getFacticheskay_power(),
                value.getYNP(), value.getValue());
    }

    public static TechnologyListDto from(Technology value) {
        return new TechnologyListDto(
                danger(value.getId_class_danger()), trash(value.getId_magazin_trash()),
                phys(value.getId_phys_trash()), factory(value.getId_magasin_factory()),
                value.getId_technology());
    }

    public static MyTrashListDto from(MyTrash value, MagasinFactory factory) {
        return new MyTrashListDto(
                danger(value.getId_class_danger()), trash(value.getId_magazin_trash()),
                factory(factory), value.getId_my_trash(), value.getValue_trash());
    }

    public static DropAirListDto from(DropAir value) {
        NameDropAirTrash name = value.getId_name_grope_air();
        DropAirNameDto nameDto = name == null ? null
                : new DropAirNameDto(name.getId_name_grope_air(), name.getName_drop_air_trash());
        return new DropAirListDto(
                danger(value.getId_class_danger()), nameDto, factory(value.getId_magasin_factory()),
                value.getId_drop_air(), value.getValue_drop_trash());
    }

    public static NumberPhoneListDto from(NumberPhone value) {
        return new NumberPhoneListDto(value.getId_phone_number(), value.getNumber());
    }

    private static ClassDangerDto danger(ClassDanger value) {
        return value == null ? null : new ClassDangerDto(value.getId_class_danger(), value.getClass_danger());
    }

    private static MagazinTrashDto trash(MagazinTrash value) {
        return value == null ? null : new MagazinTrashDto(
                value.getId_magazin_trash(), danger(value.getId_class_danger()),
                value.getCode_trash(), value.getName_trash());
    }

    private static PhysStateTrashDto phys(PhysStateTrash value) {
        return value == null ? null : new PhysStateTrashDto(value.getId_mame_group(), value.getName_group());
    }

    private static FactoryRefDto factory(MagasinFactory value) {
        return value == null ? null : new FactoryRefDto(
                value.getId_magasin_factory(), value.getId_registration(), value.getName_obj());
    }

    private static ShortTechnologyDto shortTechnology(ShortDiscribeTechnology value) {
        return value == null ? null : new ShortTechnologyDto(
                value.getId_short_discribe_technology(), value.getTechnology());
    }

    private static CitiesDto city(Cities value) {
        if (value == null) return null;
        Region region = value.getId_region();
        District district = value.getId_district();
        return new CitiesDto(
                value.getId_cities(),
                region == null ? null : new RegionDto(region.getId_region(), region.getName_region()),
                value.getIndex(),
                district == null ? null : new DistrictDto(district.getId_district(), district.getName_district()),
                value.getName_cities());
    }
}
