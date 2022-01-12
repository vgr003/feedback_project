package in.kvsr.admin.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.kvsr.common.entity.Feedback;
import in.kvsr.common.entity.Lock;

@Controller
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private LockService lockService;


	@GetMapping("/feedback") 
	public String feedbackPage(Model model) {
		model.addAttribute("title","feedback");
		model.addAttribute("feedbackActive","active"); 
		if(lockService.listAll()==null || lockService.listAll().isEmpty()) {
			lockService.save(new Lock("0000"));
		}
		if(lockService.isLocked()) {
			model.addAttribute("lock","locked"); 
		}else {
			model.addAttribute("lock","lock"); 
		}
		List<Feedback> feedbackList = feedbackService.listAll();
		if(feedbackList != null) { 
			model.addAttribute("feedbackList", feedbackList);
		} 
		return "feedback";
	}


	@GetMapping("/feedback/lock")
	public String lock(Model model, Authentication authentication) {
		model.addAttribute("title","feedback");
		model.addAttribute("feedbackActive","active");
		//System.out.println("auth: "+authentication.getName());
		if(lockService.isLocked() && lockService.isAuthenticated(authentication.getName())) {
			if(lockService.releaseLock(authentication.getName())) {
				model.addAttribute("message","lock released!");
				model.addAttribute("lock","unlocked");
			}
		}else if(lockService.isLocked() && !lockService.isAuthenticated(authentication.getName())){
			model.addAttribute("message","Sorry! it's locked by "+feedbackService.getLockedFaculty().getFirstName()+" "+feedbackService.getLockedFaculty().getLastName()+".");
			model.addAttribute("lock", "locked");
		}else if(!lockService.isLocked()) {
			if(lockService.aquireLock(authentication.getName())) {
				model.addAttribute("lock", "locked");
				model.addAttribute("message","locked!");
			}
		}
		List<Feedback> feedbackList = feedbackService.listAll();
		if(feedbackList != null) {
			model.addAttribute("feedbackList", feedbackList);
		}
		return "feedback";
	}
	
	@GetMapping("/feedback/clear")
	public String truncateFeedback(RedirectAttributes redirectAttributes,Authentication authentication) {
		if(lockService.isLocked() && lockService.isAuthenticated(authentication.getName())) {
			if(feedbackService.truncateAll()) {
				redirectAttributes.addFlashAttribute("message","cleared feedback!");
				return "redirect:/feedback";
			}
		}
		redirectAttributes.addFlashAttribute("message","Aquire lock and try again!");
		return "redirect:/feedback";
	}
	
	@GetMapping("/feedback/{field}") 
	public String feedbackPage(Model model, @PathVariable String field) {
		model.addAttribute("title","feedback");
		model.addAttribute("feedbackActive","active"); 
		if(lockService.isLocked()) {
			model.addAttribute("lock","locked"); 
		}else {
			model.addAttribute("lock","unlocked"); 
		}
		List<Feedback> feedbackList = feedbackService.sortByField(field);
		if(feedbackList != null) { 
			model.addAttribute("feedbackList", feedbackList);
		} 
		return "feedback";
	}

}
