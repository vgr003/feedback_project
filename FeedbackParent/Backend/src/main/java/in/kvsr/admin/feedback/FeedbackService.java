package in.kvsr.admin.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.kvsr.admin.faculty.FacultyService;
import in.kvsr.common.entity.Faculty;
import in.kvsr.common.entity.Feedback;

@Service
public class FeedbackService {
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private LockService lockService;
	@Autowired
	private FacultyService facultyService;
	
	public List<Feedback> listAll() {
		return (List<Feedback>) feedbackRepository.findAll();
	}
	
	public List<Feedback> sortByField(String field){
		return feedbackRepository.sortByField(Sort.by(field));
	}
	
	public boolean save(Feedback feedback) {
		if(lockService.isLocked()) {
			try {
				setAverageToFeedback(feedback);
				feedbackRepository.save(feedback);
				return true;
			}catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public boolean isAuthenticated(String regId) {
		return lockService.isAuthenticated(regId);
	}
	
	public List<Feedback> getFeedbacksByFacultyId(Long facultyId) {
		return feedbackRepository.getFeedbacksByFacultyId(facultyId);
	}
	
	public boolean truncateAll() {
		if(lockService.isLocked()) {
			return (feedbackRepository.truncate()==0);
		}
		return false;
	}
	
	public Faculty getLockedFaculty() {
		return facultyService.getByRegId(lockService.getLockedFacultyRegId());
	}
	private void setAverageToFeedback(Feedback feedback) {
		Float average = ((float)(feedback.getQuestion1()+
				feedback.getQuestion2()+
				feedback.getQuestion3()+
				feedback.getQuestion4()+
				feedback.getQuestion5()) / 5);
		//System.out.println("average: "+average);
		feedback.setAverage(average);
	}
}
