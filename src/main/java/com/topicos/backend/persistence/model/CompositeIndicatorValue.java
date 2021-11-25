package com.topicos.backend.persistence.model;

import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "compositeindicatorvalue")
public class CompositeIndicatorValue {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double value;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "indicator_id")
    @ToString.Exclude
    private Indicator indicatorId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company companyId;


}
