package duc.demo.repository.specification;

import duc.demo.model.User;
import duc.demo.util.Gender;

import org.springframework.data.jpa.domain.Specification;

public class UserSpec {

    public static Specification<User> hasFirstName(String firstName) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), "%"+ firstName + "%");
    }
    public static Specification<User> equalGender(Gender gender) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gender"), gender);
    }

}
