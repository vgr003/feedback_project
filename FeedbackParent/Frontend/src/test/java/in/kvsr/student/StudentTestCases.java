package in.kvsr.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import in.kvsr.common.entity.Student;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StudentTestCases {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Test
	public void testToGetStudentByRollNo() {
		String rollNumber = "18fh1a0539";
		Student student = studentRepository.findByRollNumber(rollNumber);
		System.out.println(student);
	}

}
