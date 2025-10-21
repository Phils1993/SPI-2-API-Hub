package app.mappers;

import app.dtos.DayDTO;
import app.entities.Day;

import java.util.stream.Collectors;

public class DayMapper {
    public static DayDTO toDTO(Day day, boolean includedExercises) {
        if (day == null) return null;

        DayDTO dto = new DayDTO();
        dto.setId(day.getId());
        dto.setDayName(day.getDayName());
        dto.setWorkoutType(day.getWorkoutType());
        dto.setDifficulty(day.getDifficulty());
        dto.setTotalWorkoutTime(day.getTotalWorkoutTime());

        if (includedExercises && day.getDayExercises() != null)
            dto.setDayExercises(day.getDayExercises()
                    .stream()
                    .map(DayExerciseMapper::toDTO)
                    .collect(Collectors.toList()));

        return dto;
    }
    // Default mapping (no exercises)
    public static DayDTO toDTO(Day day) {
        return toDTO(day, false);
    }

    public static Day toEntity(DayDTO dto) {
        if (dto == null) return null;

        Day day = new Day();
        day.setId(dto.getId());
        day.setDayName(dto.getDayName());
        day.setWorkoutType(dto.getWorkoutType());
        day.setDifficulty(dto.getDifficulty());
        day.setTotalWorkoutTime(dto.getTotalWorkoutTime());
        // DayExercises will be mapped and linked later in service
        return day;
    }
}
