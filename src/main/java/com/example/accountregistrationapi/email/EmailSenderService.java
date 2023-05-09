package com.example.accountregistrationapi.email;

public interface EmailSenderService {
    void sendEmail(String to,String subject, String email);
}
