package com.bookStore.bookstore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Book Store",
        version = "v1",
        contact = @Contact(
                name = "Caique Pires",
                email = "c.pires@ba.estudante.senai.br"
        ),
        description = "This is a Book Lending API designed for physical or online libraries. It allows the registration of authors and books with their respective details. Registered customers can browse the catalog and request to borrow books through the system."
),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        })

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER )
public class OpenApiConfig {
}
