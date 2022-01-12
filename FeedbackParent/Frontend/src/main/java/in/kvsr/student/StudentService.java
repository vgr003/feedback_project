package in.kvsr.student;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.kvsr.common.entity.Faculty;
import in.kvsr.common.entity.Student;
import in.kvsr.faculty.FacultyService;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private FacultyService facultyService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


	public Student getByRollNumber(String rollNumber) { 
		
		return studentRepository.findByRollNumber(rollNumber); }

	public List<Faculty> getFaculty(String regId){ 
		Student student = getByRollNumber(regId);
		if(student==null) {
			return null;
		}
		return facultyService.getFacultyByYearAndBranch(student.getYear(), student.getBranch()); 
	 }

	public Student getById(Long id) {
		try {
			return studentRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public boolean save(Student student) {
		try {
			if(student.getId() > 0L && (student.getPassword().trim()==null || student.getPassword().trim().isEmpty())) {
				student.setPassword(studentRepository.findByRollNumber(student.getRollNumber()).getPassword());
			}else {
				encodePassword(student);
			}
			studentRepository.save(student);
			return true;
		}catch (Exception e) {
			
            return false;		
        }
	}
	
	private void encodePassword(Student student) {
		String encoded = passwordEncoder.encode(student.getPassword());
		student.setPassword(encoded);
	}


}
