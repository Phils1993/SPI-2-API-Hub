package app.mappers;

import app.dtos.DayExerciseDTO;
import app.entities.DayExercise;

public class DayExerciseMapper {
    public static DayExerciseDTO toDTO(DayExercise entity) {
        if (entity == null) return null;

        DayExerciseDTO dto = new DayExerciseDTO();
        dto.setDayId(entity.getDay().getId());
        dto.setExerciseId(entity.getExercise().getId());
        dto.setSets(entity.getSets());
        dto.setReps(entity.getReps());
        dto.setDurationSeconds(entity.getDurationSeconds());
        dto.setExercise(ExerciseMapper.toDTO(entity.getExercise()));

        return dto;
    }

    public static DayExercise toEntity(DayExerciseDTO dto) {
        if (dto == null) return null;

        DayExercise entity = new DayExercise();
        entity.setSets(dto.getSets());
        entity.setReps(dto.getReps());
        entity.setDurationSeconds(dto.getDurationSeconds());
        // We'll set Day and Exercise separately in the service layer (since they’re managed entities)
        return entity;
    }
}
