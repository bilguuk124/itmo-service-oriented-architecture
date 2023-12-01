package itmo.soa.mainservice.service.impl;

import itmo.soa.library.*;
import itmo.soa.mainservice.exception.FlatNotFoundException;
import itmo.soa.mainservice.repository.FlatCrudRepository;
import itmo.soa.mainservice.service.FlatCrudService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class FlatCrudServiceImpl implements FlatCrudService {

    private final static int DEFAULT_PAGE_LIMIT = 10;
    @Inject
    private FlatCrudRepository repository;

    @Override
    public Flat createFlat(FlatCreateDTO flatCreateDTO) {
        Flat flat = new Flat();
        LocalDate date = LocalDate.now();
        flat.setCreationDate(date);
        flat.update(flatCreateDTO);
        repository.save(flat);
        return flat;
    }

    @Override
    public List<Flat> getAllFlats(List<String> sorts, List<String> filters, Integer page, Integer pageSize) {
        if (page == null) page = 1;
        if (pageSize == null) page = DEFAULT_PAGE_LIMIT;

        Pattern nestedFieldNamePattern = Pattern.compile("(.*)\\.(.*)");
        Pattern lhsPattern = Pattern.compile("(.*)\\[(.*)]=(.*)");

        List<Sort> sortList = new ArrayList<>();

        if (!sorts.isEmpty()){
            boolean containsOppositeSorts = sorts.stream().allMatch(e1 -> sorts.stream().allMatch(e2 -> Objects.equals(e1, "-" + e2)));
            if (containsOppositeSorts) throw new IllegalArgumentException("Opposite sorts");
            for (String sort : sorts){
                boolean desc = sort.startsWith("-");
                String sortFieldName = desc ? sort.substring(1) : sort;
                String nestedName = null;

                Matcher matcher = nestedFieldNamePattern.matcher(sortFieldName);

                if (matcher.find()){
                    String nestedField = matcher.group(2).substring(0, 1).toLowerCase() + matcher.group(2).substring(1);
                    sortFieldName = matcher.group(1);
                    nestedName = nestedField;
                }

                sortList.add(Sort.builder()
                        .desc(desc)
                        .fieldName(sortFieldName)
                        .nestedName(nestedName)
                        .build()
                );
            }
        }

        List<Filter> filterList = new ArrayList<>();

        for (String filter : filters){
            Matcher matcher = lhsPattern.matcher(filter);
            String fieldName = null, fieldValue = null;
            FilteringOperation filteringOperation = null;
            String nestedName = null;

            if (matcher.find()){
                fieldName = matcher.group(1);
                Matcher nestedFieldMatcher = nestedFieldNamePattern.matcher(fieldName);
                if (nestedFieldMatcher.find()){
                    String nestedField = nestedFieldMatcher.group(2).substring(0,1).toLowerCase() + nestedFieldMatcher.group(2).substring(1);
                    fieldName = nestedFieldMatcher.group(1);
                    nestedName = nestedField;
                }

                filteringOperation = FilteringOperation.fromValue(matcher.group(2));

                boolean equationOperation = Objects.equals(filteringOperation, FilteringOperation.EQ) && Objects.equals(filteringOperation, FilteringOperation.NEQ);
                if (Objects.equals(fieldName, "furnish")){
                    if(!equationOperation){
                        throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
                    }
                }
                if (Objects.equals(fieldName, "view")){
                    if(!equationOperation){
                        throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
                    }
                }
                if (Objects.equals(fieldName, "transport")){
                    if(!equationOperation){
                        throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
                    }
                }
                if (Objects.equals(fieldName, "hasBalcony")){
                    if(!equationOperation){
                        throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
                    }
                }

                if (Objects.equals(fieldName, "view") || Objects.equals(fieldName, "transport") || Objects.equals(fieldName, "furnish")){
                    fieldValue = matcher.group(3).toLowerCase();
                } else fieldValue = matcher.group(3);
            }


            if (fieldName.isEmpty()) throw new IllegalArgumentException("Filter field name cannot be empty");
            if (fieldValue.isEmpty()) throw new IllegalArgumentException("Filter field value cannot be empty");
            if (Objects.equals(filteringOperation, FilteringOperation.UNDEFINED)) throw new IllegalArgumentException("Wrong filtering operation");

            filterList.add(Filter.builder()
                    .fieldName(fieldName)
                    .nestedName(nestedName)
                    .fieldValue(fieldValue)
                    .filteringOperation(filteringOperation)
                    .build());
        }
        return repository.getAllPageable(sortList, filterList, page, pageSize);
    }


    @Override
    public Flat getFlatByID(Integer id) throws FlatNotFoundException {
        Optional<Flat> result = repository.getById(id);
        if (result.isEmpty()) throw new FlatNotFoundException();
        return result.get();
    }

    @Override
    public Flat updateFlatById(Integer id, FlatCreateDTO flatCreateDTO) throws FlatNotFoundException{
        Optional<Flat> result = repository.updateById(id, flatCreateDTO);
        if (result.isEmpty()) throw new FlatNotFoundException();
        return result.get();
    }

    @Override
    public void deleteById(Integer id) throws FlatNotFoundException {
        if (!repository.deleteById(id)) throw new FlatNotFoundException();
    }
}
