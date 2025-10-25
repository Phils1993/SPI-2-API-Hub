package app.Controllers;

import app.dtos.DayExerciseDTO;
import app.services.DayExerciseService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DayExerciseController implements IController{
    private final DayExerciseService dayExerciseService;

    // Logger til generel logging
    private static final Logger logger = LoggerFactory.getLogger(DayExerciseController.class);

    // Logger til debug-specifik information (kan bruges til tracing)
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

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
        return ctx -> {
            List<DayExerciseDTO> allDayExercise = dayExerciseService.getAllDayExercises();
            ctx.status(HttpStatus.OK).json(allDayExercise);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int dayId = Integer.parseInt(ctx.pathParam("dayId"));
            int exerciseId = Integer.parseInt(ctx.pathParam("exerciseId"));
            DayExerciseDTO updatedDTO = ctx.bodyAsClass(DayExerciseDTO.class);

            DayExerciseDTO updatedDay = dayExerciseService.updateDayExercise(dayId, exerciseId, updatedDTO);
            ctx.status(HttpStatus.OK).json(updatedDay);
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int dayId = Integer.parseInt(ctx.pathParam("id"));
            int exerciseId = Integer.parseInt(ctx.pathParam("exerciseId"));
            dayExerciseService.removeExerciseFromDay(dayId, exerciseId);
            ctx.status(HttpStatus.NO_CONTENT).result("DayExercise deleted successfully");
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int dayId = Integer.parseInt(ctx.pathParam("id"));
            List<DayExerciseDTO> dayExercise = dayExerciseService.getExercisesForDay(dayId);
            ctx.status(HttpStatus.OK).json(dayExercise);
        };
    }
}
