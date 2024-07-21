package com.studentgradetacker.sgt.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddGradesRequest {

    private Integer enrolledId;

    private Double prelims;

    private Double midterms;

    private Double finals;
}
