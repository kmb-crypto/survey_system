package main.dto;

import lombok.Data;

@Data
public class LoginResponseUserDto {
    private int id;
    private String name;
    private boolean moderation;
}
