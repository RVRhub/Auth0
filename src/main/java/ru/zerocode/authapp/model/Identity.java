package ru.zerocode.authapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Сущность пользователя полученная из Токена
 * */
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Identity {
    // Идентификатор пользователя
    @Getter
    @JsonProperty("user_id")
    private String id;

    // Имя пользователя
    @Getter
    @JsonProperty("name")
    private String name;

    // Никнейм
    @Getter
    @JsonProperty("nickname")
    private String nickName;

    // Аватарка
    @Getter
    @JsonProperty("picture")
    private String picture;

    // Емаил
    @Getter
    @JsonProperty("email")
    private String email;

    // Роли пользователя
    @Getter
    private List<GrantedAuthority> authorities;

    // Время просрочки
    @Getter
    private Date expires;


    /**
     * Билдер
     * */
    public static class Builder{
        private Identity identity;

        public Builder() {
            this.identity = new Identity();
            this.identity.authorities = new ArrayList<>();
        }

        public Builder id(String id) {
            this.identity.id = id;
            return this;
        }

        public Builder name(String name) {
            this.identity.name = name;
            return this;
        }

        public Builder nickName(String nickName) {
            this.identity.nickName = nickName;
            return this;
        }

        public Builder picture(String picture) {
            this.identity.picture = picture;
            return this;
        }

        public Builder email(String email) {
            this.identity.email = email;
            return this;
        }

        public Builder expires(int expires) {
            this.identity.expires = new Date((long)expires*1000);
            return this;
        }

        public Builder authorities(String authorities) {
            if(!StringUtils.isEmpty(authorities))
                this.identity.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
            return this;
        }

        public Identity build(){
            return this.identity;
        }
    }
}
