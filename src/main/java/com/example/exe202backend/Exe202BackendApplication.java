package com.example.exe202backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "SIIN",
                version = "1.0.0",
                description = "EXE201 Project Spring24",
                termsOfService = "SIIN",
                license = @License(
                        name = "licence",
                        url = "siin"
                )
        ),
        servers = {
                @Server(url = "http://172.188.64:8080.221/", description = "Default Server URL"),
                @Server(url = "http://localhost:8080/", description = "Default Server URL")
        }
)
public class Exe202BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Exe202BackendApplication.class, args);
    }

}
