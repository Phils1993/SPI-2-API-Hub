package app.services;

import app.daos.DayDAO;
import app.dtos.DayDTO;
import app.entities.Day;
import app.mappers.DayMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DayService {
    private final DayDAO dayDAO;

    public DayService(EntityManagerFactory emf) {
        this.dayDAO = DayDAO.getInstance(emf);
    }
    public DayDTO create(DayDTO dto, int weekId) {
        Day day = DayMapper.toEntity(dto);
        Day saved = dayDAO.Create(day, weekId);
        return DayMapper.toDTO(saved);
    }

    public DayDTO getById(int id) {
        //Day day = dayDAO.getById(id);
        Day day = dayDAO.getByIdOrThrow(id);
        return DayMapper.toDTO(day,true);
    }

    public List<DayDTO> getAll() {
        return dayDAO.getAll().stream()
                .map(DayMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DayDTO update(DayDTO dto, int id) {
        Day updatedDay = DayMapper.toEntity(dto);
        Day updated = dayDAO.update(id, updatedDay);
        return DayMapper.toDTO(updated);
    }

    public void delete(int id) {
        dayDAO.deleteById(id);
    }
}
