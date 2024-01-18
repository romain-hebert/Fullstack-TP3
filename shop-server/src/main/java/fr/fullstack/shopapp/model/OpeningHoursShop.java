package fr.fullstack.shopapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "openingHours")
public class OpeningHoursShop {
    @Getter
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "CloseAt may not be null")
    private LocalTime closeAt;

    @Getter
    @Setter
    @Column(nullable = false)
    @NotNull(message = "Day may not be null")
    @Min(value = 1, message = "Day should not be less than 1")
    @Max(value = 7, message = "Day should not be greater than 7")
    private int day;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "OpenAt may not be null")
    private LocalTime openAt;

    public LocalTime getOpenAt() {
        return openAt;
    }

    public void setCloseAt(LocalTime closeAt) {
        this.closeAt = closeAt;
    }


    public void setOpenAt(LocalTime openAt) {
        this.openAt = openAt;
    }
}
