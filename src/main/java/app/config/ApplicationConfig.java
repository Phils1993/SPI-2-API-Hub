package app.config;

import app.exceptions.ApiException;
import app.routes.WeekRoutes;
import app.services.WeekService;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class ApplicationConfig {
    private static Javalin app;

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        ServiceRegistry services = new ServiceRegistry(emf);
        RoutesRegistry routes = new RoutesRegistry(services);

        var app = Javalin.create(config -> configure(config, routes));

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
