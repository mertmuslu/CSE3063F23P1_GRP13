
import java.util.ArrayList;
import java.util.List;
public class Lecturer extends Person{

    private List<Course> assignedCourses;

    public Lecturer(String firstName, String lastName, List<Course> assignedCourses) {
        super(firstName, lastName);
        this.assignedCourses = new ArrayList<Course>();
    }

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public void setAssignedCourses(Course course) {
        this.assignedCourses.add(course);
    }
    public String createEmailAddress() {
        return getFirstName().toLowerCase() + getLastName().toLowerCase() + "@marmara.edu.tr" ;
    }
}