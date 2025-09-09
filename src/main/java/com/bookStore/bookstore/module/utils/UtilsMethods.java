package com.bookStore.bookstore.module.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Component
public class UtilsMethods {

    public URI generateHeaderLocation(UUID id){
       return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    public static String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        int atIndex = email.indexOf('@');

        if (atIndex > 0) {
            return email.substring(0, atIndex);
        } else {
            return "";
        }
    }
}
