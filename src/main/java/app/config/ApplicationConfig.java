package app.config;

import app.exceptions.ApiException;
import app.security.SecurityController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationConfig {
    private static Javalin app;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        ServiceRegistry services = new ServiceRegistry(emf);
        RoutesRegistry routes = new RoutesRegistry(services);

        app = Javalin.create(config -> configure(config, routes));

        SecurityController securityController = new SecurityController();
        app.beforeMatched(securityController.authenticate());
        app.beforeMatched(securityController.authorize());


        // CORS HEADERS
        app.before(ApplicationConfig::corsHeaders);
        app.options("/*", ApplicationConfig::corsHeadersOptions);




        setSecurity();
        setGeneralExceptionHandling();
        setDebugHeaderLogging();


        // Register global exception handlers here if needed

        app.start(port);
        return app;
    }

    private static void configure(JavalinConfig config, RoutesRegistry routes) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes");
        config.http.defaultContentType = "application/json";
        config.router.contextPath = "/api/v1";
        config.router.apiBuilder(routes.getRoutes());
    }

    private static void corsHeadersOptions(Context ctx) {
        String origin = ctx.header("Origin");

        if (origin != null) {
            ctx.header("Access-Control-Allow-Origin", origin);
        }

        ctx.header("Vary", "Origin");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.status(204);
    }


    private static void corsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }



    private static void setSecurity() {
        SecurityController securityController = new SecurityController();
        app.beforeMatched(securityController.authenticate());
        app.beforeMatched(securityController.authorize());
    }


    private static void setGeneralExceptionHandling() {
        app.exception(Exception.class, (e, ctx) -> {
            int statusCode = (e instanceof ApiException apiEx) ? apiEx.getStatusCode() : 500;
            String message = (e instanceof ApiException) ? e.getMessage() : "Internal server error";

            logger.error("An exception occurred", e);

            ObjectNode on = jsonMapper.createObjectNode()
                    .put("status", statusCode)
                    .put("msg", message);

            ctx.json(on);
            ctx.status(statusCode);
        });
    }

    private static void setDebugHeaderLogging() {
        app.before(ctx -> {
            String pathInfo = ctx.req().getPathInfo();
            System.out.println("Request path: " + pathInfo);
            ctx.req().getHeaderNames().asIterator().forEachRemaining(header ->
                    System.out.println("Header: " + header + " = " + ctx.req().getHeader(header)));
        });
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
