package oop_proje;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String studentID;
    private String password;
    private double GPA;
    protected int year;
    private List<Course> courses; // List to store courses for each student

    public Student(String firstName, String lastName, String studentID, String password, double gPA, int year) {
        super(firstName, lastName);
        this.studentID = studentID;
        this.password = password;
        this.GPA = gPA;
        this.year = year;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Method to add a course to the student's list
    public void addCourse(Course course) {
        courses.add(course);
    }

    // Method to get the list of courses for the student
    public List<Course> getCourses() {
        return courses;
    }
}
