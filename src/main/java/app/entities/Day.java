package app.entities;

import app.eums.Difficulty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String dayName;
    private String workoutType;
    private Difficulty difficulty;
    private int totalWorkoutTime;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "week_id")
    private Week week;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DayExercise> dayExercises = new ArrayList<>();

}
