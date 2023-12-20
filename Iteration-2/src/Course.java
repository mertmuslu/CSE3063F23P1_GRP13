
import java.util.ArrayList;

public class Course {
    private String courseID;
    private String courseName;
    private int credit;
    private ArrayList<Student> students;
    private String prerequisite;
    private int coursesemester;

    public Course(String courseID, String courseName, int credit, Lecturer courseLecturer,
                  ArrayList<Student> students, String prerequisite, int coursesemester) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credit = credit;
        this.students = new ArrayList<Student>();
        this.prerequisite = prerequisite;
        this.coursesemester = coursesemester;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(Student student) {
        this.students.add(student);
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public int getCoursesemester() {
        return coursesemester;
    }
}