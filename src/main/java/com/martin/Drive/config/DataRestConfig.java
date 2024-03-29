package com.martin.Drive.config;

import com.martin.Drive.entity.Fichero;
import com.martin.Drive.entity.User;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/*Esto solo configura la API Rest*/
@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {


    private String theAllowedOrigins = "http://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT};

        config.exposeIdsFor(Fichero.class);
        config.exposeIdsFor(User.class);

        disableHttpMethods(Fichero.class, config, theUnsupportedActions);
        disableHttpMethods(User.class, config, theUnsupportedActions);

        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);


        config.getProjectionConfiguration().addProjection(UserProjection.class);
    }

    private void disableHttpMethods(Class theClass,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions));
    }

}
