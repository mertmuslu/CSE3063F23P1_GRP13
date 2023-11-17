import java.util.ArrayList;
import java.util.List;
public class Advisor extends Lecturer {
	private ArrayList<Student> students;

	public Advisor(String firstName, String lastName, String email, List<Course> assignedCourses,
			ArrayList<Student> students) {
		super(firstName, lastName, assignedCourses);
		this.students = new ArrayList<Student>();
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(Student student) {
		this.students.add(student);
	}
}
