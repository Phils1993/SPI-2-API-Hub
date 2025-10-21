package app.Controllers;

import app.dtos.DayDTO;
import app.services.DayService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

import java.util.List;

public class DayController implements IController {

    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    @Override
    public Handler create() {
        return (Context ctx) -> {
            DayDTO dayDTO = ctx.bodyAsClass(DayDTO.class);
            if (dayDTO.getWeekId() == null) {
                ctx.status(HttpStatus.BAD_REQUEST).json("Week ID is required");
                return;
            }
            DayDTO newDayDTO = dayService.create(dayDTO, dayDTO.getWeekId());
            ctx.status(HttpStatus.CREATED).json(newDayDTO);
        };
    }

    @Override
    public Handler getAll() {
        return (Context ctx) -> {
            List<DayDTO> dayDTOs = dayService.getAll();
            ctx.status(HttpStatus.OK).json(dayDTOs);
        };
    }

    @Override
    public Handler update() {
        return (Context ctx) -> {
            int dayId = Integer.parseInt(ctx.pathParam("id"));
            DayDTO dayDTO = ctx.bodyAsClass(DayDTO.class);
            DayDTO newDayDTO = dayService.update(dayDTO, dayId);
            ctx.status(HttpStatus.OK).json(newDayDTO);
        };
    }

    @Override
    public Handler delete() {
        return (Context ctx) -> {
            int dayId = Integer.parseInt(ctx.pathParam("id"));
            dayService.delete(dayId);
            ctx.status(HttpStatus.NO_CONTENT).json("Day deleted successfully");
        };
    }

    @Override
    public Handler getById() {
        return (Context ctx) -> {
            int dayId = Integer.parseInt(ctx.pathParam("id"));
            DayDTO dayDTO = dayService.getById(dayId);
            ctx.status(HttpStatus.OK).json(dayDTO);
        };
    }
}
