package com.studentgradetacker.sgt.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String message;

    private Integer id;

    public MessageResponse(String message) {
        this.message = message;
    }

}
