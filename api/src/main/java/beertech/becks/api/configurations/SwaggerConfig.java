package beertech.becks.api.configurations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${beertech.swagger.baseUrl}")
    private String baseUrl;

    @Value("${beertech.swagger.apiTitle}")
    private String apiTitle;

    @Value("${beertech.swagger.apiInfo}")
    private String apiInfo;

    @Bean
    public Docket microServicesApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(baseUrl)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .apiInfo(metaData(apiInfo));
    }

    private ApiInfo metaData(String description) {
        final String projectVersion = "1.0";
        return new ApiInfoBuilder()
                .description(description)
                .version(projectVersion)
                .title(apiTitle)
                .contact(
                        new Contact("Becks Bank", "https://github.com/victorhrgc/beertech.bancobeer", "contato@becks,bank"))
                .license("Becks Bank © 2020")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*"))
                .build();
    }

    ArrayList<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }
}
