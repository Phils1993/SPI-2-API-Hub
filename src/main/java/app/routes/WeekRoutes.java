package app.routes;

import static io.javalin.apibuilder.ApiBuilder.*;

import app.controllers.WeekController;
import app.services.WeekService;

public class WeekRoutes {

    public static void register(io.javalin.Javalin app) {
        app.routes(() -> {
            crud("weeks/{id}", new WeekController(new WeekService()));
        });
    }
}
