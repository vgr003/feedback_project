package in.kvsr.student;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kvsr.common.entity.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
	
	@Query("select s from Student s where rollNumber = :rollNumber")
	Student findByRollNumber(@Param("rollNumber") String rollNumber);
}
