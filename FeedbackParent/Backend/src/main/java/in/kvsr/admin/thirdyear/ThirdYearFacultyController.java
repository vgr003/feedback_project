package in.kvsr.admin.thirdyear;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.kvsr.admin.faculty.FacultyService;
import in.kvsr.common.entity.Faculty;
import in.kvsr.common.entity.ThirdYearFaculty;
import in.kvsr.util.FacultyPdfExporter;

@Controller
public class ThirdYearFacultyController {
	
	@Autowired
	private ThirdYearFacultyService thirdYearFacultyService;
	
	@Autowired
	private FacultyService facultyService;
	
	
	private List<ThirdYearFaculty> thirdYearFacultyList=null;
	
	@GetMapping("/third-year-faculty")
	public String viewHodPage(Model model) {
		model.addAttribute("title", "third-year-faculty");
		model.addAttribute("thirdYearActive", "active");
		model.addAttribute("newFaculty", new ThirdYearFaculty());
		thirdYearFacultyList = thirdYearFacultyService.listAll();
		if(thirdYearFacultyList != null) {
			for(ThirdYearFaculty thirdYearFaculty: thirdYearFacultyList) {
				Faculty faculty = facultyService.getByRegId(thirdYearFaculty.getRegId());
				preProcess(thirdYearFaculty, faculty);
			}
		}
		
		model.addAttribute("facultyList", thirdYearFacultyList);
		return "thirdyearfaculty";
	}
	
	
	@PostMapping("/third-year-faculty/save")
	public String save(@ModelAttribute("newFaculty") ThirdYearFaculty thirdYearFaculty, RedirectAttributes redirectAttributes) {
		Faculty faculty = facultyService.getByRegId(thirdYearFaculty.getRegId().trim());
		if(faculty==null) {
			redirectAttributes.addFlashAttribute("message","Soryy! no faculty found with id, "+thirdYearFaculty.getRegId());
			return "redirect:/third-year-faculty";
		}
		preProcess(thirdYearFaculty, faculty);
		if(thirdYearFacultyService.save(thirdYearFaculty)) {
			redirectAttributes.addFlashAttribute("message","Added Third Year Faculty with id, "+thirdYearFaculty.getRegId()+"!");
			return "redirect:/third-year-faculty";
		}
		redirectAttributes.addFlashAttribute("message","field error, person might exists!");
		return "redirect:/third-year-faculty";
	}
	
	@GetMapping("/third-year-faculty/update/{regId}")
	public String updateByRegId(@PathVariable String regId, Model model) {
		ThirdYearFaculty thirdYearFaculty = thirdYearFacultyService.getFacultyByRegId(regId);
		model.addAttribute("year", "(3rd Year)");
		model.addAttribute("thirdYearFaculty", thirdYearFaculty);
		return "branchform3";
	}
	
	@PostMapping("/third-year-faculty/update")
	public String update(@ModelAttribute ThirdYearFaculty thirdYearFaculty, RedirectAttributes redirectAttributes) {
		Faculty faculty = facultyService.getByRegId(thirdYearFaculty.getRegId());
		
		preProcess(thirdYearFaculty, faculty);
		if(thirdYearFacultyService.save(thirdYearFaculty)) {
			redirectAttributes.addFlashAttribute("message","Person with id, "+thirdYearFaculty.getRegId()+" details updated!");
			return "redirect:/third-year-faculty";
		}else {
			redirectAttributes.addFlashAttribute("message","try again!");
			return "redirect:/third-year-faculty/update/"+thirdYearFaculty.getRegId();
		}
	}
	
	@GetMapping("/third-year-faculty/delete/{regId}")
	public String delete(@PathVariable String regId, RedirectAttributes redirectAttributes) {
		if(thirdYearFacultyService.deleteByRegId(regId.trim())) {
			redirectAttributes.addFlashAttribute("message","Third Year Faculty with id, "+regId+" deleted!");
			return "redirect:/third-year-faculty";
		}
		redirectAttributes.addFlashAttribute("message","try again!");
		return "redirect:/third-year-faculty";
	}
	
	@GetMapping("/third-year-faculty/refactor-id")
	public String truncate(RedirectAttributes redirectAttributes) {
		if(thirdYearFacultyService.refactorIdColumn()) {
			return "redirect:/third-year-faculty";
		}
		redirectAttributes.addFlashAttribute("message","try again!");
		return "redirect:/third-year-faculty";
	}
	
	@GetMapping("/third-year-faculty/export-to-pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		FacultyPdfExporter pdfExporter = new FacultyPdfExporter();
		List<Faculty> facultyList = new ArrayList<>();
		for(ThirdYearFaculty thirdYearFaculty: thirdYearFacultyService.listAll()) {
			facultyList.add(facultyService.getByRegId(thirdYearFaculty.getRegId()));
		}
		sortByBranch(facultyList);
		pdfExporter.export(response, facultyList);
	}
	
	private void sortByBranch(List<Faculty> faculties) {
		Collections.sort(faculties, new Comparator<Faculty>() {
			public int compare(Faculty f1, Faculty f2) {
				return f1.getDepartment().compareTo(f2.getDepartment());
			}
		});
	}
	
	public void preProcess(ThirdYearFaculty thirdYearFaculty, Faculty faculty) {
		
		thirdYearFaculty.setRegId(faculty.getRegId().trim());
		thirdYearFaculty.setFirstName(faculty.getFirstName());
		thirdYearFaculty.setAverage(faculty.getAverage());
		
		switch (faculty.getDepartment().toUpperCase()) {
		case "CSE": 
			thirdYearFaculty.setCse(true);
			break;
		case "H&S": 
			thirdYearFaculty.sethAndS(true);
			break;
		case "CIVIL": 
			thirdYearFaculty.setCivil(true);
			break;
		case "ECE": 
			thirdYearFaculty.setEce(true);
			break;
		case "EEE": 
			thirdYearFaculty.setEee(true);
			break;
		case "MECHANICAL": 
			thirdYearFaculty.setMechanical(true);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + faculty);
		}
	}

}
