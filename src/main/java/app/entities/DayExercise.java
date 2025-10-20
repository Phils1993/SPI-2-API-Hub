package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayExercise {
    @EmbeddedId
    private DayExerciseKey id = new DayExerciseKey();

    @ManyToOne
    @MapsId("dayId")
    @JoinColumn(name = "day_id")
    private Day day;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int sets;
    private int reps;
    private int durationSeconds;
}
