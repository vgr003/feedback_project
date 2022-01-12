package in.kvsr.admin.feedback;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kvsr.common.entity.Feedback;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long>{
	

	
	@Transactional
	@Modifying
	@Query(value="truncate table feedback", nativeQuery=true)
	Integer truncate();
	
	@Query("select f from Feedback f")
	List<Feedback> sortByField(Sort by);
	
	@Query("select f from Feedback f where facultyId = :id")
	List<Feedback> getFeedbacksByFacultyId(@Param("id") Long id);
}
