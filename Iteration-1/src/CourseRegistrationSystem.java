import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;

public class CourseRegistrationSystem {
    private List<Student> students;
    private List<Course> courses;

    public CourseRegistrationSystem() {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
    }
    public void loadStudentsFromJson(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray studentArray = (JSONArray) obj;

            for (Object studentObj : studentArray) {
                JSONObject studentInFile = (JSONObject) studentObj;
                Student student = new Student(
                        (String) studentInFile.get("firstName"),
                        (String) studentInFile.get("lastName"),
                        (String) studentInFile.get("studentID"),
                        (String) studentInFile.get("password"),
                        ((Double) studentInFile.get("GPA")).doubleValue(),
                        ((Long) studentInFile.get("year")).intValue()
                );
                students.add(student);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadCoursesFromJson(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray courseArray = (JSONArray) obj;

            for (Object courseObj : courseArray) {
                JSONObject courseInFile = (JSONObject) courseObj;
                Course course = new Course(
                        (String) courseInFile.get("courseID"),
                        (String) courseInFile.get("courseName"),
                        ((Long) courseInFile.get("credit")).intValue(),
                        null,  // Assuming don't have lecturer information
                        null,  // Assuming don't have prerequisites information we are going to add in 2nd iteration
                        (String) courseInFile.get("prerequisite"),  
                        ((Long) courseInFile.get("courseYear")).intValue()  
                );
                courses.add(course);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }}
    public void authenticateAndEnroll(Student student) {
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
        while (!authenticated) {
            System.out.print("Username: ");
            String enteredUsername = scanner.nextLine().toLowerCase();
            System.out.print("Password: ");
            String enteredPassword = scanner.nextLine();
            for (Student currentStudent : students) {
                if (authenticateUser(currentStudent, enteredUsername, enteredPassword)) {    
                	System.out.println("Login successfull.");
                    selectCourses(currentStudent);
                    generatestudentInFileFiles();            
                    authenticated = true;  
                    break;  
                }
}
            if (!authenticated) {
                System.out.println("Login failed. Please try again.");
            }
    } }

    public boolean authenticateUser(Student student, String enteredUsername, String enteredPassword) {
        String signedUsername = (student.getFirstName() + student.getLastName()).toLowerCase();
        String signedPassword = student.getPassword();

        return signedUsername.equals(enteredUsername) && signedPassword.equals(enteredPassword);
    }

    private List<Course> getAvailableCourses(Student authenticatedStudent) {
        List<Course> avaCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getCourseYear() == authenticatedStudent.getYear() && !authenticatedStudent.getCourses().contains(course)) {
                avaCourses.add(course);
            }
        }

        return avaCourses;
    }
    public void selectCourses(Student authenticatedStudent) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome " + authenticatedStudent.getFirstName() + " " + authenticatedStudent.getLastName() + "\n");

        List<Course> avaCourses = getAvailableCourses(authenticatedStudent);

        if (avaCourses.size() == 0) {
            System.out.println("There are no available courses for your year.");
            return;
        }

        int coursesEnrolled = 0;

        while (coursesEnrolled < 5) {
        	if (avaCourses.size() == 0) {
                System.out.println("There are no available courses.");
                return;
            }
            System.out.println("Available Courses:");
            
            for (int i = 0; i < avaCourses.size(); i++) {
                Course course = avaCourses.get(i);
                System.out.println((i + 1) + ". " + course.getCourseName());
            }

            System.out.println("Select courses by entering their numbers (e.g., 1), or enter 0 to exit: ");
            String userInput = scanner.nextLine();

            if (userInput.equals("0")) {
                break; 
            }

            String[] selectedCourseNumbers = userInput.split(",");
            for (String courseNumber : selectedCourseNumbers) {
                int choice = Integer.parseInt(courseNumber);
                
                if (choice >= 1 && choice <= avaCourses.size()) {
                    Course selectedCourse = avaCourses.get(choice - 1);
                    authenticatedStudent.addCourse(selectedCourse);
                    System.out.println("Enrolled in course: " + selectedCourse.getCourseID() + " - " + selectedCourse.getCourseName());                 
                    avaCourses.remove(selectedCourse);

                    coursesEnrolled++; 
                } else {
                    System.out.println("Inappropriate selection: " + courseNumber + ". Please enter an appropriate course.");
                }
            }

            System.out.println(); 
        }
    }




    public void generatestudentInFileFiles() {
        for (Student student : students) {
            JSONObject studentInFile = new JSONObject();
            JSONArray enrolledCoursesArray = new JSONArray();
            studentInFile.put("studentID", student.getStudentID());
            studentInFile.put("firstName", student.getFirstName());
            studentInFile.put("lastName", student.getLastName());
            studentInFile.put("year", student.getYear());
            studentInFile.put("GPA", student.getGPA());
            for (Course course : student.getCourses()) {
                JSONObject courseInFile = new JSONObject();
                courseInFile.put("courseID", course.getCourseID());
                courseInFile.put("courseName", course.getCourseName());
                courseInFile.put("credit", course.getCredit());
                courseInFile.put("prerequisite", course.getPrerequisite());
                courseInFile.put("courseYear", course.getCourseYear());
                enrolledCoursesArray.add(courseInFile);
            }

            studentInFile.put("enrolledCourses", enrolledCoursesArray);

            try (FileWriter file = new FileWriter(student.getStudentID() + ".json")) {
                file.write(studentInFile.toJSONString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CourseRegistrationSystem registrationSystem = new CourseRegistrationSystem();
        registrationSystem.loadStudentsFromJson("students.json");
        registrationSystem.loadCoursesFromJson("courses.json");

        for (Student student : registrationSystem.students) {
            registrationSystem.authenticateAndEnroll(student);
        }
    }
}