package com.example.accountregistrationapi.user;

import com.example.accountregistrationapi.Security.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

     private final static String USER_NOT_FOUND_MSG =
             "user with email %s not found";
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          return userRepository.findByEmail(email).orElseThrow(() ->
                  new UsernameNotFoundException(
                          String.format(USER_NOT_FOUND_MSG, email)));
     }

     public String singUpUser(User user){
         // enableUser(user.getEmail());
          //System.out.println("is ran");
          boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

          if(userExists){
               throw new IllegalStateException("Email already in use");
          }
          String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword());
          user.setPassword(encodedPassword);

          userRepository.save(user);

          return "Success";
     }
     public void enableUser(String email) {
          userRepository.enableUser(email);
     }



}
