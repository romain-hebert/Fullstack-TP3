package fr.fullstack.shopapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "shops")
@Indexed(index = "idx_shops")
@FilterDefs({
    @FilterDef(name = "createdAtAfter", parameters = @ParamDef(name = "createdAtAfter", type = LocalDate.class)),
    @FilterDef(name = "createdAtBefore", parameters = @ParamDef(name = "createdAtBefore", type = LocalDate.class))
})
@Filters({
    @Filter(name = "createdAtAfter", condition = ":createdAtAfter <= created_at"),
    @Filter(name = "createdAtBefore", condition = ":createdAtBefore >= created_at"),
})
public class Shop {
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    @Column(nullable = false)
    @NotNull(message = "InVacations may not be null")
    @GenericField
    private boolean inVacations;

    @Setter
    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @NotNull(message = "Name may not be null")
    @FullTextField
    private String name;

    @Setter
    @Formula(value = "(SELECT COUNT(DISTINCT pc.category_id) FROM products_categories pc "
            + "WHERE pc.product_id IN (SELECT p.id FROM products p WHERE p.shop_id = id))")
    private Long nbCategories;

    @Setter
    @Formula(value = "(SELECT COUNT(*) FROM products p WHERE p.shop_id = id)")
    private Long nbProducts;

    @Setter
    @OneToMany(cascade = {CascadeType.ALL})
    private List<@Valid OpeningHoursShop> openingHours = new ArrayList<OpeningHoursShop>();

    @Setter
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products = new ArrayList<Product>();

}
