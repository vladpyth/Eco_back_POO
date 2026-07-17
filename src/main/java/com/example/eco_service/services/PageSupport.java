package com.example.eco_service.services;

import com.example.eco_service.entities.Cities;
import com.example.eco_service.entities.District;
import com.example.eco_service.entities.DropAir;
import com.example.eco_service.entities.MagazinTrash;
import com.example.eco_service.entities.MyTrash;
import com.example.eco_service.entities.MyTrashCount;
import com.example.eco_service.entities.NameDropAirTrash;
import com.example.eco_service.entities.NumberPhone;
import com.example.eco_service.entities.PhysStateTrash;
import com.example.eco_service.entities.ShortDiscribeTechnology;
import com.example.eco_service.entities.Technology;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Общие хелперы пагинации и текстового поиска. */
public final class PageSupport {

    private static final Set<Class<?>> SORTABLE = Set.of(
            String.class,
            Boolean.class, boolean.class,
            Integer.class, int.class,
            Long.class, long.class,
            Double.class, double.class,
            Float.class, float.class,
            LocalDate.class,
            LocalDateTime.class
    );

    private PageSupport() {}

    /** Pageable без Sort — ORDER BY задаётся в Specification (Criteria). */
    public static Pageable pageable(Integer page, Integer size, String ignoredIdField) {
        int p = page == null || page < 0 ? 0 : page;
        int s = size == null || size < 1 ? 50 : Math.min(size, 500);
        return PageRequest.of(p, s);
    }

    public static boolean wantsPage(Integer page, Integer size) {
        return page != null && size != null;
    }

    public static <T> Specification<T> textSearch(String q, String idAttribute) {
        return textSearch(q, idAttribute, null, null, idAttribute, false);
    }

    public static <T> Specification<T> textSearch(
            String q,
            String idAttribute,
            String sortBy,
            String sortDir,
            String defaultSortField,
            boolean defaultAsc) {
        return (root, query, cb) -> {
            Class<?> resultType = query != null ? query.getResultType() : null;
            boolean isCount = resultType == Long.class || resultType == long.class;
            if (!isCount && query != null) {
                applyBasicOrder(root, query, cb, sortBy, sortDir, defaultSortField, defaultAsc, idAttribute);
            }
            return buildBasicSearchPredicate(root, cb, q, idAttribute);
        };
    }

    /** Справочник отходов: сортировка по коду, поиск по коду/названию/классу. */
    public static Specification<MagazinTrash> magazinTrashSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "code_trash".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("code_trash")) : cb.desc(root.get("code_trash")));
                } else if ("name_trash".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("name_trash")) : cb.desc(root.get("name_trash")));
                } else if ("id_class_danger".equals(sort)) {
                    Path<Integer> cls = root.get("id_class_danger").get("class_danger");
                    query.orderBy(asc ? cb.asc(cls) : cb.desc(cls));
                } else {
                    query.orderBy(asc ? cb.asc(root.get("id_magazin_trash")) : cb.desc(root.get("id_magazin_trash")));
                }
            }

            if (q == null || q.isBlank()) {
                return cb.conjunction();
            }
            String like = "%" + q.trim().toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.like(cb.lower(root.get("name_trash")), like));
            Join<?, ?> danger = root.join("id_class_danger", JoinType.LEFT);
            preds.add(cb.like(cb.lower(danger.get("class_danger").as(String.class)), like));
            String digits = q.trim().replaceAll("\\D", "");
            if (!digits.isEmpty()) {
                try {
                    int code = Integer.parseInt(digits);
                    preds.add(cb.equal(root.get("code_trash"), code));
                    preds.add(cb.equal(root.get("id_magazin_trash"), (long) code));
                } catch (NumberFormatException ignored) {
                    // skip
                }
            }
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }

    /** Выбросы: сортировка/поиск по предприятию, наименованию, классу, значению. */
    public static Specification<DropAir> dropAirSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                List<Order> orders = new ArrayList<>();
                if (sort == null || "id_magasin_factory".equals(sort)) {
                    Path<String> reg = root.get("id_magasin_factory").get("id_registration");
                    orders.add(asc ? cb.asc(cb.length(reg)) : cb.desc(cb.length(reg)));
                    orders.add(asc ? cb.asc(reg) : cb.desc(reg));
                } else if ("id_name_grope_air".equals(sort)) {
                    Path<String> name = root.get("id_name_grope_air").get("name_drop_air_trash");
                    orders.add(asc ? cb.asc(name) : cb.desc(name));
                } else if ("id_class_danger".equals(sort)) {
                    Path<Integer> cls = root.get("id_class_danger").get("class_danger");
                    orders.add(asc ? cb.asc(cls) : cb.desc(cls));
                } else if ("value_drop_trash".equals(sort)) {
                    orders.add(asc ? cb.asc(root.get("value_drop_trash")) : cb.desc(root.get("value_drop_trash")));
                } else {
                    orders.add(asc ? cb.asc(root.get("id_drop_air")) : cb.desc(root.get("id_drop_air")));
                }
                query.orderBy(orders);
            }

            if (q == null || q.isBlank()) {
                return cb.conjunction();
            }
            String like = "%" + q.trim().toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            Join<?, ?> factory = root.join("id_magasin_factory", JoinType.LEFT);
            Join<?, ?> name = root.join("id_name_grope_air", JoinType.LEFT);
            Join<?, ?> danger = root.join("id_class_danger", JoinType.LEFT);
            preds.add(cb.like(cb.lower(factory.get("name_obj")), like));
            preds.add(cb.like(cb.lower(factory.get("id_registration")), like));
            preds.add(cb.like(cb.lower(name.get("name_drop_air_trash")), like));
            preds.add(cb.like(cb.lower(danger.get("class_danger").as(String.class)), like));
            preds.add(cb.like(cb.lower(root.get("value_drop_trash").as(String.class)), like));
            String digits = q.trim().replaceAll("\\D", "");
            if (!digits.isEmpty()) {
                try {
                    long id = Long.parseLong(digits);
                    preds.add(cb.equal(root.get("id_drop_air"), id));
                    preds.add(cb.equal(factory.get("id_magasin_factory"), id));
                    preds.add(cb.equal(factory.get("id_registration"), digits));
                } catch (NumberFormatException ignored) {
                    // skip
                }
            }
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }

    /**
     * Отходы предприятия: сортировка по коду отхода / предприятию (через MyTrashCount),
     * поиск по связанным справочникам.
     */
    public static Specification<MyTrash> myTrashSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "id_magazin_trash".equals(sort)) {
                    Path<Integer> code = root.get("id_magazin_trash").get("code_trash");
                    query.orderBy(asc ? cb.asc(code) : cb.desc(code));
                } else if ("id_class_danger".equals(sort)) {
                    Path<Integer> cls = root.get("id_class_danger").get("class_danger");
                    query.orderBy(asc ? cb.asc(cls) : cb.desc(cls));
                } else if ("value_trash".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("value_trash")) : cb.desc(root.get("value_trash")));
                } else if ("id_magasin_factory".equals(sort)) {
                    // предприятие через MyTrashCount — сортируем по рег. номеру подзапросом через join в from
                    Subquery<String> regSq = query.subquery(String.class);
                    Root<MyTrashCount> mc = regSq.from(MyTrashCount.class);
                    regSq.select(mc.get("id_object_place_trash").get("id_registration"));
                    regSq.where(cb.equal(mc.get("id_my_trash").get("id_my_trash"), root.get("id_my_trash")));
                    query.orderBy(asc ? cb.asc(regSq) : cb.desc(regSq));
                } else {
                    query.orderBy(asc ? cb.asc(root.get("id_my_trash")) : cb.desc(root.get("id_my_trash")));
                }
            }

            if (q == null || q.isBlank()) {
                return cb.conjunction();
            }
            String like = "%" + q.trim().toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            Join<?, ?> trash = root.join("id_magazin_trash", JoinType.LEFT);
            Join<?, ?> danger = root.join("id_class_danger", JoinType.LEFT);
            preds.add(cb.like(cb.lower(trash.get("name_trash")), like));
            preds.add(cb.like(cb.lower(trash.get("code_trash").as(String.class)), like));
            preds.add(cb.like(cb.lower(danger.get("class_danger").as(String.class)), like));
            preds.add(cb.like(cb.lower(root.get("value_trash").as(String.class)), like));

            Subquery<Long> factoryMatch = query.subquery(Long.class);
            Root<MyTrashCount> mc = factoryMatch.from(MyTrashCount.class);
            Join<?, ?> factory = mc.join("id_object_place_trash", JoinType.LEFT);
            factoryMatch.select(mc.get("id_my_trash").get("id_my_trash"));
            List<Predicate> factoryPreds = new ArrayList<>();
            factoryPreds.add(cb.like(cb.lower(factory.get("name_obj")), like));
            factoryPreds.add(cb.like(cb.lower(factory.get("id_registration")), like));
            String digits = q.trim().replaceAll("\\D", "");
            if (!digits.isEmpty()) {
                try {
                    long id = Long.parseLong(digits);
                    factoryPreds.add(cb.equal(factory.get("id_magasin_factory"), id));
                    factoryPreds.add(cb.equal(factory.get("id_registration"), digits));
                    preds.add(cb.equal(root.get("id_my_trash"), id));
                    try {
                        preds.add(cb.equal(trash.get("code_trash"), Integer.parseInt(digits)));
                    } catch (NumberFormatException ignored) {
                        // skip
                    }
                } catch (NumberFormatException ignored) {
                    // skip
                }
            }
            factoryMatch.where(cb.or(factoryPreds.toArray(new Predicate[0])));
            preds.add(root.get("id_my_trash").in(factoryMatch));

            return cb.or(preds.toArray(new Predicate[0]));
        };
    }

    /** Технологии: сортировка/поиск по предприятию, отходу, классу, физ. состоянию. */
    public static Specification<Technology> technologySpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                List<Order> orders = new ArrayList<>();
                if (sort == null || "id_magasin_factory".equals(sort)) {
                    Path<String> reg = root.get("id_magasin_factory").get("id_registration");
                    orders.add(asc ? cb.asc(cb.length(reg)) : cb.desc(cb.length(reg)));
                    orders.add(asc ? cb.asc(reg) : cb.desc(reg));
                } else if ("id_magazin_trash".equals(sort)) {
                    Path<Integer> code = root.get("id_magazin_trash").get("code_trash");
                    orders.add(asc ? cb.asc(code) : cb.desc(code));
                } else if ("id_class_danger".equals(sort)) {
                    Path<Integer> cls = root.get("id_class_danger").get("class_danger");
                    orders.add(asc ? cb.asc(cls) : cb.desc(cls));
                } else if ("id_phys_trash".equals(sort)) {
                    Path<String> phys = root.get("id_phys_trash").get("name_group");
                    orders.add(asc ? cb.asc(phys) : cb.desc(phys));
                } else {
                    orders.add(asc ? cb.asc(root.get("id_technology")) : cb.desc(root.get("id_technology")));
                }
                query.orderBy(orders);
            }

            if (q == null || q.isBlank()) {
                return cb.conjunction();
            }
            String like = "%" + q.trim().toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            Join<?, ?> factory = root.join("id_magasin_factory", JoinType.LEFT);
            Join<?, ?> trash = root.join("id_magazin_trash", JoinType.LEFT);
            Join<?, ?> danger = root.join("id_class_danger", JoinType.LEFT);
            Join<?, ?> phys = root.join("id_phys_trash", JoinType.LEFT);
            preds.add(cb.like(cb.lower(factory.get("name_obj")), like));
            preds.add(cb.like(cb.lower(factory.get("id_registration")), like));
            preds.add(cb.like(cb.lower(trash.get("name_trash")), like));
            preds.add(cb.like(cb.lower(trash.get("code_trash").as(String.class)), like));
            preds.add(cb.like(cb.lower(danger.get("class_danger").as(String.class)), like));
            preds.add(cb.like(cb.lower(phys.get("name_group")), like));
            String digits = q.trim().replaceAll("\\D", "");
            if (!digits.isEmpty()) {
                try {
                    long id = Long.parseLong(digits);
                    preds.add(cb.equal(root.get("id_technology"), id));
                    preds.add(cb.equal(factory.get("id_magasin_factory"), id));
                    preds.add(cb.equal(factory.get("id_registration"), digits));
                    try {
                        preds.add(cb.equal(trash.get("code_trash"), Integer.parseInt(digits)));
                    } catch (NumberFormatException ignored) {
                        // skip
                    }
                } catch (NumberFormatException ignored) {
                    // skip
                }
            }
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }

    /** Физ. состояние: по названию. */
    public static Specification<PhysStateTrash> physStateTrashSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "name_group".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("name_group")) : cb.desc(root.get("name_group")));
                } else {
                    query.orderBy(asc ? cb.asc(root.get("id_mame_group")) : cb.desc(root.get("id_mame_group")));
                }
            }
            return buildBasicSearchPredicate(root, cb, q, "id_mame_group");
        };
    }

    /** Краткое описание технологии: по тексту. */
    public static Specification<ShortDiscribeTechnology> shortDiscribeTechnologySpec(
            String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "technology".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("technology")) : cb.desc(root.get("technology")));
                } else {
                    query.orderBy(asc
                            ? cb.asc(root.get("id_short_discribe_technology"))
                            : cb.desc(root.get("id_short_discribe_technology")));
                }
            }
            return buildBasicSearchPredicate(root, cb, q, "id_short_discribe_technology");
        };
    }

    /** Наименования выбросов: по названию. */
    public static Specification<NameDropAirTrash> nameDropAirTrashSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "name_drop_air_trash".equals(sort)) {
                    query.orderBy(asc
                            ? cb.asc(root.get("name_drop_air_trash"))
                            : cb.desc(root.get("name_drop_air_trash")));
                } else {
                    query.orderBy(asc
                            ? cb.asc(root.get("id_name_grope_air"))
                            : cb.desc(root.get("id_name_grope_air")));
                }
            }
            return buildBasicSearchPredicate(root, cb, q, "id_name_grope_air");
        };
    }

    /** Районы: по названию. */
    public static Specification<District> districtSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "name_district".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("name_district")) : cb.desc(root.get("name_district")));
                } else {
                    query.orderBy(asc ? cb.asc(root.get("id_district")) : cb.desc(root.get("id_district")));
                }
            }
            return buildBasicSearchPredicate(root, cb, q, "id_district");
        };
    }

    /** Города: по названию / индексу / району / области. */
    public static Specification<Cities> citiesSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "name_cities".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("name_cities")) : cb.desc(root.get("name_cities")));
                } else if ("index".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("index")) : cb.desc(root.get("index")));
                } else if ("id_district".equals(sort)) {
                    Path<String> d = root.get("id_district").get("name_district");
                    query.orderBy(asc ? cb.asc(d) : cb.desc(d));
                } else if ("id_region".equals(sort)) {
                    Path<String> r = root.get("id_region").get("name_region");
                    query.orderBy(asc ? cb.asc(r) : cb.desc(r));
                } else {
                    query.orderBy(asc ? cb.asc(root.get("id_cities")) : cb.desc(root.get("id_cities")));
                }
            }

            if (q == null || q.isBlank()) {
                return cb.conjunction();
            }
            String like = "%" + q.trim().toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.like(cb.lower(root.get("name_cities")), like));
            preds.add(cb.like(cb.lower(root.get("index")), like));
            Join<?, ?> district = root.join("id_district", JoinType.LEFT);
            Join<?, ?> region = root.join("id_region", JoinType.LEFT);
            preds.add(cb.like(cb.lower(district.get("name_district")), like));
            preds.add(cb.like(cb.lower(region.get("name_region")), like));
            String digits = q.trim().replaceAll("\\D", "");
            if (!digits.isEmpty()) {
                try {
                    long id = Long.parseLong(digits);
                    preds.add(cb.equal(root.get("id_cities"), id));
                } catch (NumberFormatException ignored) {
                    // skip
                }
            }
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }

    /** Телефоны: по номеру. */
    public static Specification<NumberPhone> numberPhoneSpec(String q, String sortBy, String sortDir) {
        return (root, query, cb) -> {
            boolean isCount = isCountQuery(query);
            if (!isCount && query != null) {
                boolean asc = resolveAsc(sortBy, sortDir, true);
                String sort = blankToNull(sortBy);
                if (sort == null || "number".equals(sort)) {
                    query.orderBy(asc ? cb.asc(root.get("number")) : cb.desc(root.get("number")));
                } else {
                    query.orderBy(asc ? cb.asc(root.get("id_phone_number")) : cb.desc(root.get("id_phone_number")));
                }
            }
            return buildBasicSearchPredicate(root, cb, q, "id_phone_number");
        };
    }

    private static boolean isCountQuery(jakarta.persistence.criteria.CriteriaQuery<?> query) {
        if (query == null) return false;
        Class<?> resultType = query.getResultType();
        return resultType == Long.class || resultType == long.class;
    }

    private static String blankToNull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }

    private static boolean resolveAsc(String sortBy, String sortDir, boolean defaultAsc) {
        if (sortBy != null && !sortBy.isBlank()) {
            return sortDir == null || sortDir.isBlank() || "asc".equalsIgnoreCase(sortDir);
        }
        if (sortDir != null && !sortDir.isBlank()) {
            return "asc".equalsIgnoreCase(sortDir);
        }
        return defaultAsc;
    }

    private static <T> void applyBasicOrder(
            Root<T> root,
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            String sortBy,
            String sortDir,
            String defaultSortField,
            boolean defaultAsc,
            String idAttribute) {
        String field = resolveSortField(root, sortBy, defaultSortField != null ? defaultSortField : idAttribute);
        boolean asc = resolveAsc(sortBy, sortDir, defaultAsc);
        Path<?> path = root.get(field);
        if ("id_registration".equals(field) && path.getJavaType() == String.class) {
            var len = cb.length(root.get(field));
            query.orderBy(
                    asc ? cb.asc(len) : cb.desc(len),
                    asc ? cb.asc(path) : cb.desc(path)
            );
        } else {
            query.orderBy(asc ? cb.asc(path) : cb.desc(path));
        }
    }

    private static <T> Predicate buildBasicSearchPredicate(
            Root<T> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            String q,
            String idAttribute) {
        if (q == null || q.isBlank()) {
            return cb.conjunction();
        }
        String like = "%" + q.trim().toLowerCase() + "%";
        List<Predicate> preds = new ArrayList<>();
        root.getModel().getDeclaredSingularAttributes().forEach(attr -> {
            if (attr.getJavaType() == String.class) {
                preds.add(cb.like(cb.lower(root.get(attr.getName())), like));
            }
        });
        String digits = q.trim().replaceAll("\\D", "");
        if (!digits.isEmpty() && idAttribute != null) {
            try {
                long id = Long.parseLong(digits);
                preds.add(cb.equal(root.get(idAttribute), id));
            } catch (NumberFormatException ignored) {
                // skip
            }
            root.getModel().getDeclaredSingularAttributes().forEach(attr -> {
                Class<?> t = attr.getJavaType();
                if (t == int.class || t == Integer.class) {
                    try {
                        preds.add(cb.equal(root.get(attr.getName()), Integer.parseInt(digits)));
                    } catch (NumberFormatException ignored) {
                        // skip
                    }
                } else if (t == long.class || t == Long.class) {
                    try {
                        preds.add(cb.equal(root.get(attr.getName()), Long.parseLong(digits)));
                    } catch (NumberFormatException ignored) {
                        // skip
                    }
                }
            });
        }
        if (preds.isEmpty()) {
            return cb.conjunction();
        }
        return cb.or(preds.toArray(new Predicate[0]));
    }

    private static <T> String resolveSortField(Root<T> root, String sortBy, String fallback) {
        if (sortBy != null && !sortBy.isBlank() && isSortable(root, sortBy)) {
            return sortBy;
        }
        if (fallback != null && isSortable(root, fallback)) {
            return fallback;
        }
        for (SingularAttribute<? super T, ?> attr : root.getModel().getSingularAttributes()) {
            if (attr.isId()) {
                return attr.getName();
            }
        }
        return fallback;
    }

    private static <T> boolean isSortable(Root<T> root, String name) {
        try {
            Attribute<? super T, ?> attr = root.getModel().getAttribute(name);
            if (!(attr instanceof SingularAttribute<?, ?> singular)) {
                return false;
            }
            if (singular.isAssociation()) {
                return false;
            }
            return SORTABLE.contains(singular.getJavaType());
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
