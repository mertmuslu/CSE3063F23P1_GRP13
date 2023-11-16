package oop_proje;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
                JSONObject studentJson = (JSONObject) studentObj;
                Student student = new Student(
                        (String) studentJson.get("firstName"),
                        (String) studentJson.get("lastName"),
                        (String) studentJson.get("studentID"),
                        (String) studentJson.get("password"),
                        ((Double) studentJson.get("GPA")).doubleValue(),
                        ((Long) studentJson.get("year")).intValue()
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
                JSONObject courseJson = (JSONObject) courseObj;
                Course course = new Course(
                        (String) courseJson.get("courseID"),
                        (String) courseJson.get("courseName"),
                        ((Long) courseJson.get("credit")).intValue(),
                        null,  // Assuming you don't have lecturer information in the JSON
                        null,  // Assuming you don't have prerequisites information in the JSON
                        (String) courseJson.get("prerequisite"),  // Assuming you have the prerequisite information
                        ((Long) courseJson.get("courseYear")).intValue()  // Added for course year
                );
                courses.add(course);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void enrollStudents() {
        for (Student student : students) {
            int maxCourses = 5; // Maximum number of courses a student can take
            int coursesEnrolled = 0;

            for (Course course : courses) {
                // Check if the course is suitable for the student's year
                if (course.getCourseYear() == student.getYear()) {
                    // Check if the student has not exceeded the maximum number of courses
                    if (coursesEnrolled < maxCourses) {
                        // Check if the student meets the prerequisites for the course
                            // Enroll the student in the course
                            student.addCourse(course);
                            System.out.println("Student " + student.getStudentID() + " enrolled in course " + course.getCourseID());
                            coursesEnrolled++;
          
                    }
                }
            }
        }
    }


    public void generateStudentJsonFiles() {
        for (Student student : students) {
            JSONObject studentJson = new JSONObject();
            studentJson.put("studentID", student.getStudentID());
            studentJson.put("firstName", student.getFirstName());
            studentJson.put("lastName", student.getLastName());
            studentJson.put("year", student.getYear());
            studentJson.put("GPA", student.getGPA());

            JSONArray enrolledCoursesArray = new JSONArray();
            for (Course course : student.getCourses()) {
                JSONObject courseJson = new JSONObject();
                courseJson.put("courseID", course.getCourseID());
                courseJson.put("courseName", course.getCourseName());
                courseJson.put("credit", course.getCredit());
                courseJson.put("prerequisite", course.getPrerequisite());
                courseJson.put("courseYear", course.getCourseYear());
                enrolledCoursesArray.add(courseJson);
            }

            studentJson.put("enrolledCourses", enrolledCoursesArray);

            // Write JSON to a file named [studentID].json
            try (FileWriter file = new FileWriter(student.getStudentID() + ".json")) {
                file.write(studentJson.toJSONString());
                System.out.println("File created for student " + student.getStudentID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CourseRegistrationSystem registrationSystem = new CourseRegistrationSystem();

        // Load students and courses from JSON files
        registrationSystem.loadStudentsFromJson("students.json");
        registrationSystem.loadCoursesFromJson("courses.json");

        // Enroll students in courses
        registrationSystem.enrollStudents();

        // Generate JSON files for each student
        registrationSystem.generateStudentJsonFiles();
    }
}
