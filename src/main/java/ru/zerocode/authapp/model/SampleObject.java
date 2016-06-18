package ru.zerocode.authapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * Тестовый объект для проверки работоспособности
 * */
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleObject {
    // Идентификатор
    @Getter
    private String id;
    // Текст
    @Getter
    private String text;
    // Сущность пользователя
    @Getter
    private Identity identity;
}
