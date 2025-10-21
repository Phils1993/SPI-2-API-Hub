package app.routes;

import app.Controllers.DayExerciseController;
import app.services.DayExerciseService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class DayExerciseRoutes {
    private final DayExerciseController dayExerciseController;

    public DayExerciseRoutes(DayExerciseService dayExerciseService) {
        this.dayExerciseController = new DayExerciseController(dayExerciseService);
    }

    public EndpointGroup getRoutes(){
        return () -> {
            path("dayExercise", () -> {
                post(dayExerciseController.create());
            });
        };
    }
}
