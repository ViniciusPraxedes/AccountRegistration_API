package com.example.accountregistrationapi.user;

import com.example.accountregistrationapi.Security.PasswordEncoder;
import com.example.accountregistrationapi.email.EmailSender;
import com.example.accountregistrationapi.token.ConfirmationToken;
import com.example.accountregistrationapi.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

     private final static String USER_NOT_FOUND_MSG =
             "user with email %s not found";
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final ConfirmationTokenService confirmationTokenService;
     private final EmailSender emailSender;
     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          return userRepository.findByEmail(email).orElseThrow(() ->
                  new UsernameNotFoundException(
                          String.format(USER_NOT_FOUND_MSG, email)));
     }

     public String singUpUser(User user){

          //Check if user already exists in the database
          boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
          if(userExists){
               throw new IllegalStateException("Email already in use");
          }
          //############################################


          //Encodes the user password
          String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword());
          user.setPassword(encodedPassword);
          //#########################


          //Generates a confirmation token and saves it to the database
          String token = UUID.randomUUID().toString();
          ConfirmationToken confirmationToken = new ConfirmationToken(
                  token,
                  LocalDateTime.now(),
                  LocalDateTime.now().plusMinutes(15),
                  user
          );
          confirmationTokenService.saveConfirmationToken(confirmationToken);
          //################################################################
          String email = user.getEmail();
          String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;


          emailSender.sendEmail("doitc4@hotmail.com","Confirm your email",link);


          userRepository.save(user);

          return token;
     }
     public void enableUser(String email) {
          userRepository.enableUser(email);
     }




}
