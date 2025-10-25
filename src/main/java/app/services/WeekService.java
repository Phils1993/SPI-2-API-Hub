package app.services;

import app.daos.WeekDAO;
import app.dtos.WeekDTO;
import app.entities.Week;
import app.mappers.WeekMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class WeekService {
    private final WeekDAO weekDAO;

    public WeekService(EntityManagerFactory emf) {
        this.weekDAO = new WeekDAO(emf);
    }

    public WeekDTO create(WeekDTO dto) {
        Week entity = WeekMapper.toEntity(dto);
        Week saved = weekDAO.create(entity);
        return WeekMapper.toDTO(saved);
    }

    public WeekDTO getById(int id) {
        Week week = weekDAO.getById(id);
        return WeekMapper.toDTO(week);
    }

    public List<WeekDTO> getAll() {
        return weekDAO.getAll().stream()
                .map(WeekMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WeekDTO update(WeekDTO dto, int id) {
        // Update only the fields you care about
        if (dto.getWeekNumber() != 0) {
            Week updatedWeek = new Week();
            updatedWeek.setWeekNumber(dto.getWeekNumber());
            weekDAO.update(id, updatedWeek);
        }
        // Re-fetch with JOIN FETCH so lazy collections are initialized
        Week refreshed = weekDAO.getById(id);
        return WeekMapper.toDTO(refreshed);
    }

    public void delete(int id) {
        weekDAO.deleteById(id);
    }

}
