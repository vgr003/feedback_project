package in.kvsr.admin.faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import in.kvsr.admin.feedback.FeedbackService;
import in.kvsr.admin.hod.HODService;
import in.kvsr.common.entity.Faculty;
import in.kvsr.common.entity.Feedback;
import in.kvsr.common.entity.HOD;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class FacultyService {
	
	@Autowired
	private FacultyRepository facultyRepository;
	@Autowired
	private HODService hodService;
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<Faculty> listAll() {
		return (List<Faculty>) facultyRepository.findAll();
	}
	
	public List<Faculty> listAllInSortOrder(String sortOrder){
		return facultyRepository.findAllBySortOrder(Sort.by(sortOrder));
	}
	
	public Faculty getById(Long id) {
		try {
			return facultyRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public boolean deleteByID(Long id) {
		try {
			Faculty faculty = facultyRepository.findById(id).get();
			Path directory = Paths.get("../frontend/faculty-images/"+faculty.getRegId());
			FileSystemUtils.deleteRecursively(directory);
			facultyRepository.delete(faculty);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Faculty getByRegId(String name) {
		
		return facultyRepository.getByRegId(name);
	}
	
	public List<Faculty> getFacultyWithAverages() {
		for(Faculty faculty: listAll()) {
			setAverageToFaculty(faculty);
		}
		return listAll();
	}
	
	public void setAverageToFaculty(Faculty faculty) {
		List<Feedback> feedbacks = feedbackService.getFeedbacksByFacultyId(faculty.getId());
		if(feedbacks==null || feedbacks.isEmpty()) {
			return;
		}
		Float average = 0F;
		Map<Integer, Integer> hashMap = new LinkedHashMap<>(6);
		for(int i=0;i<10;i++) {
			hashMap.put(i, 0);
		}
		for(Feedback feedback : feedbacks) {
			setQuestionCounter(feedback, hashMap);
			average += feedback.getAverage();
		}
		average = average / feedbacks.size();
		faculty.setAverage(average);
		setMinAndMax(faculty, hashMap);
		hashMap.clear();
		saveWithoutEncryption(faculty);
		return;
	}
	private void setMinAndMax(Faculty faculty, Map<Integer, Integer> hashMap) {
		Integer max = 0, maxIndex=-1;
		Integer min = Integer.MAX_VALUE, minIndex=-1;
		
		for(int i=0;i<hashMap.size();i++) {
			Integer value = hashMap.get(i);
			if(value>=max) {
				max = value;
				maxIndex=i;
			}
			if(value <= min) {
				min = value;
				minIndex = i;
			}
		}
		if(minIndex == maxIndex) {
			faculty.setGood("_");
			faculty.setRequired("_");
			return;
		}
		faculty.setGood(String.valueOf(maxIndex+1));
		faculty.setRequired(String.valueOf(minIndex+1));
	}
	private void setQuestionCounter(Feedback feedback,Map<Integer, Integer> hashMap) {
		List<Integer> feedbackList = new ArrayList<>();
		feedbackList.add(feedback.getQuestion1());
		feedbackList.add(feedback.getQuestion2());
		feedbackList.add(feedback.getQuestion3());
		feedbackList.add(feedback.getQuestion4());
		feedbackList.add(feedback.getQuestion5());
		feedbackList.add(feedback.getQuestion6());
		feedbackList.add(feedback.getQuestion7());
		feedbackList.add(feedback.getQuestion8());
		feedbackList.add(feedback.getQuestion9());
		feedbackList.add(feedback.getQuestion10());
		for(int i=0;i<feedbackList.size();i++) {
			hashMap.put(i, hashMap.get(i)+feedbackList.get(i));
		}
	}
	public boolean save(Faculty faculty) {
		try {
			Faculty tempFaculty = facultyRepository.getByRegId(faculty.getRegId());
			faculty.setAverage(tempFaculty.getAverage());
			faculty.setGood(tempFaculty.getGood());
			faculty.setRequired(tempFaculty.getRequired());
			if(faculty.getId() != null && (faculty.getPassword()==null || faculty.getPassword().isEmpty())) {
				faculty.setPassword(facultyRepository.getByRegId(faculty.getRegId()).getPassword());
			}else {
				encodePassword(faculty);
			}
			if(faculty.getRegId()!=null) {
				HOD hod = hodService.findByRegId(faculty.getRegId());
				
				if(hod!=null) {
					hod.setPassword(faculty.getPassword());
					hodService.save(hod);
				}
			}
			facultyRepository.save(faculty);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean saveWithoutEncryption(Faculty faculty) {
		try {
			facultyRepository.save(faculty);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public List<Faculty> searchByRegIdAndName(String key) {
		return facultyRepository.searchByRegIdAndName(key);
	}
	
	public boolean refactorIdColumn() {
		Integer dv =facultyRepository.dropIdColumn();
		Integer av = facultyRepository.addIdColumn();
		return !(av==dv);
	}
	 
	private void encodePassword(Faculty faculty) {
		String encoded = passwordEncoder.encode(faculty.getPassword());
		faculty.setPassword(encoded);
	}

}
