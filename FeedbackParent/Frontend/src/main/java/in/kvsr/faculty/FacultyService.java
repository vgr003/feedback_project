package in.kvsr.faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.kvsr.common.entity.Faculty;
import in.kvsr.common.entity.FirstYearFaculty;
import in.kvsr.common.entity.FourthYearFaculty;
import in.kvsr.common.entity.SecondYearFaculty;
import in.kvsr.common.entity.ThirdYearFaculty;

@Service
public class FacultyService {
	
	@Autowired
	private FirstYearFacultyRepository firstYearFacultyRepository;
	
	@Autowired
	private SecondYearFacultyRepository secondYearFacultyRepository;
	
	@Autowired
	private ThirdYearFacultyRepository thirdYearFacultyRepository;
	
	@Autowired
	private FourthYearFacultyRepository fourthYearFacultyRepository;
	
	@Autowired
	private FacultyRepository facultyRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Faculty getByRegId(String regId) {
		return facultyRepository.findByRegId(regId);
	}
	
	public Faculty getById(Long id) {
		try {
			return facultyRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public boolean save(Faculty faculty) {
		try {
			Faculty tempFaculty=null;
			if(faculty.getId() > 0L) {
				tempFaculty = facultyRepository.findByRegId(faculty.getRegId());
				faculty.setAverage(tempFaculty.getAverage());
				faculty.setGood(tempFaculty.getGood());
				faculty.setRequired(tempFaculty.getRequired());
				if(faculty.getPhoto()==null && tempFaculty.getPhoto()!=null) {
					faculty.setPhoto(tempFaculty.getPhoto());
				}
			}
			if((faculty.getId() > 0L) && (faculty.getPassword()==null || faculty.getPassword().isEmpty())) {
				faculty.setPassword(tempFaculty.getPassword());
			}else {
				encodePassword(faculty);
			}
			facultyRepository.save(faculty);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public List<Faculty> getFacultyByYearAndBranch(Integer year, String branch){
		List<Faculty> facultyList = new ArrayList<>();
		facultyList.clear();
		switch (year) {
		
		case 1: 
			List<FirstYearFaculty> firstYearFaculties = (List<FirstYearFaculty>) firstYearFacultyRepository.findAll();
			for(FirstYearFaculty firstYearFaculty: firstYearFaculties) {
				if(firstYearFaculty.contains(branch)) {
					Faculty faculty = facultyRepository.findByRegId(firstYearFaculty.getRegId());
					facultyList.add(faculty);
				}
			}
			return facultyList;
			
		case 2: 
			List<SecondYearFaculty> secondYearFaculties = (List<SecondYearFaculty>) secondYearFacultyRepository.findAll();
			for(SecondYearFaculty secondYearFaculty: secondYearFaculties) {
				if(secondYearFaculty.contains(branch)) {
					Faculty faculty = facultyRepository.findByRegId(secondYearFaculty.getRegId());
					facultyList.add(faculty);
				}
			}
			return facultyList;
		case 3: 
			List<ThirdYearFaculty> thirdYearFaculties = (List<ThirdYearFaculty>) thirdYearFacultyRepository.findAll();
			for(ThirdYearFaculty thirdYearFaculty: thirdYearFaculties) {
				if(thirdYearFaculty.contains(branch)) {
					Faculty faculty = facultyRepository.findByRegId(thirdYearFaculty.getRegId());
					facultyList.add(faculty);
				}
			}
			return facultyList;
		case 4: 
			List<FourthYearFaculty> fourthYearFaculties = (List<FourthYearFaculty>) fourthYearFacultyRepository.findAll();
			for(FourthYearFaculty fourthYearFaculty: fourthYearFaculties) {
				
				if(fourthYearFaculty.contains(branch)) {
					Faculty faculty = facultyRepository.findByRegId(fourthYearFaculty.getRegId());
					facultyList.add(faculty);
				}
			}
			return facultyList;
		default:
			throw new IllegalArgumentException("Unexpected value: " + year);
		}
	}
	
	private void encodePassword(Faculty faculty) {
		String encoded = passwordEncoder.encode(faculty.getPassword());
		faculty.setPassword(encoded);
	}
}
