package duc.demo.repository;

import duc.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//DAL
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
}
