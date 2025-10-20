package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", ctx -> ctx.result("Hello World"));

            /* get routes
            path("/highscores", highscoresRoutes.getRoutes());
            path("/highscore", highscoreRoutes.getRoutes());

             */
        };
    }
}
