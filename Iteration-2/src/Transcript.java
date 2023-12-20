
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Transcript {
    private String studentID;
    private double GPA;
    private List<String> completedCourses;
    private List<String> failedCourses;
    private int completedCredits;
    private int creditsTaken;

    public Transcript(String studentID) {
        this.studentID = studentID;
        this.GPA = 0.0;
        this.completedCourses = new ArrayList<>();
        this.failedCourses = new ArrayList<>();
        this.completedCredits = 0;
        this.creditsTaken = 0;
    }

	public String getStudentID() {
		return studentID;
	}
	
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	
	public double getGPA() {
		return GPA;
	}
	
	public void setGPA(double gPA) {
		GPA = gPA;
	}
	
	public List<String> getCompletedCourses() {
		return completedCourses;
	}

	public void setCompletedCourses(List<String> list) {
		this.completedCourses = list;
	}

	public List<String> getFailedCourses() {
		return failedCourses;
	}

	public void setFailedCourses(List<String> list) {
		this.failedCourses = list;
	}
	
	public int getCompletedCredits() {
		return completedCredits;
	}
	
	public void setCompletedCredits(int completedCredits) {
		this.completedCredits = completedCredits;
	}

	public int getCreditsTaken() {
		return creditsTaken;
	}
	
	public void setCreditsTaken(int creditsTaken) {
		this.creditsTaken = creditsTaken;
	}

	public void addCompletedCourse(Course course) {
        completedCourses.addAll((Collection<? extends String>) course);
        completedCredits += course.getCredit();
        creditsTaken += course.getCredit();
    }

    public void addFailedCourse(Course course) {
        failedCourses.addAll((Collection<? extends String>) course);
        creditsTaken += course.getCredit();  
    }

}