package PopulatorTest;

import app.config.HibernateConfig;
import app.daos.DayDAO;
import app.daos.DayExerciseDAO;
import app.daos.ExerciseDAO;
import app.daos.WeekDAO;
import app.entities.Day;
import app.entities.DayExercise;
import app.entities.Exercise;
import app.entities.Week;
import app.eums.Difficulty;
import jakarta.persistence.EntityManagerFactory;

public class PopulatorTest {

    private final WeekDAO weekDAO;
    private final DayDAO dayDAO;
    private final ExerciseDAO exerciseDAO;
    private final DayExerciseDAO dayExerciseDAO;

    //  Keep references so tests can use them
    public Exercise ex1;
    public Exercise ex2;
    public Exercise ex3;
    public Week week1;
    public Week week2;
    public Day day1;
    public Day day2;

    public PopulatorTest(EntityManagerFactory emf) {
        this.weekDAO = new WeekDAO(emf);
        this.dayDAO = new DayDAO(emf);
        this.exerciseDAO = new ExerciseDAO(emf);
        this.dayExerciseDAO = new DayExerciseDAO(emf);
    }

    public void populate() {
        // Create Exercises
        ex1 = new Exercise();
        ex1.setName("Push Up");
        ex1.setDescription("Upper body strength");
        ex1.setMuscleGroup("Chest");
        ex1.setEquipment("None");
        ex1.setDifficulty(Difficulty.NORMAL);
        ex1 = exerciseDAO.create(ex1);

        ex2 = new Exercise();
        ex2.setName("Squat");
        ex2.setDescription("Lower body strength");
        ex2.setMuscleGroup("Legs");
        ex2.setEquipment("None");
        ex2.setDifficulty(Difficulty.EASY);
        ex2 = exerciseDAO.create(ex2);

        ex3 = new Exercise();
        ex3.setName("Plank");
        ex3.setDescription("Core stability");
        ex3.setMuscleGroup("Core");
        ex3.setEquipment("None");
        ex3.setDifficulty(Difficulty.HARD);
        ex3 = exerciseDAO.create(ex3);

        // Create Weeks
        week1 = new Week();
        week1.setWeekNumber(1);
        week1 = weekDAO.create(week1);

        week2 = new Week();
        week2.setWeekNumber(2);
        week2 = weekDAO.create(week2);

        // Create Days for Week 1
        day1 = new Day();
        day1.setDayName("Monday");
        day1.setWorkoutType("Strength");
        day1.setTotalWorkoutTime(60);
        day1.setDifficulty(Difficulty.NORMAL);
        day1.setWeek(week1);
        day1 = dayDAO.create(day1);

        day2 = new Day();
        day2.setDayName("Wednesday");
        day2.setWorkoutType("Cardio");
        day2.setTotalWorkoutTime(45);
        day2.setDifficulty(Difficulty.EASY);
        day2.setWeek(week1);
        day2 = dayDAO.create(day2);

        // Create DayExercises
        DayExercise de1 = new DayExercise();
        de1.setDay(day1);
        de1.setExercise(ex1);
        de1.setSets(4);
        de1.setReps(12);
        de1.setDurationSeconds(0);
        dayExerciseDAO.create(de1);

        DayExercise de2 = new DayExercise();
        de2.setDay(day2);
        de2.setExercise(ex3);
        de2.setSets(3);
        de2.setReps(0);
        de2.setDurationSeconds(60);
        dayExerciseDAO.create(de2);

        System.out.println("Database populated successfully!");
        System.out.println("Exercise IDs: " + ex1.getId() + ", " + ex2.getId() + ", " + ex3.getId());
        System.out.println("Week IDs: " + week1.getId() + ", " + week2.getId());
        System.out.println("Day IDs: " + day1.getId() + ", " + day2.getId());
    }
}
