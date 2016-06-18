package ru.zerocode.authapp.model.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse {
    @Getter
    private boolean success = true;

    @Getter
    private Object result;

    @Getter
    private List<RestError> errors;

    public RestResponse(boolean success){
        this.success = success;
    }

    public RestResponse(Object body){
        this.result = body;
    }

    public RestResponse addError(RestError error) {
        if (errors == null) errors = new ArrayList<>();
        errors.add(error);
        return this;
    }
}
