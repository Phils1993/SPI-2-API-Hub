package app.routes;

import app.Controllers.ExerciseController;
import app.security.Roles;
import app.services.ExerciseService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ExerciseRoutes {

    private final ExerciseController exerciseController;

    public ExerciseRoutes(ExerciseService exerciseService) {
        this.exerciseController = new ExerciseController(exerciseService);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("exercise", () -> {
                post(exerciseController.create(), Roles.ADMIN);
                get(exerciseController.getAll(), Roles.USER, Roles.ADMIN);

                path("{id}", () -> {
                    get(exerciseController.getById(), Roles.USER, Roles.ADMIN);
                    put(exerciseController.update(), Roles.ADMIN);
                    delete(exerciseController.delete(), Roles.ADMIN);
                });
            });
        };
    }
}
