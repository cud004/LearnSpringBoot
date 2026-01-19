package duc.demo.repository.specification;


import lombok.Getter;

import static duc.demo.repository.specification.SearchOperation.OR_PREDICATE_FLAG;
import static duc.demo.repository.specification.SearchOperation.ZERO_OR_MORE_REGEX;

@Getter
public class SpecSearchCriteria {

    private String key; //firstName, lastName, id....
    private SearchOperation operation; // :, < , >, !, ~
    private Object value;
    private Boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
    public SpecSearchCriteria(String orPredicate ,String key, SearchOperation operation, Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String key, String operation, String value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if(oper == SearchOperation.EQUALITY){
            boolean startsWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
            boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

            if(startsWithAsterisk && endWithAsterisk){
                oper = SearchOperation.CONTAINS; //*abc* -> Chứa
            } else if(startsWithAsterisk){
                oper = SearchOperation.ENDS_WITH; // *abc -> Kết thúc bằng
            }else if(endWithAsterisk){
                oper = SearchOperation.STARTS_WITH; // abc* -> Bắt đầu bằng
            }

        }
        this.key = key;
        this.operation = oper;
        this.value = value;
    }
}
