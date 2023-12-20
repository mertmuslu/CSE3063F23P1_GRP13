import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String studentID;
    private String password;
    private double GPA;
    private int semester;
    List<Course> courses; 
    private Transcript transcript;


    public Student(String firstName, String lastName, String studentID, String password, double gPA, int semester) {
        super(firstName, lastName);
        this.studentID = studentID;
        this.password = password;
        this.GPA = gPA;
        this.semester = semester;
        this.courses = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getCourses() {
        return courses;
    }
    public Transcript getTranscript() {
        return transcript;
    }
    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public String createEmailAddress() {
        return getFirstName().toLowerCase() + getLastName().toLowerCase() + "@marun.edu.tr" ;
    }
}