package in.kvsr.admin.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.kvsr.common.entity.Lock;

@Service
public class LockService {
	@Autowired
	private LockRepository lockRepository;
		
	public boolean isLocked() {
		return lockRepository.findById(1L).get().isEnabled();
	}
	
	public List<Lock> listAll(){
		return (List<Lock>) lockRepository.findAll();
	}
	public boolean aquireLock(String regId) {
		Lock lock = lockRepository.findById(1L).get();
		if(!lock.isEnabled()) {
			lock.setEnabled(true);
			lock.setRegId(regId);
			lockRepository.save(lock);
			return true;
		}
		return false;
	}
	
	
	public boolean releaseLock(String regId) {
		if(regId.trim().equalsIgnoreCase(getLockedFacultyRegId())) {
			Lock lock = lockRepository.findById(1L).get();
			lock.setEnabled(false);
			lock.setRegId("");
			lockRepository.save(lock);
			return true;
		}
		return false;
	}
	
	public boolean isAuthenticated(String regId) {
		return lockRepository.findById(1L).get().getRegId().equalsIgnoreCase(regId);
	}
	public String getLockedFacultyRegId() {
		return lockRepository.findById(1L).get().getRegId();
	}

	public void save(Lock lock) {
		lockRepository.save(lock);
	}
}
