package com.studentgradetacker.sgt.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGradeRequest {

    private Double prelims;

    private  Double midterms;

    private Double finals;
}
