package com.example.roomie.Config;


import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){

        return new OpenAPI().components(new Components()).info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("Roomie Project")
                .description("Springdoc를 사용한 Roomie Swagger UI")
                .version("1.0.0");
    }
}
