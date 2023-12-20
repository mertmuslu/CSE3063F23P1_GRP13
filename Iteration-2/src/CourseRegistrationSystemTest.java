
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CourseRegistrationSystemTest {

    private CourseRegistrationSystem registrationSystem;

    @Before
    public void setUp() {
        registrationSystem = new CourseRegistrationSystem();
        
    }

    @Test
    public void testDisplayEnrolledStudentsForAdvisor() {
        Student student = new Student("Ahmet", "Kaya", "150120027", "ahmet027", 3.5, 5);
        Advisor advisor = new Advisor("Serap", "Korkmaz", "marmara", new ArrayList<>(), new ArrayList<>());
        Course course = new Course("C101", "Introduction to Programming", 3, null, null, "", 5);
        student.addCourse(course);
        advisor.setStudents(new ArrayList<>());     
        advisor.getStudents().add(student);
        System.out.println(advisor.hasStudent(student));
        
        registrationSystem.displayEnrolledStudentsForAdvisor(advisor);
     
    }
    @Test
    public void testSelectCourses() {
        // Test için gerekli öğrenci, danışman ve kurs bilgilerini yükleyebilirsiniz.
        Student student = new Student("Ahmet", "Kaya", "150120027", "ahmet027", 3.5, 5);
        Course course5 = new Course("C101", "Introduction to Programming", 3, null, null, "", 5);
        Course course2 = new Course("C102", "Introduction to Programming2", 3, null, null, "", 5);
        Course course3 = new Course("C103", "Introduction to Programming3", 3, null, null, "", 5);

        registrationSystem.getCourses().add(course5);
        registrationSystem.getCourses().add(course2);
        registrationSystem.getCourses().add(course3);

        student.getCourses().add(course5); // Öğrenci zaten bir dersi almış olsun

        Transcript transcript = new Transcript("150120027");

        registrationSystem.selectCourses(student, transcript);

        // Öğrencinin seçtiği ders sayısı kontrol ediliyor
        

        // İlgili dönemdeki dersler kontrol ediliyor
        assertFalse(registrationSystem.getAvailableCourses(student).isEmpty());
    }
    @Test
    public void testGetAvailableCourses() {
        
        Student student = new Student("Ahmet", "Kaya", "150120025", "ahmet025", 3.5, 5);
        Course course1 = new Course("C101", "Introduction to Programming1", 3, null, null, "", 5);
        Course course2 = new Course("C102", "Introduction to Programming2", 3, null, null, "", 5);
        Course course3 = new Course("C103", "Introduction to Programming3", 3, null, null, "", 7);
        Course course4 = new Course("C104", "Introduction to Programming4", 3, null, null, "", 7);
        Course course5 = new Course("C105", "Introduction to Programming5", 3, null, null, "", 6);

        registrationSystem.getCourses().add(course1);
        registrationSystem.getCourses().add(course2);
        registrationSystem.getCourses().add(course3);
        registrationSystem.getCourses().add(course4);
        registrationSystem.getCourses().add(course5);

        student.getCourses().add(course5); 

        List<Course> availableCourses = registrationSystem.getAvailableCourses(student);
        
        assertEquals(2, availableCourses.size());
        assertTrue(availableCourses.contains(course1));
        assertTrue(availableCourses.contains(course2));
        assertFalse(availableCourses.contains(course3));
        assertFalse(availableCourses.contains(course4));
        assertFalse(availableCourses.contains(course5));
    }

    @Test
    public void testGenerateStudentFiles() {
 
        Student student = new Student("Ahmet", "Kaya", "150120030", "ahmet030", 3.5, 5);
        
        registrationSystem.generateStudentFiles(student);
        
        String studentFileName = student.getStudentID() + ".json";
        File studentFile = new File(studentFileName);
        
        assertTrue(studentFile.exists());
        
        
    }
    @Test
    public void testAuthenticateAndEnroll() {
        Student student = new Student("Ahmet", "Kaya", "150120027", "ahmet031", 3.5, 5);
        registrationSystem.authenticateAndEnroll(student); 
        
    }

}