package app.routes;

import app.Controllers.DayExerciseController;
import app.security.Roles;
import app.services.DayExerciseService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class DayExerciseRoutes {
    private final DayExerciseController dayExerciseController;

    public DayExerciseRoutes(DayExerciseService dayExerciseService) {
        this.dayExerciseController = new DayExerciseController(dayExerciseService);
    }

    public EndpointGroup getRoutes(){
        return () -> {
            path("dayExercise", () -> {
                // Public endpoints
                get(dayExerciseController.getAll(), Roles.ANYONE);
                get("{id}", dayExerciseController.getById(), Roles.USER);

                // Admin-only endpoints
                post(dayExerciseController.create(), Roles.ADMIN);
                put("{dayId}/{exerciseId}", dayExerciseController.update(), Roles.ADMIN);
                delete("{id}/{exerciseId}", dayExerciseController.delete(), Roles.ADMIN);
            });
        };
    }
}
