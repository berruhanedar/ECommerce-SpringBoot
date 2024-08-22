package com.berru.app.ecommercespringboot.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetailDto implements Serializable {

    private String type;

    private String code;

    @JsonIgnore
    private String message;

    @JsonIgnore
    private Object[] args;

    @JsonProperty("message")
    public String innerMessage() {
        return getMessage();
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}
