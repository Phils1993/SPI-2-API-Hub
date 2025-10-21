package app.populator;

import app.Config.HibernateConfig;
import app.daos.DayExerciseDAO;
import app.daos.DayDAO;
import app.daos.ExerciseDAO;
import app.daos.WeekDAO;
import app.entities.Day;
import app.entities.DayExercise;
import app.entities.DayExerciseKey;
import app.entities.Exercise;
import app.entities.Week;
import app.eums.Difficulty;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;

public class DBPopulator {

    private final WeekDAO weekDAO;
    private final DayDAO dayDAO;
    private final ExerciseDAO exerciseDAO;
    private final DayExerciseDAO dayExerciseDAO;

    public DBPopulator(EntityManagerFactory emf) {
        this.weekDAO = WeekDAO.getInstance(emf);
        this.dayDAO = DayDAO.getInstance(emf);
        this.exerciseDAO = ExerciseDAO.getInstance(emf);
        this.dayExerciseDAO = DayExerciseDAO.getInstance(emf);
    }

    public void populate() {
        // Create Exercises
        Exercise ex1 = new Exercise();
        ex1.setName("Push Up");
        ex1.setDescription("Upper body strength");
        ex1.setMuscleGroup("Chest");
        ex1.setEquipment("None");
        ex1.setDifficulty(Difficulty.NORMAL);
        exerciseDAO.create(ex1);

        Exercise ex2 = new Exercise();
        ex2.setName("Squat");
        ex2.setDescription("Lower body strength");
        ex2.setMuscleGroup("Legs");
        ex2.setEquipment("None");
        ex2.setDifficulty(Difficulty.EASY);
        exerciseDAO.create(ex2);

        Exercise ex3 = new Exercise();
        ex3.setName("Plank");
        ex3.setDescription("Core stability");
        ex3.setMuscleGroup("Core");
        ex3.setEquipment("None");
        ex3.setDifficulty(Difficulty.HARD);
        exerciseDAO.create(ex3);

        // Create Weeks
        Week week1 = new Week();
        week1.setWeekNumber(1);
        weekDAO.create(week1);

        Week week2 = new Week();
        week2.setWeekNumber(2);
        weekDAO.create(week2);

        // Create Days for Week 1
        Day day1 = new Day();
        day1.setDayName("Monday");
        day1.setWorkoutType("Strength");
        day1.setTotalWorkoutTime(60);
        day1.setDifficulty(Difficulty.NORMAL);
        day1.setWeek(week1);
        dayDAO.create(day1);

        Day day2 = new Day();
        day2.setDayName("Wednesday");
        day2.setWorkoutType("Cardio");
        day2.setTotalWorkoutTime(45);
        day2.setDifficulty(Difficulty.EASY);
        day2.setWeek(week1);
        dayDAO.create(day2);


        // Create DayExercises
        DayExercise de1 = new DayExercise();
        de1.setId(new DayExerciseKey(day1.getId(), ex1.getId()));
        de1.setDay(day1);
        de1.setExercise(ex1);
        de1.setSets(4);
        de1.setReps(12);
        de1.setDurationSeconds(0);
        dayExerciseDAO.create(de1);

        DayExercise de2 = new DayExercise();
        de2.setId(new DayExerciseKey(day2.getId(), ex3.getId()));
        de2.setDay(day2);
        de2.setExercise(ex3);
        de2.setSets(3);
        de2.setReps(0);
        de2.setDurationSeconds(60);
        dayExerciseDAO.create(de2);

        System.out.println("Database populated successfully!");
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        DBPopulator dbPopulator = new DBPopulator(emf);
        dbPopulator.populate();
    }
}
