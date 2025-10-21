package app.controllers;

import app.dtos.WeekDTO;
import app.services.WeekService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

import java.util.List;

public class WeekController implements IController{

    private final WeekService weekService;
    public WeekController(WeekService weekService) {
        this.weekService = weekService;
    }

    @Override
    public Handler create() {
        return (Context ctx) -> {
            WeekDTO weekDTO = ctx.bodyAsClass(WeekDTO.class);
            WeekDTO newWeekDTO = weekService.create(weekDTO);
            ctx.status(HttpStatus.CREATED).json(newWeekDTO);
        };
    }

    @Override
    public Handler getAll() {
        return (Context ctx) -> {
            List<WeekDTO> weekDTOs = weekService.getAll();
            ctx.status(HttpStatus.OK).json(weekDTOs);
        };
    }

    @Override
    public Handler update() {
        return (Context ctx) -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            WeekDTO weekDTO = ctx.bodyAsClass(WeekDTO.class);
            WeekDTO updatedWeek = weekService.update(weekDTO, id);
            ctx.status(HttpStatus.OK).json(updatedWeek);
        };
    }

    @Override
    public Handler delete() {
        return (Context ctx) -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            weekService.delete(id);
            ctx.status(HttpStatus.NO_CONTENT).json("Week deleted successfully");
        };
    }

    @Override
    public Handler getById() {
        return (Context ctx) -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            WeekDTO weekDTO = weekService.getById(id);
            ctx.status(HttpStatus.OK).json(weekDTO);
        };
    }
}
