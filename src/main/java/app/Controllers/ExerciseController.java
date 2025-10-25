package app.Controllers;

import io.javalin.http.Handler;
import app.dtos.ExerciseDTO;
import app.services.ExerciseService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExerciseController implements IController{
    private final ExerciseService exerciseService;

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Override
    public Handler create() {
        return (Context ctx) -> {
            ExerciseDTO exerciseDTO = ctx.bodyAsClass(ExerciseDTO.class);
            ExerciseDTO newExercise = exerciseService.create(exerciseDTO);
            ctx.status(HttpStatus.CREATED).json(newExercise);
        };
    }

    @Override
    public Handler getAll() {
        return (Context ctx) -> {
            List<ExerciseDTO> exercises = exerciseService.getAll();
            ctx.status(HttpStatus.OK).json(exercises);
        };
    }

    @Override
    public Handler update() {
        return (Context ctx) -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ExerciseDTO exerciseDTO = ctx.bodyAsClass(ExerciseDTO.class);
            ExerciseDTO updated = exerciseService.update(exerciseDTO, id);
            ctx.status(HttpStatus.OK).json(updated);
        };
    }

    @Override
    public Handler delete() {
        return (Context ctx) -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            exerciseService.delete(id);
            ctx.status(HttpStatus.NO_CONTENT).json("Exercise deleted successfully");
        };
    }

    @Override
    public Handler getById() {
        return (Context ctx) -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ExerciseDTO exercise = exerciseService.getById(id);
            ctx.status(HttpStatus.OK).json(exercise);
        };
    }
}
