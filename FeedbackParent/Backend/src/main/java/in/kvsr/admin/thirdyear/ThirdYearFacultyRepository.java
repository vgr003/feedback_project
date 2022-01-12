package in.kvsr.admin.thirdyear;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import in.kvsr.common.entity.ThirdYearFaculty;

@Repository
public interface ThirdYearFacultyRepository extends CrudRepository<ThirdYearFaculty, Long>{
	@Query("select f from ThirdYearFaculty f where regId = :regId")
	ThirdYearFaculty getByRegId(@Param("regId") String regId);
	
	@Transactional
	@Modifying
	@Query("delete from ThirdYearFaculty where regId = :regId")
	Integer deleteByRegId(@Param("regId") String regId);
	
	@Transactional
	@Modifying
	@Query(value="truncate table third_year_faculty", nativeQuery=true)
	Integer truncateAll();
	
	@Transactional
	@Modifying
	@Query(value="alter table third_year_faculty drop column id", nativeQuery=true)
	Integer dropIdColumn();
	
	
	@Transactional
	@Modifying
	@Query(value="alter table third_year_faculty add column id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY", nativeQuery=true)
	Integer addIdColumn();
}
