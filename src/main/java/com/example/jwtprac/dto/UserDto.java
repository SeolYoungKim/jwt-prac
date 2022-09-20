package com.example.jwtprac.dto;

import com.example.jwtprac.entity.Authority;
import com.example.jwtprac.entity.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nickName;

    private Set<AuthorityDto> authorityDtoSet;

    @Builder
    public UserDto(String username, String password, String nickName, Set<AuthorityDto> authorityDtoSet) {
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.authorityDtoSet = authorityDtoSet;
    }



    public static UserDto from(Users user) {
        if (user == null) return null;

        return UserDto.builder()
                .username(user.getUsername())
                .nickName(user.getNickname())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder()
                                .authorityName(authority.getAuthorityName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

}
