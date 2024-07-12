package com.studentgradetacker.sgt.model.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private String message;

    private Integer id;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, Integer id) {
        this.message = message;
        this.id = id;
    }


}
