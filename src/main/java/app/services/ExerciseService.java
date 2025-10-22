package app.services;

import app.daos.ExerciseDAO;
import app.dtos.ExerciseDTO;
import app.entities.Exercise;
import app.mappers.ExerciseMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ExerciseService {
    private final ExerciseDAO exerciseDAO;

    public ExerciseService(EntityManagerFactory emf) {
        this.exerciseDAO = new ExerciseDAO(emf);
    }

    public ExerciseDTO getById(int id) {
        Exercise exercise = exerciseDAO.getById(id);
        return ExerciseMapper.toDTO(exercise);
    }

    public List<ExerciseDTO> getAll() {
        return exerciseDAO.getAll().stream()
                .map(ExerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ExerciseDTO update(ExerciseDTO exerciseDTO, int id) {
        Exercise updatedExercise = ExerciseMapper.toEntity(exerciseDTO);
        Exercise updated = exerciseDAO.update(id, updatedExercise);
        return ExerciseMapper.toDTO(updated);
    }

    public void delete(int id) {
        exerciseDAO.deleteById(id);
    }
}
