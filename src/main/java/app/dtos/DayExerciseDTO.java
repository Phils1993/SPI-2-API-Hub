package app.dtos;

import app.entities.Exercise;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DayExerciseDTO {
    private Integer dayId;
    private Integer exerciseId;

    private int sets;
    private int reps;
    private int durationSeconds;

    private ExerciseDTO exercise;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayExerciseDTO)) return false;
        DayExerciseDTO that = (DayExerciseDTO) o;
        return dayId == that.dayId &&
                exerciseId == that.exerciseId &&
                sets == that.sets &&
                reps == that.reps &&
                durationSeconds == that.durationSeconds &&
                Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayId, exerciseId, sets, reps, durationSeconds, exercise);
    }

}
