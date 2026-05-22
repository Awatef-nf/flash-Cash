package com.example.flachCash.dto;
import com.example.flachCash.validation.FieldsMatch;
import com.example.flachCash.validation.StrongPass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldsMatch(
        first = "password",
        second = "confirmPassword"
)
public class RegisterDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @StrongPass
    private String password;

    @NotBlank
    private String confirmPassword;
}
