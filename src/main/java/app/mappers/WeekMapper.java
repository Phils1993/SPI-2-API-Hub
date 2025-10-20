package app.mappers;

import app.dtos.WeekDTO;
import app.entities.Week;

import java.util.stream.Collectors;

public class WeekMapper {
    public static WeekDTO toDTO(Week week) {
        if (week == null) return null;

        WeekDTO dto = new WeekDTO();
        dto.setId(week.getId());
        dto.setWeekNumber(week.getWeekNumber());

        if (week.getDays() != null)
            dto.setDays(week.getDays()
                    .stream()
                    .map(DayMapper::toDTO)
                    .collect(Collectors.toList()));

        return dto;
    }

    public static Week toEntity(WeekDTO dto) {
        if (dto == null) return null;

        Week week = new Week();
        week.setId(dto.getId());
        week.setWeekNumber(dto.getWeekNumber());

        if (dto.getDays() != null)
            week.setDays(dto.getDays()
                    .stream()
                    .map(DayMapper::toEntity)
                    .collect(Collectors.toList()));

        return week;
    }
}
