package commonplayground.model;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findAllById(Long id);
    ArrayList<Session> findAllByIdOfHost(Long id);
    ArrayList<Session> findAllByUsers(User user);
}
