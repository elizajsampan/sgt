package com.studentgradetacker.sgt.model.payload.response;

import com.studentgradetacker.sgt.model.Students;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GwaResponse {

    private Students students;

    private Double gwa;

}
