package app.dtos;

import app.entities.Exercise;
import lombok.*;

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
}
