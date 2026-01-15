package duc.demo.repository.criteria;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String keyword; //firstName, lastName, id....
    private String operation; // :, < , >
    private Object value;


}
