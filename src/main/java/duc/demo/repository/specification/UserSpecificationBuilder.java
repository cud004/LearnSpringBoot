package duc.demo.repository.specification;

import duc.demo.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static duc.demo.repository.specification.SearchOperation.ZERO_OR_MORE_REGEX;

public class UserSpecificationBuilder {

    public final List<SpecSearchCriteria> param;

    public UserSpecificationBuilder() {
        this.param = new ArrayList<>();
    }

    public UserSpecificationBuilder with(String key, String operation, Object value, String prefix, String suffix) {

        return with(null, key, operation, value, prefix, suffix);
    }

    public UserSpecificationBuilder with (String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper == SearchOperation.EQUALITY) {
            boolean startsWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
            boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

            if (startsWithAsterisk && endWithAsterisk) {
                oper = SearchOperation.CONTAINS; //*abc* -> Chứa
            } else if (startsWithAsterisk) {
                oper = SearchOperation.ENDS_WITH; // *abc -> Kết thúc bằng
            } else if (endWithAsterisk) {
                oper = SearchOperation.STARTS_WITH; // abc* -> Bắt đầu bằng
            }

        }

        param.add(new SpecSearchCriteria(orPredicate, key, oper, value));
        return this;
    }

    public Specification<User> build(){
        if(param.isEmpty()){
            return null;
        }
        Specification<User> specification = new UserSpecification(param.get(0));

        for (int i = 1; i < param.size(); i++) {
            UserSpecification nextSpec = new UserSpecification(param.get(i));

            // SỬA Ở ĐÂY: Gọi trực tiếp .or() hoặc .and() từ biến specification cũ
            specification = param.get(i).getOrPredicate()
                    ? specification.or(nextSpec)   // Nếu có cờ OR
                    : specification.and(nextSpec); // Mặc định là AND
        }
        return specification;
    }
}
