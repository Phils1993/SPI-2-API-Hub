package app.routes;

import app.Controllers.DayController;
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
                post(dayController.create());
            });
        };
    }
}
