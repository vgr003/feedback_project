package in.kvsr.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.kvsr.common.entity.Faculty;
import in.kvsr.common.entity.Feedback;
import in.kvsr.common.entity.Student;
import in.kvsr.questions.QuestionService;

@Controller
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/student")
	public String homePage(Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
		if(!isAuthenticated()) {
			return "redirect:/login";
		}
		model.addAttribute("title","student");
		List<Faculty> facultyList = studentService.getFaculty(authentication.getName());
		if(facultyList==null) {
			redirectAttributes.addFlashAttribute("message","No faculty found in year!");
			return "redirect:/student/profile";
		}
		model.addAttribute("facultyList", facultyList);
		return "studenthomepage";
	}
	
	@GetMapping("/student/profile")
	public String profile(Model model, Authentication authentication) {
		if(!isAuthenticated()) {
			return "redirect:/login";
		}
		model.addAttribute("title","profile");
		model.addAttribute("profileActive","active");
		Student student = studentService.getByRollNumber(authentication.getName());
		model.addAttribute("student",student);
		return "studentprofile";
	}
	
	@PostMapping("/save-student")
	public String saveStudent(@ModelAttribute Student student, Model model, RedirectAttributes redirectAttributes) {
		if(!validateStudent(student)) {
			model.addAttribute("student", student);
			model.addAttribute("message", "Form validation failed!");
			return "studentregistration";
		}
		if(student.getId() > 0L && (student.getPassword().trim()==null || student.getPassword().isBlank())) {
			if(studentService.save(student)){
				redirectAttributes.addFlashAttribute("message", "hey, details updated!");
				return "redirect:/student";
			}else {
				model.addAttribute("message", "try again!");
				model.addAttribute("student", student);
				return "studentprofile";
			}
		}
		boolean isNew = (student.getId() > 0L) ? false : true;
		if(studentService.save(student)) {
			if(isNew) 
				redirectAttributes.addFlashAttribute("message","Account created, Please login!");
			else 
				redirectAttributes.addFlashAttribute("message","Account details updated, please login!");
			return "redirect:/login";
		}else {
			model.addAttribute("student", student);
			model.addAttribute("message", "Field error, person might exits!");
			return "studentregistration";
		}
	}
	
	@GetMapping("/questions/{facultyId}")
	public String questions(@PathVariable Long facultyId, Model model, Authentication authentication) {
		Feedback feedback = new Feedback();
		Student student = studentService.getByRollNumber(authentication.getName());
		feedback.setStudentId(student.getId());
		feedback.setFacultyId(facultyId);
	    model.addAttribute("questionList", questionService.listAll());
		model.addAttribute("feedback",feedback);
		return "questions";
	}
	
	
	private boolean validateStudent(Student student) {
		student.setFirstName(student.getFirstName().trim());
		if(student.getFirstName()==null || student.getFirstName().isBlank()) {
			return false;
		}
		student.setLastName(student.getLastName().trim());
		if(student.getLastName()==null || student.getLastName().isBlank()) {
	    	   return false;
			}
		student.setRollNumber(student.getRollNumber().trim().toUpperCase());
       if(student.getRollNumber()==null || student.getRollNumber().isBlank()) {
    	   return false;
		}
	   student.setPassword(student.getPassword().trim());
       if((student.getId() <= 0L) && (student.getPassword()==null || student.getPassword().isBlank())) {
    	   return false;
       }
       return true;
	}
	private  boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return true;
	}
}
