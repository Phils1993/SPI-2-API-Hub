package app.routes;

import app.Controllers.WeekController;
import app.services.WeekService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class WeekRoutes {

    private final WeekController weekController;

    public WeekRoutes(WeekService weekService) {
        this.weekController = new WeekController(weekService);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("week", () -> {
                post(weekController.create());
                get(weekController.getAll());

                path("{id}", () -> {
                    get(weekController.getById());
                    put(weekController.update());
                    delete(weekController.delete());
                });
            });
        };
    }
}
