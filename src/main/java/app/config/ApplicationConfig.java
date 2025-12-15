package app.config;

import app.security.SecurityController;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class ApplicationConfig {
    private static Javalin app;

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        ServiceRegistry services = new ServiceRegistry(emf);
        RoutesRegistry routes = new RoutesRegistry(services);

        var app = Javalin.create(config -> configure(config, routes));

        SecurityController securityController = new SecurityController();
        app.beforeMatched(securityController.authenticate());
        app.beforeMatched(securityController.authorize());


        // CORS HEADERS
        app.before(ApplicationConfig::corsHeaders);
        app.options("/*", ApplicationConfig::corsHeadersOptions);

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

    private static void corsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }

    private static void corsHeadersOptions(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.status(204);
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
