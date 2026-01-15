package duc.demo.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteriaConsumer implements Consumer<SearchCriteria> {
    private CriteriaBuilder builder;
    private Predicate predicate;
    private Root root;



    @Override
    public void accept(SearchCriteria param) {
        if(param.getOperation().equals(">")){
            predicate =  builder.and(predicate, builder.greaterThanOrEqualTo(root.get(param.getKeyword()), param.getValue().toString()));
        }
        else if(param.getOperation().equals("<")){
            predicate =  builder.and(predicate, builder.lessThanOrEqualTo(root.get(param.getKeyword()), param.getValue().toString()));

        } else{// : => equal
            if(root.get(param.getKeyword()).getJavaType() ==  String.class){
                predicate =  builder.and(predicate, builder.like(root.get(param.getKeyword()), "%" + param.getValue().toString() + "%"));
            } else{
                predicate = builder.and(predicate, builder.equal(root.get(param.getKeyword()), param.getValue()));
            }

        }
    }
}
