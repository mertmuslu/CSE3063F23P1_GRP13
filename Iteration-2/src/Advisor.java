package Projee;

import java.util.ArrayList;
import java.util.List;

public class Advisor extends Lecturer {
    private String password;
    private List<Student> students;

    public Advisor(String firstName, String lastName, String password, List<Course> assignedCourses, List<Student> students) {
        super(firstName, lastName, assignedCourses);
        this.password = password;
        this.students = new ArrayList<>();
        this.students.addAll(students);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public boolean hasStudent(Student student) {
        return students.contains(student);
    }
}