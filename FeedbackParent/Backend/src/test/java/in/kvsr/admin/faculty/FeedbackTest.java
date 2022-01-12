package in.kvsr.admin.faculty;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import in.kvsr.admin.feedback.FeedbackRepository;
import in.kvsr.common.entity.Feedback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class FeedbackTest {
	
	@Autowired
	public FeedbackRepository feedbackRepository;
	
	@Test
	public void testToInsertIntoFeedback() {
		List<Feedback> feedbacks = new ArrayList<>();
		Feedback feedback = new Feedback(1L,1L,3,4,3,4,5);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(2L,1L,1,4,3,4,3);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(3L,1L,4,3,4,2,2);			
		settAverage(feedback);
		feedbacks.add(feedback);
		
		feedback = new Feedback(1L,7L,1,3,3,4,3);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(2L,7L,4,4,5,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(3L,7L,4,4,5,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		
		feedback = new Feedback(1L,3L,1,1,3,4,3);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(2L,3L,4,1,5,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(3L,3L,4,3,5,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		
		feedback = new Feedback(1L,5L,1,3,4,4,3);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(2L,5L,4,2,5,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(3L,5L,4,4,3,3,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		
		feedback = new Feedback(1L,13L,1,1,3,4,3);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(2L,13L,4,4,3,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(3L,13L,4,4,2,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		
		feedback = new Feedback(1L,15L,4,1,3,2,3);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(2L,15L,4,3,3,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		feedback = new Feedback(3L,15L,3,4,1,5,1);
		settAverage(feedback);
		feedbacks.add(feedback);
		
		feedbackRepository.saveAll(feedbacks);
	}
	
	private Float settAverage(Feedback feedback) {
		Float average = ((float)(feedback.getQuestion1()+
				feedback.getQuestion2()+
				feedback.getQuestion3()+
				feedback.getQuestion4()+
				feedback.getQuestion5()) / 5);
		//System.out.println("average: "+average);
		feedback.setAverage(average);
		return average;
	}
}
