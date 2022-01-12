	package in.kvsr.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "feedback",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"student_id", "faculty_id"})
	})
public class Feedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id=0L;
	@Column(name="student_id", nullable = false)
	private Long studentId=0L;
	@Column(name="faculty_id", nullable = false)
	private Long facultyId=0L;
	@Column(name="question_1", nullable = false)
	private Integer question1=0;
	@Column(name="question_2", nullable = false)
	private Integer question2=0;
	@Column(name="question_3", nullable = false)
	private Integer question3=0;
	@Column(name="question_4", nullable = false)
	private Integer question4=0;
	@Column(name="questin_5", nullable = false)
	private Integer question5=0;
	@Column(name="questin_6", nullable = false)
	private Integer question6=0;
	@Column(name="questin_7", nullable = false)
	private Integer question7=0;
	@Column(name="questin_8", nullable = false)
	private Integer question8=0;
	@Column(name="questin_9", nullable = false)
	private Integer question9=0;
	@Column(name="questin_10", nullable = false)
	private Integer question10=0;
	@Column(name="average", nullable = false)
	private Float average=0.0f;
	
	public Feedback() {
		
	}
	
	public Feedback(Long studentId, Long facultyId, Integer question1, Integer question2, Integer question3,
			Integer question4, Integer question5) {
		
		this.studentId = studentId;
		this.facultyId = facultyId;
		this.question1 = question1;
		this.question2 = question2;
		this.question3 = question3;
		this.question4 = question4;
		this.question5 = question5;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(Long facultyId) {
		this.facultyId = facultyId;
	}

	public Integer getQuestion1() {
		return question1;
	}
	
	public void setQuestion1(Integer question1) {
		this.question1 = question1;
	}

	public Integer getQuestion2() {
		return question2;
	}

	public void setQuestion2(Integer question2) {
		this.question2 = question2;
	}

	public Integer getQuestion3() {
		return question3;
	}

	public void setQuestion3(Integer question3) {
		this.question3 = question3;
	}

	public Integer getQuestion4() {
		return question4;
	}

	public void setQuestion4(Integer question4) {
		this.question4 = question4;
	}

	public Integer getQuestion5() {
		return question5;
	}

	public void setQuestion5(Integer question5) {
		this.question5 = question5;
	}
	
	public Integer getQuestion6() {
		return question6;
	}
	
	public void setQuestion6(Integer question6) {
		this.question6 = question6;
	}
	
	
	

	public Integer getQuestion7() {
		return question7;
	}

	public void setQuestion7(Integer question7) {
		this.question7 = question7;
	}

	public Integer getQuestion8() {
		return question8;
	}

	public void setQuestion8(Integer question8) {
		this.question8 = question8;
	}

	public Integer getQuestion9() {
		return question9;
	}

	public void setQuestion9(Integer question9) {
		this.question9 = question9;
	}

	public Integer getQuestion10() {
		return question10;
	}

	public void setQuestion10(Integer question10) {
		this.question10 = question10;
	}

	public Float getAverage() {
		return average;
	}

	public void setAverage(Float average) {
		this.average = average;
	}

	@Override
	public String toString() {
		return "Feedback [studentId=" + studentId + ", facultyId=" + facultyId + ", question1=" + question1
				+ ", question2=" + question2 + ", question3=" + question3 + ", question4=" + question4 + ", question5="
				+ question5 + ", question6=" + question6 + ", question7=" + question7 + ", question8=" + question8
				+ ", question9=" + question9 + ", question10=" + question10 + ", average=" + average + "]";
	}
	
}
