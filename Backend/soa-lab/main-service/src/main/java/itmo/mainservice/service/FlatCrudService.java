package itmo.mainservice.service;

import itmo.library.Flat;
import itmo.library.FlatCreateDTO;
import itmo.mainservice.exception.FlatNotFoundException;
import itmo.mainservice.exception.NotCreatedException;
import jakarta.ejb.Local;
import jakarta.transaction.Transactional;

import java.util.List;

@Local
public interface FlatCrudService {
    @Transactional
    Flat createFlat(FlatCreateDTO flatCreateDTO) throws NotCreatedException;
    List<Flat> getAllFlats(List<String> sort, List<String> filter, Integer page, Integer pageSize);
    Flat getFlatByID(Integer id) throws FlatNotFoundException;
    Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException;
    void deleteById(Integer id) throws FlatNotFoundException;
}
