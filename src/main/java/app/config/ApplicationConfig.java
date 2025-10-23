package app.config;

import app.exceptions.ApiException;
import app.routes.WeekRoutes;
import app.security.SecurityController;
import app.services.WeekService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ApplicationConfig {
    private static Javalin app;
    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        ServiceRegistry services = new ServiceRegistry(emf);
        RoutesRegistry routes = new RoutesRegistry(services);

        var app = Javalin.create(config -> configure(config, routes));
        SecurityController securityController = new SecurityController();
        app.beforeMatched(securityController.authenticate());
        app.beforeMatched(securityController.authorize());

        // Register global exception handlers here if needed

        app.start(port);
        return app;
    }

    private static void configure(JavalinConfig config, RoutesRegistry routes) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes");
        config.router.contextPath = "/api/v1";
        config.router.apiBuilder(routes.getRoutes());
    }

/**
     * Stop server
     *
     * @param app Instans af Javalin som skal stoppes
     */
public static void stopServer() {
    if (app != null) {
        System.out.println("Stopping server and closing EMF...");
        app.stop();
        if (HibernateConfig.getEntityManagerFactory().isOpen()) {
            HibernateConfig.getEntityManagerFactory().close();
        }
    }
}
}
