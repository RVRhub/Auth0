package ru.zerocode.authapp.model.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper=true)
public class RestError {
    @Getter @Setter
    @JsonProperty("code")
    private int code;

    @Getter @Setter
    @JsonProperty("txt")
    private String txt;

    public RestError(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }
}