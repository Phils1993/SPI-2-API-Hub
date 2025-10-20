package app.mappers;

import app.dtos.ExerciseDTO;
import app.entities.Exercise;

public class ExerciseMapper {
    public static ExerciseDTO toDTO(Exercise exercise) {
        if (exercise == null) return null;
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getMuscleGroup(),
                exercise.getEquipment(),
                exercise.getDifficulty()
        );
    }

    public static Exercise toEntity(ExerciseDTO dto) {
        if (dto == null) return null;
        Exercise exercise = new Exercise();
        exercise.setId(dto.getId());
        exercise.setName(dto.getName());
        exercise.setDescription(dto.getDescription());
        exercise.setMuscleGroup(dto.getMuscleGroup());
        exercise.setEquipment(dto.getEquipment());
        exercise.setDifficulty(dto.getDifficulty());
        return exercise;
    }
}
