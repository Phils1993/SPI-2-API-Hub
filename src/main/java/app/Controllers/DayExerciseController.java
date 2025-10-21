package app.Controllers;

import app.dtos.DayExerciseDTO;
import app.services.DayExerciseService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class DayExerciseController implements IController{
    private final DayExerciseService dayExerciseService;

    public DayExerciseController(DayExerciseService dayExerciseService) {
        this.dayExerciseService = dayExerciseService;
    }

    @Override
    public Handler create() {
        return (Context ctx) -> {
            DayExerciseDTO dayExerciseDTO = ctx.bodyAsClass(DayExerciseDTO.class);
            int dayId = dayExerciseDTO.getDayId();
            int exerciseId = dayExerciseDTO.getExerciseId();

            DayExerciseDTO created = dayExerciseService.addExerciseToDay(dayExerciseDTO,dayId,exerciseId);
            ctx.status(HttpStatus.CREATED).json(created);
        };
    }

    @Override
    public Handler getAll() {
        return null;
    }

    @Override
    public Handler update() {
        return null;
    }

    @Override
    public Handler delete() {
        return null;
    }

    @Override
    public Handler getById() {
        return null;
    }
}
