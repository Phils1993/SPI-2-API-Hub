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
        Week existing = weekDAO.getById(id);
        if (existing == null) {
            throw new RuntimeException("Week not found");
        }
        if (dto.getWeekNumber() != 0) {
            existing.setWeekNumber(dto.getWeekNumber());
        }
        Week updated = weekDAO.update(id, existing);
        return WeekMapper.toDTO(updated);
    }

    public void delete(int id) {
        weekDAO.deleteById(id);
    }

}
