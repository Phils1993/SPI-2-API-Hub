package app.services;

import app.daos.DayExerciseDAO;
import app.dtos.DayExerciseDTO;
import app.entities.DayExercise;
import app.entities.DayExerciseKey;
import app.mappers.DayExerciseMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DayExerciseService {
    private final DayExerciseDAO dayExerciseDAO;

    public DayExerciseService(EntityManagerFactory emf) {
        this.dayExerciseDAO = DayExerciseDAO.getInstance(emf);
    }

    public DayExerciseDTO addExerciseToDay(DayExerciseDTO dto, int dayId, int exerciseId) {
        DayExercise entity = DayExerciseMapper.toEntity(dto);
        entity.setId(new DayExerciseKey(dayId, exerciseId));

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
