package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "basepriceDates")
@Where(clause = "deleted_date is null")
public class BasePriceDate extends AbstractDate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time", nullable = false)
    private Date dateTime;

    @NotBlank
    private String type;

    @NotNull
    @Positive
    private Integer datePrice;

}
