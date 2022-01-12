package in.kvsr.admin.feedback;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import in.kvsr.common.entity.Lock;

@Repository
public interface LockRepository extends CrudRepository<Lock, Long> {

}
