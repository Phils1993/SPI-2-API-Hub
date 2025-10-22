package app.services;

import app.daos.DayDAO;
import app.daos.DayExerciseDAO;
import app.daos.ExerciseDAO;
import app.dtos.DayExerciseDTO;
import app.entities.Day;
import app.entities.DayExercise;
import app.entities.DayExerciseKey;
import app.entities.Exercise;
import app.exceptions.ApiException;
import app.mappers.DayExerciseMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DayExerciseService {
    private final DayExerciseDAO dayExerciseDAO;
    private final DayDAO dayDAO;
    private final ExerciseDAO exerciseDAO;

    public DayExerciseService(EntityManagerFactory emf) {
        this.dayExerciseDAO = new DayExerciseDAO(emf);
        this.exerciseDAO = new ExerciseDAO(emf);
        this.dayDAO = new DayDAO(emf);
    }

    public DayExerciseDTO addExerciseToDay(DayExerciseDTO dto, int dayId, int exerciseId) {
        // Fetch the managed Day and Exercise entities first
        Day day = dayDAO.getById(dayId);
        Exercise exercise = exerciseDAO.getById(exerciseId);

        if (day == null) throw new ApiException(404, "Day not found");
        if (exercise == null) throw new ApiException(404, "Exercise not found");

        // Create DayExercise Entity
        DayExercise entity = new DayExercise();
        entity.setId(new DayExerciseKey(dayId, exerciseId));
        entity.setDay(day);
        entity.setExercise(exercise);
        entity.setSets(dto.getSets());
        entity.setReps(dto.getReps());
        entity.setDurationSeconds(dto.getDurationSeconds());

        // Persist it via DAO method
        DayExercise saved = dayExerciseDAO.create(entity);
        return DayExerciseMapper.toDTO(saved);
    }

    public List<DayExerciseDTO> getExercisesForDay(int dayId) {
        return dayExerciseDAO.getExercisesByDayId(dayId)
                .stream()
                .map(DayExerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DayExerciseDTO updateDayExercise(int dayId, int exerciseId, DayExerciseDTO dto) {
        DayExercise updatedEntity = DayExerciseMapper.toEntity(dto);
        DayExerciseKey key = new DayExerciseKey(dayId, exerciseId);

        DayExercise updated = dayExerciseDAO.updateByKey(key, updatedEntity);
        return DayExerciseMapper.toDTO(updated);
    }

    public void removeExerciseFromDay(int dayId, int exerciseId) {
        DayExerciseKey key = new DayExerciseKey(dayId, exerciseId);
        dayExerciseDAO.deleteByKey(key);
    }
}
