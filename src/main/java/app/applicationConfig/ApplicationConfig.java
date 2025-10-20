package app.applicationConfig;

import app.exceptions.ApiException;
import app.routes.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

import java.util.Map;

public class ApplicationConfig {

    // Routes objekt som holder alle API endpoints
    private static Routes routes = new Routes();

    /**
     * Konfiguration af Javalin-serveren
     */
    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;                  // Slå Javalin-banneret fra i konsollen
        config.bundledPlugins.enableRouteOverview("/routes"); // Aktivér oversigt over alle routes på /routes
        config.router.contextPath = "/api/v1";            // Base path for alle endpoints
        config.router.apiBuilder(routes.getRoutes());     // Registrer alle routes fra Routes-klassen
    }

    /**
     * Start server på et givent portnummer
     *
     * @param port Portnummer hvor serveren skal starte
     * @return instans af Javalin
     */
    public static Javalin startServer(int port) {
        routes = new Routes();                             // Opret ny Routes-instans
        var app = Javalin.create(ApplicationConfig::configuration); // Opret Javalin med konfiguration

        // Global exception handler for ApiException
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());                // Sæt HTTP-statuskode fra ApiException
            ctx.json(Map.of(                              // Returner JSON med fejlinfo
                    "status", e.getStatusCode(),
                    "message", e.getMessage()
            ));
        });

        app.start(port);                                  // Start server på valgt port
        return app;                                       // Returner Javalin instans
    }

    /**
     * Stop server
     *
     * @param app Instans af Javalin som skal stoppes
     */
    public static void stopServer(Javalin app) {
        app.stop();                                       // Stop serveren
    }
}
