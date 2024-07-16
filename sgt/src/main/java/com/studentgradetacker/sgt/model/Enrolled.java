package com.studentgradetacker.sgt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ManyToAny;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Enrolled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enrolledId;

    @ManyToOne
    @JoinColumn(name = "enrolled_id")
    private Students students;

    private Integer courseId;

    private Boolean isArchived;

}
