package app.routes;

import app.Controllers.WeekController;
import app.security.Roles;
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
                post(weekController.create(), Roles.ADMIN);
                get(weekController.getAll(), Roles.USER,Roles.ADMIN);

                path("{id}", () -> {
                    get(weekController.getById(), Roles.USER,Roles.ADMIN);
                    put(weekController.update(), Roles.ADMIN);
                    delete(weekController.delete(), Roles.ADMIN);
                });
            });
        };
    }
}
