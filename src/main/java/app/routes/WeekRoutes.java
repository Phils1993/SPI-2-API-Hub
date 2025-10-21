package app.routes;

import app.Controllers.WeekController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class WeekRoutes {

    public EndpointGroup getRoutes() {
        return () -> {
            path("week", () -> {
                post(WeekController::create);
                get(WeekController::getAll);

                path("{id}", () -> {
                    get(WeekController::getById);
                })
            })
            /* get routes
            path("/highscores", highscoresRoutes.getRoutes());
            path("/highscore", highscoreRoutes.getRoutes());

             */
        };
    }
}
