package app.config;

import app.services.*;
import jakarta.persistence.EntityManagerFactory;

public class ServiceRegistry {

    public final WeekService weekService;
    public final DayService dayService;
    public final ExerciseService exerciseService;
    public final DayExerciseService dayExerciseService;

    public ServiceRegistry(EntityManagerFactory emf) {
        this.weekService = new WeekService(emf);
        this.dayService = new DayService(emf);
        this.exerciseService = new ExerciseService(emf);
        this.dayExerciseService = new DayExerciseService(emf);
    }
}
