package app.dtos;

import app.eums.Difficulty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class ExerciseDTO {
    private Integer id;
    private String name;
    private String description;
    private String muscleGroup;
    private String equipment;
    private Difficulty difficulty;

    // TODO: skal den være der?
    //private List<DayExerciseDTO>dayExercises;

}
