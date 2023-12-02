package itmo.soa.mainservice.service;

import itmo.soa.library.Flat;
import itmo.soa.library.FlatCreateDTO;
import itmo.soa.library.FlatSearchResponse;
import itmo.soa.mainservice.exception.FlatNotFoundException;
import jakarta.ejb.Local;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Local
public interface FlatCrudService {
    Flat createFlat(FlatCreateDTO flatCreateDTO);
    List<Flat> getAllFlats(List<String> sort, List<String> filter, Integer page, Integer pageSize);
    Flat getFlatByID(Integer id) throws FlatNotFoundException;
    Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException;
    void deleteById(Integer id) throws FlatNotFoundException;
}
