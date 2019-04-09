package commonplayground.model;

import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findAllById(Long id);
}
