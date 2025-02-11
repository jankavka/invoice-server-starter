package cz.itnetwork.dto;

import lombok.Getter;

@Getter
public class MyErrorResponse {

    private String message;

    public MyErrorResponse(String message){
        this.message = message;
    }

}
