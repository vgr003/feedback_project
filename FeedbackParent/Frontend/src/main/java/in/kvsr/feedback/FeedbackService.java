package in.kvsr.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.kvsr.common.entity.Feedback;

@Service
public class FeedbackService {
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private LockService lockService;
	
	
	public Integer save(Feedback feedback) {
		if(lockService.isLocked()) {
			try {
				
				feedbackRepository.save(feedback);
				return 1;
			}catch (Exception e) {
				return -1;
			}
		}
		return 0;
	}
	
	
}
