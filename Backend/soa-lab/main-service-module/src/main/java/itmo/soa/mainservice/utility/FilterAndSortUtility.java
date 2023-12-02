package itmo.soa.mainservice.utility;

import itmo.soa.library.Filter;
import itmo.soa.library.FilteringOperation;
import itmo.soa.library.Flat;
import itmo.soa.library.Sort;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class FilterAndSortUtility {

    private final static Pattern nestedFieldNamePattern = Pattern.compile("(.*)\\.(.*)");
    private final static Pattern lhsPattern = Pattern.compile("(.*)\\[(.*)]=(.*)");
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static int DEFAULT_PAGE = 1;


    public static void prepareDataForSortFilter(List<String> sorts, List<String> filters,
                                                Integer page, Integer pageSize, Class<?> clazz,
                                                List<Sort> sortList, List<Filter> filterList){
        if (page == null) page = DEFAULT_PAGE;
        if (pageSize == null) page = DEFAULT_PAGE_SIZE;

        if (!sorts.isEmpty()){
            sortList = FilterAndSortUtility.getSortsFromStringList(sorts);
        }
        if (!filters.isEmpty()){
            filterList = FilterAndSortUtility.getFiltersFromStringList(filters, clazz);
        }
    }

    private static List<Sort> getSortsFromStringList(List<String> sorts) {
        List<Sort> sortList = new ArrayList<>();
        boolean containsOppositeSorts = sorts.stream().allMatch(e1 -> sorts.stream().allMatch(e2 -> Objects.equals(e1, "-" + e2)));
        if (containsOppositeSorts) throw new IllegalArgumentException("Opposite sorts");
        for (String sort : sorts) {
            boolean desc = sort.startsWith("-");
            String sortFieldName = desc ? sort.substring(1) : sort;
            String nestedName = null;

            Matcher matcher = nestedFieldNamePattern.matcher(sortFieldName);

            if (matcher.find()) {
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
        return sortList;
    }

    private static List<Filter> getFiltersFromStringList(List<String> filters, Class<?> clazz) {
        List<Filter> filterList = new ArrayList<>();
        for (String filter : filters) {
            Matcher matcher = lhsPattern.matcher(filter);
            String fieldName = null, fieldValue = null;
            FilteringOperation filteringOperation = null;
            String nestedName = null;

            if (matcher.find()) {
                fieldName = matcher.group(1);
                Matcher nestedFieldMatcher = nestedFieldNamePattern.matcher(fieldName);
                if (nestedFieldMatcher.find()) {
                    String nestedField = nestedFieldMatcher.group(2).substring(0, 1).toLowerCase() + nestedFieldMatcher.group(2).substring(1);
                    fieldName = nestedFieldMatcher.group(1);
                    nestedName = nestedField;
                }

                filteringOperation = FilteringOperation.fromValue(matcher.group(2));

                if (clazz.equals(Flat.class)) {
                    checkIfEquationOperation(filteringOperation, fieldName);
                    if (Objects.equals(fieldName, "view") || Objects.equals(fieldName, "transport") || Objects.equals(fieldName, "furnish")) {
                        fieldValue = matcher.group(3).toLowerCase();
                    } else fieldValue = matcher.group(3);
                }
            }
            if (fieldName != null && fieldName.isEmpty())
                throw new IllegalArgumentException("Filter field name cannot be empty");
            if (fieldValue != null && fieldValue.isEmpty())
                throw new IllegalArgumentException("Filter field value cannot be empty");
            if (Objects.equals(filteringOperation, FilteringOperation.UNDEFINED))
                throw new IllegalArgumentException("Wrong filtering operation");

            filterList.add(Filter.builder()
                    .fieldName(fieldName)
                    .nestedName(nestedName)
                    .fieldValue(fieldValue)
                    .filteringOperation(filteringOperation)
                    .build());
        }
        return filterList;
    }

    private static void checkIfEquationOperation(FilteringOperation filteringOperation, String fieldName) {
        boolean equationOperation = Objects.equals(filteringOperation, FilteringOperation.EQ) || Objects.equals(filteringOperation, FilteringOperation.NEQ);
        if (Objects.equals(fieldName, "furnish")) {
            if (!equationOperation) {
                throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
            }
        }
        if (Objects.equals(fieldName, "view")) {
            if (!equationOperation) {
                throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
            }
        }
        if (Objects.equals(fieldName, "transport")) {
            if (!equationOperation) {
                throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
            }
        }
        if (Objects.equals(fieldName, "hasBalcony")) {
            if (!equationOperation) {
                throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
            }
        }
    }

}

