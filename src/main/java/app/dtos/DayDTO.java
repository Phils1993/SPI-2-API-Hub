package app.dtos;

import app.entities.Day;
import app.entities.DayExercise;
import app.eums.Difficulty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DayDTO {
    private Integer weekId;
    private Integer id;
    private String dayName;
    private String workoutType;
    private Difficulty difficulty;
    private int totalWorkoutTime;

    private List<DayExerciseDTO>dayExercises = new ArrayList<>();

}
