package itmo.mainservice.utility;

import itmo.library.Filter;
import itmo.library.FilteringOperation;
import itmo.library.Flat;
import itmo.library.Sort;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class FilterAndSortUtility {

    public final static int DEFAULT_PAGE_SIZE = 10;
    public final static int DEFAULT_PAGE = 1;
    private final static Pattern nestedFieldNamePattern = Pattern.compile("(.*)\\.(.*)");
    private final static Pattern lhsPattern = Pattern.compile("(.*)\\[(.*)]=(.*)");

    public static List<Sort> getSortsFromStringList(List<String> sorts) {
        List<Sort> sortList = new ArrayList<>();
        if (sorts.isEmpty()) return sortList;

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


    public static List<Filter> getFiltersFromStringList(List<String> filters, Class<?> clazz) {
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
                } else {
                    fieldValue = matcher.group(3);
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
        if (Objects.equals(fieldName, "hasBalcony")) {
            if (!equationOperation) {
                throw new IllegalArgumentException("Balcony can be only equals or not equals to!");
            }
        }
    }


}

