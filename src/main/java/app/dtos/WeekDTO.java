package app.dtos;


import app.entities.Day;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class WeekDTO {
    private Integer id;
    private int weekNumber;
    private List<DayDTO> days;
}
