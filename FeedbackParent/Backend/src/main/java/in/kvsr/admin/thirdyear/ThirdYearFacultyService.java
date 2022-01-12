package in.kvsr.admin.thirdyear;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.kvsr.common.entity.ThirdYearFaculty;

@Service
public class ThirdYearFacultyService {
	
	@Autowired
	private ThirdYearFacultyRepository thirdYearFacultyRepository;
	
	public boolean save(ThirdYearFaculty thirdYearFaculty) {
		try {
			thirdYearFacultyRepository.save(thirdYearFaculty);
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}
	
	public List<ThirdYearFaculty> listAll() {
		List<ThirdYearFaculty> facultyList = (List<ThirdYearFaculty>) thirdYearFacultyRepository.findAll();
		
		if(facultyList == null) {
			return null;
		}
		return facultyList;
	}
	
	public ThirdYearFaculty getFacultyByRegId(String regId) {
		return thirdYearFacultyRepository.getByRegId(regId);
	}
	
	public boolean deleteById(Long id) {
		try {
			thirdYearFacultyRepository.deleteById(id);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public boolean deleteByRegId(String regId) {
		if(thirdYearFacultyRepository.deleteByRegId(regId)==1) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean refactorIdColumn() {
		Integer dv = thirdYearFacultyRepository.dropIdColumn();
		Integer av = thirdYearFacultyRepository.addIdColumn();
		if(av==0 && dv==0)
			return true;
		return !(av==dv);
	}
	
	public boolean truncateAll() {
		return (thirdYearFacultyRepository.truncateAll()==0);
	}

}
