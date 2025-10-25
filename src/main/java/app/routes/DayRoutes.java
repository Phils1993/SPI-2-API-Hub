package app.routes;

import app.Controllers.DayController;
import app.security.Roles;
import app.services.DayService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class DayRoutes {
    private final DayController dayController;

    public DayRoutes(DayService dayService) {
        this.dayController = new DayController(dayService);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("day", () -> {
                // Public endpoints
                get(dayController.getAll(), Roles.USER, Roles.ADMIN);
                get("{id}", dayController.getById(), Roles.USER, Roles.ADMIN);

                // Admin-only endpoints
                post(dayController.create(), Roles.ADMIN);
                put("{id}", dayController.update(), Roles.ADMIN);
                delete("{id}", dayController.delete(), Roles.ADMIN);
            });
        };
    }
}
