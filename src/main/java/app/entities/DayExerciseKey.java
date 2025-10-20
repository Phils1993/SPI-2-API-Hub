package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DayExerciseKey implements Serializable {
    @Column(name = "day_id")
    private Integer dayId;
    @Column(name = "exercise_id")
    private Integer exerciseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayExerciseKey)) return false;
        DayExerciseKey that = (DayExerciseKey) o;
        return Objects.equals(dayId, that.dayId) && Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayId, exerciseId);
    }

}
