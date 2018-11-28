package commonplayground.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findAllByName(String name);
    User findAllById(Long id);
}