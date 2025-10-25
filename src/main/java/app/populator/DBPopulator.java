package app.populator;

import app.config.HibernateConfig;
import app.daos.DayDAO;
import app.daos.DayExerciseDAO;
import app.daos.ExerciseDAO;
import app.daos.WeekDAO;
import app.entities.Day;
import app.entities.DayExercise;
import app.entities.DayExerciseKey;
import app.entities.Exercise;
import app.entities.Week;
import app.eums.Difficulty;
import jakarta.persistence.EntityManagerFactory;

public class DBPopulator {

    private final WeekDAO weekDAO;
    private final DayDAO dayDAO;
    private final ExerciseDAO exerciseDAO;
    private final DayExerciseDAO dayExerciseDAO;

    public DBPopulator(EntityManagerFactory emf) {
        this.weekDAO = new WeekDAO(emf);
        this.dayDAO = new DayDAO(emf);
        this.exerciseDAO = new ExerciseDAO(emf);
        this.dayExerciseDAO = new DayExerciseDAO(emf);
    }

    public void populate() {
        // Exercises
        Exercise pushUp = new Exercise();
        pushUp.setName("Push Up");
        pushUp.setDescription("Upper body strength");
        pushUp.setMuscleGroup("Chest");
        pushUp.setEquipment("None");
        pushUp.setDifficulty(Difficulty.NORMAL);
        pushUp = exerciseDAO.create(pushUp);

        Exercise squat = new Exercise();
        squat.setName("Squat");
        squat.setDescription("Lower body strength");
        squat.setMuscleGroup("Legs");
        squat.setEquipment("None");
        squat.setDifficulty(Difficulty.EASY);
        squat = exerciseDAO.create(squat);

        Exercise plank = new Exercise();
        plank.setName("Plank");
        plank.setDescription("Core stability");
        plank.setMuscleGroup("Core");
        plank.setEquipment("None");
        plank.setDifficulty(Difficulty.HARD);
        plank = exerciseDAO.create(plank);

        Exercise hathaYoga = new Exercise();
        hathaYoga.setName("Hatha Yoga");
        hathaYoga.setDescription("Gentle yoga focusing on posture and breathing");
        hathaYoga.setMuscleGroup("Full Body");
        hathaYoga.setEquipment("Mat");
        hathaYoga.setDifficulty(Difficulty.EASY);
        hathaYoga = exerciseDAO.create(hathaYoga);

        Exercise yinYoga = new Exercise();
        yinYoga.setName("Yin Yoga");
        yinYoga.setDescription("Slow-paced yoga with long holds");
        yinYoga.setMuscleGroup("Flexibility");
        yinYoga.setEquipment("Mat");
        yinYoga.setDifficulty(Difficulty.EASY);
        yinYoga = exerciseDAO.create(yinYoga);

        Exercise vinyasaYoga = new Exercise();
        vinyasaYoga.setName("Vinyasa Flow");
        vinyasaYoga.setDescription("Dynamic yoga linking breath with movement");
        vinyasaYoga.setMuscleGroup("Full Body");
        vinyasaYoga.setEquipment("Mat");
        vinyasaYoga.setDifficulty(Difficulty.NORMAL);
        vinyasaYoga = exerciseDAO.create(vinyasaYoga);

        Exercise biking = new Exercise();
        biking.setName("Biking");
        biking.setDescription("Cardio endurance training");
        biking.setMuscleGroup("Legs");
        biking.setEquipment("Stationary bike");
        biking.setDifficulty(Difficulty.NORMAL);
        biking = exerciseDAO.create(biking);

        // Weeks
        Week week1 = new Week();
        week1.setWeekNumber(1);
        week1 = weekDAO.create(week1);

        Week week2 = new Week();
        week2.setWeekNumber(2);
        week2 = weekDAO.create(week2);

        Week week3 = new Week();
        week3.setWeekNumber(3);
        week3 = weekDAO.create(week3);

        // Days
        Day dayMondayStrengthW1 = new Day();
        dayMondayStrengthW1.setDayName("Monday");
        dayMondayStrengthW1.setWorkoutType("Strength");
        dayMondayStrengthW1.setTotalWorkoutTime(60);
        dayMondayStrengthW1.setDifficulty(Difficulty.NORMAL);
        dayMondayStrengthW1.setWeek(week1);
        dayMondayStrengthW1 = dayDAO.create(dayMondayStrengthW1);

        Day dayWednesdayCardioW1 = new Day();
        dayWednesdayCardioW1.setDayName("Wednesday");
        dayWednesdayCardioW1.setWorkoutType("Cardio");
        dayWednesdayCardioW1.setTotalWorkoutTime(45);
        dayWednesdayCardioW1.setDifficulty(Difficulty.EASY);
        dayWednesdayCardioW1.setWeek(week1);
        dayWednesdayCardioW1 = dayDAO.create(dayWednesdayCardioW1);

        Day dayFridayYogaW1 = new Day();
        dayFridayYogaW1.setDayName("Friday");
        dayFridayYogaW1.setWorkoutType("Yoga");
        dayFridayYogaW1.setTotalWorkoutTime(50);
        dayFridayYogaW1.setDifficulty(Difficulty.EASY);
        dayFridayYogaW1.setWeek(week1);
        dayFridayYogaW1 = dayDAO.create(dayFridayYogaW1);

        Day dayMondayYogaW2 = new Day();
        dayMondayYogaW2.setDayName("Monday");
        dayMondayYogaW2.setWorkoutType("Yoga");
        dayMondayYogaW2.setTotalWorkoutTime(60);
        dayMondayYogaW2.setDifficulty(Difficulty.EASY);
        dayMondayYogaW2.setWeek(week2);
        dayMondayYogaW2 = dayDAO.create(dayMondayYogaW2);

        Day dayThursdayStrengthW2 = new Day();
        dayThursdayStrengthW2.setDayName("Thursday");
        dayThursdayStrengthW2.setWorkoutType("Strength");
        dayThursdayStrengthW2.setTotalWorkoutTime(70);
        dayThursdayStrengthW2.setDifficulty(Difficulty.HARD);
        dayThursdayStrengthW2.setWeek(week2);
        dayThursdayStrengthW2 = dayDAO.create(dayThursdayStrengthW2);

        Day daySaturdayIntervalW3 = new Day();
        daySaturdayIntervalW3.setDayName("Saturday");
        daySaturdayIntervalW3.setWorkoutType("4:4 Interval Training");
        daySaturdayIntervalW3.setTotalWorkoutTime(32); // 4x4 minutes biking + rests
        daySaturdayIntervalW3.setDifficulty(Difficulty.NORMAL);
        daySaturdayIntervalW3.setWeek(week3);
        daySaturdayIntervalW3 = dayDAO.create(daySaturdayIntervalW3);

        // DayExercises (named by day for clarity)
        // Week 1 - Monday Strength
        DayExercise dayMondayStrength_PushUps = new DayExercise();
        dayMondayStrength_PushUps.setId(new DayExerciseKey(dayMondayStrengthW1.getId(), pushUp.getId()));
        dayMondayStrength_PushUps.setDay(dayMondayStrengthW1);
        dayMondayStrength_PushUps.setExercise(pushUp);
        dayMondayStrength_PushUps.setSets(4);
        dayMondayStrength_PushUps.setReps(12);
        dayMondayStrength_PushUps.setDurationSeconds(0);
        dayExerciseDAO.create(dayMondayStrength_PushUps);

        DayExercise dayMondayStrength_Squats = new DayExercise();
        dayMondayStrength_Squats.setId(new DayExerciseKey(dayMondayStrengthW1.getId(), squat.getId()));
        dayMondayStrength_Squats.setDay(dayMondayStrengthW1);
        dayMondayStrength_Squats.setExercise(squat);
        dayMondayStrength_Squats.setSets(4);
        dayMondayStrength_Squats.setReps(15);
        dayMondayStrength_Squats.setDurationSeconds(0);
        dayExerciseDAO.create(dayMondayStrength_Squats);

        // Week 1 - Wednesday Cardio
        DayExercise dayWednesdayCardio_Plank = new DayExercise();
        dayWednesdayCardio_Plank.setId(new DayExerciseKey(dayWednesdayCardioW1.getId(), plank.getId()));
        dayWednesdayCardio_Plank.setDay(dayWednesdayCardioW1);
        dayWednesdayCardio_Plank.setExercise(plank);
        dayWednesdayCardio_Plank.setSets(3);
        dayWednesdayCardio_Plank.setReps(0);
        dayWednesdayCardio_Plank.setDurationSeconds(60);
        dayExerciseDAO.create(dayWednesdayCardio_Plank);

        // Week 1 - Friday Yoga
        DayExercise dayFridayYoga_Hatha = new DayExercise();
        dayFridayYoga_Hatha.setId(new DayExerciseKey(dayFridayYogaW1.getId(), hathaYoga.getId()));
        dayFridayYoga_Hatha.setDay(dayFridayYogaW1);
        dayFridayYoga_Hatha.setExercise(hathaYoga);
        dayFridayYoga_Hatha.setSets(1);
        dayFridayYoga_Hatha.setReps(0);
        dayFridayYoga_Hatha.setDurationSeconds(1800); // 30 min
        dayExerciseDAO.create(dayFridayYoga_Hatha);

        DayExercise dayFridayYoga_Yin = new DayExercise();
        dayFridayYoga_Yin.setId(new DayExerciseKey(dayFridayYogaW1.getId(), yinYoga.getId()));
        dayFridayYoga_Yin.setDay(dayFridayYogaW1);
        dayFridayYoga_Yin.setExercise(yinYoga);
        dayFridayYoga_Yin.setSets(1);
        dayFridayYoga_Yin.setReps(0);
        dayFridayYoga_Yin.setDurationSeconds(1800); // 30 min
        dayExerciseDAO.create(dayFridayYoga_Yin);

        // Week 2 - Monday Yoga
        DayExercise dayMondayYoga_Vinyasa = new DayExercise();
        dayMondayYoga_Vinyasa.setId(new DayExerciseKey(dayMondayYogaW2.getId(), vinyasaYoga.getId()));
        dayMondayYoga_Vinyasa.setDay(dayMondayYogaW2);
        dayMondayYoga_Vinyasa.setExercise(vinyasaYoga);
        dayMondayYoga_Vinyasa.setSets(1);
        dayMondayYoga_Vinyasa.setReps(0);
        dayMondayYoga_Vinyasa.setDurationSeconds(3600); // 60 min
        dayExerciseDAO.create(dayMondayYoga_Vinyasa);

        // Week 2 - Thursday Strength
        DayExercise dayThursdayStrength_PushUps = new DayExercise();
        dayThursdayStrength_PushUps.setId(new DayExerciseKey(dayThursdayStrengthW2.getId(), pushUp.getId()));
        dayThursdayStrength_PushUps.setDay(dayThursdayStrengthW2);
        dayThursdayStrength_PushUps.setExercise(pushUp);
        dayThursdayStrength_PushUps.setSets(5);
        dayThursdayStrength_PushUps.setReps(15);
        dayThursdayStrength_PushUps.setDurationSeconds(0);
        dayExerciseDAO.create(dayThursdayStrength_PushUps);

        DayExercise dayThursdayStrength_Squats = new DayExercise();
        dayThursdayStrength_Squats.setId(new DayExerciseKey(dayThursdayStrengthW2.getId(), squat.getId()));
        dayThursdayStrength_Squats.setDay(dayThursdayStrengthW2);
        dayThursdayStrength_Squats.setExercise(squat);
        dayThursdayStrength_Squats.setSets(5);
        dayThursdayStrength_Squats.setReps(20);
        dayThursdayStrength_Squats.setDurationSeconds(0);
        dayExerciseDAO.create(dayThursdayStrength_Squats);

        DayExercise dayThursdayStrength_Plank = new DayExercise();
        dayThursdayStrength_Plank.setId(new DayExerciseKey(dayThursdayStrengthW2.getId(), plank.getId()));
        dayThursdayStrength_Plank.setDay(dayThursdayStrengthW2);
        dayThursdayStrength_Plank.setExercise(plank);
        dayThursdayStrength_Plank.setSets(4);
        dayThursdayStrength_Plank.setReps(0);
        dayThursdayStrength_Plank.setDurationSeconds(90); // fixed: ensure creation and duration set
        dayExerciseDAO.create(dayThursdayStrength_Plank);

        // Week 3 - Saturday 4:4 Interval (4 min biking × 4 rounds)
        DayExercise daySaturdayInterval_Biking = new DayExercise();
        daySaturdayInterval_Biking.setId(new DayExerciseKey(daySaturdayIntervalW3.getId(), biking.getId()));
        daySaturdayInterval_Biking.setDay(daySaturdayIntervalW3);
        daySaturdayInterval_Biking.setExercise(biking);
        daySaturdayInterval_Biking.setSets(4);                 // 4 rounds
        daySaturdayInterval_Biking.setReps(0);
        daySaturdayInterval_Biking.setDurationSeconds(240);    // 4 minutes per round
        dayExerciseDAO.create(daySaturdayInterval_Biking);

        System.out.println("Database populated successfully");
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        DBPopulator dbPopulator = new DBPopulator(emf);
        dbPopulator.populate();
    }
}
