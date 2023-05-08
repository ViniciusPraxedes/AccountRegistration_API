package com.example.accountregistrationapi.Registration;

import com.example.accountregistrationapi.user.User;
import com.example.accountregistrationapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;

    public String register(RegistrationRequest request){
        String Response = userService.singUpUser(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword())
        );

        return Response;
    }

}
