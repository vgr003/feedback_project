package in.kvsr.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockService {
	@Autowired
	private LockRepository lockRepository;
		
	public boolean isLocked() {
		return lockRepository.findById(1L).get().isEnabled();
	}
	
	public boolean isAuthenticated(String regId) {
		return lockRepository.findById(1L).get().getRegId().equalsIgnoreCase(regId);
	}
	public String getLockedFacultyRegId() {
		return lockRepository.findById(1L).get().getRegId();
	}

}
