package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.LoginResponseUserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private boolean result;

    @JsonProperty("user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LoginResponseUserDto loginResponseUserDto;
}
