
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CourseRegistrationSystem {
    private List<Student> students;
    private List<Course> courses;
    private List<Advisor> advisors;
    private List<Transcript> transcripts;

    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
    }
    
    public CourseRegistrationSystem() {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.advisors = new ArrayList<>();
        this.transcripts = new ArrayList<>();
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
                        ((Long) studentInFile.get("semester")).intValue()
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
                        null,
                        null,
                        (String) courseInFile.get("prerequisite"),
                        ((Long) courseInFile.get("coursesemester")).intValue()
                );
                courses.add(course);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadAdvisorsFromJson(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray advisorArray = (JSONArray) obj;

            for (Object advisorObj : advisorArray) {
                JSONObject advisorInFile = (JSONObject) advisorObj;
                String firstName = (String) advisorInFile.get("firstName");
                String lastName = (String) advisorInFile.get("lastName");
                String password = (String) advisorInFile.get("password");

                JSONArray studentsArray = (JSONArray) advisorInFile.get("students");
                ArrayList<Student> students = new ArrayList<>();

                for (Object studentObj : studentsArray) {
                    JSONObject studentInFile = (JSONObject) studentObj;
                    Student student = new Student(
                            (String) studentInFile.get("firstName"),
                            (String) studentInFile.get("lastName"),
                            (String) studentInFile.get("studentID"),
                            (String) studentInFile.get("password"),
                            ((Double) studentInFile.get("GPA")).doubleValue(),
                            ((Long) studentInFile.get("semester")).intValue()
                    );
                    students.add(student);
                }

                Advisor advisor = new Advisor(firstName, lastName, password, new ArrayList<>(), students);
                advisors.add(advisor);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void authenticateAndEnroll(Person person) {
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;

        while (!authenticated) {
            System.out.print("Email address: ");
            String enteredEmail = scanner.nextLine().toLowerCase();
            System.out.print("Password: ");
            String enteredPassword = scanner.nextLine();
          
            if (person instanceof Student) {
                Student currentStudent = (Student) person;
                Transcript studentTranscript = new Transcript(currentStudent.getStudentID());
                String signedEmail = (currentStudent.createEmailAddress()).toLowerCase();
                String signedPassword = currentStudent.getPassword();
                if (signedEmail.equals(enteredEmail) && signedPassword.equals(enteredPassword)) {
                    System.out.println("Student Login successful.");
                    selectCourses(currentStudent,studentTranscript);
                    generateStudentFiles(currentStudent);
                    authenticated = true;
                }
            } else if (person instanceof Advisor) {
                Advisor currentAdvisor = (Advisor) person;
                String signedEmail = (currentAdvisor.createEmailAddress()).toLowerCase();
                String signedPassword = currentAdvisor.getPassword();
                if (signedEmail.equals(enteredEmail) && signedPassword.equals(enteredPassword)) {
                    System.out.println("Advisor Login successful.");
                    displayEnrolledStudentsForAdvisor(currentAdvisor);
                    authenticated = true;
                }
            }

            if (!authenticated) {
                System.out.println("Login failed. Please try again.");
            }
        }
    }
    public boolean hasPrerequisites(Student student, Course course) {
        String prerequisiteCourseID = course.getPrerequisite();

        if (prerequisiteCourseID.isEmpty()) {
            return true;
        }

        for (Course completedCourse : student.getCourses()) {
            if (completedCourse.getCourseID().equals(prerequisiteCourseID)) {
                return true;
            }
        }

        return false;
    }


    void selectCourses(Student authenticatedStudent, Transcript transcript) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome " + authenticatedStudent.getFirstName() + " " + authenticatedStudent.getLastName() + "\n");

        List<Course> availableCourses = getAvailableCourses(authenticatedStudent);

        if (availableCourses.size() == 0) {
            System.out.println("There are no available courses for your semester.");
            return;
        }

        int coursesEnrolled = 0;
        
        if(hasEnrollmentFile(authenticatedStudent)) {
            String studentFileName2 = authenticatedStudent.getStudentID() + ".json";

            File studentFile2 = new File(studentFileName2);

            if (studentFile2.exists()) {
                JSONParser parser = new JSONParser();

                try (FileReader reader = new FileReader(studentFileName2)) {
                    Object obj = parser.parse(reader);
                    JSONObject studentInFile = (JSONObject) obj;

                    JSONArray enrolledCoursesArray = (JSONArray) studentInFile.get("enrolledCourses");

                    coursesEnrolled = enrolledCoursesArray.size(); 
                    System.out.println("You have succesfully selected " + coursesEnrolled + " courses! ");
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                coursesEnrolled = 0;
            }
        }
        	while (coursesEnrolled < 5) {
                if (availableCourses.size() == 0) {
                    System.out.println("There are no available courses.");
                    return;
                }
                System.out.println("Available Courses:");

                for (int i = 0; i < availableCourses.size(); i++) {
                    Course course = availableCourses.get(i);
                    System.out.println((i + 1) + ". " + course.getCourseName());
                }

                System.out.println("Select courses by entering their numbers one by one, or enter 0 to exit: ");
                String userInput = scanner.nextLine();

                if (userInput.equals("0")) {
                    break;
                }
                
                String[] selectedCourseNumbers = userInput.split(",");
               
                for (String courseNumber : selectedCourseNumbers) {
                    int choice = Integer.parseInt(courseNumber);

                    if (choice >= 1 && choice <= availableCourses.size()) {
                        Course selectedCourse = availableCourses.get(choice - 1);

                        if (hasPrerequisites(authenticatedStudent, selectedCourse)) {

                            authenticatedStudent.addCourse(selectedCourse);
                            System.out.println("Enrolled in course: " + selectedCourse.getCourseID() + " - " + selectedCourse.getCourseName());
                            availableCourses.remove(selectedCourse);
                            coursesEnrolled++;
                        } else {
                            System.out.println("You haven't completed the prerequisite for course: " + selectedCourse.getCourseID());
                        }
                    } else {
                        System.out.println("Inappropriate selection: " + courseNumber + ". Please enter an appropriate course.");
                    }
                }

                System.out.println();
            }
        }
     List<Course> getAvailableCourses(Student authenticatedStudent) {
    	
        List<Course> availableCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getCoursesemester() == authenticatedStudent.getSemester() && !authenticatedStudent.getCourses().contains(course)) {
                availableCourses.add(course);
            }
        }

        return availableCourses;
    }

    void generateStudentFiles(Student student) {
            JSONObject studentInFile = new JSONObject();
            JSONArray enrolledCoursesArray = new JSONArray();
            studentInFile.put("studentID", student.getStudentID());
            studentInFile.put("firstName", student.getFirstName());
            studentInFile.put("lastName", student.getLastName());
            studentInFile.put("semester", student.getSemester());
            studentInFile.put("GPA", student.getGPA());
            for (Course course : student.getCourses()) {
                JSONObject courseInFile = new JSONObject();
                courseInFile.put("courseID", course.getCourseID());
                courseInFile.put("courseName", course.getCourseName());
                courseInFile.put("credit", course.getCredit());
                courseInFile.put("prerequisite", course.getPrerequisite());
                courseInFile.put("coursesemester", course.getCoursesemester());
                enrolledCoursesArray.add(courseInFile);
            }

            studentInFile.put("enrolledCourses", enrolledCoursesArray);

            try (FileWriter file = new FileWriter(student.getStudentID() + ".json")) {
                file.write(studentInFile.toJSONString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    

    public void displayEnrolledStudentsForAdvisor(Advisor advisor) {
        List<Student> enrolledStudents = advisor.getStudents();

        if (enrolledStudents.isEmpty()) {
            System.out.println("No students enrolled in courses.");
        } else {
            System.out.println("Enrolled Students:");

            for (Student student : enrolledStudents) {
                if (hasEnrollmentFile(student)) {
                    System.out.println("Student: " + student.getFirstName() + " " + student.getLastName());

                    String studentFileName = student.getStudentID() + ".json";

                    File studentFile = new File(studentFileName);

                    if (studentFile.exists()) {
                        JSONParser parser = new JSONParser();

                        try (FileReader reader = new FileReader(studentFileName)) {
                            Object obj = parser.parse(reader);
                            JSONObject studentInFile = (JSONObject) obj;

                            JSONArray enrolledCoursesArray = (JSONArray) studentInFile.get("enrolledCourses");

                            if (enrolledCoursesArray.isEmpty()) {
                                System.out.println("No enrolled courses for this student.");
                            } else {
                                System.out.println("Enrolled Courses:");
                                
                                
                                
                                
                                Iterator<Object> iterator = enrolledCoursesArray.iterator();
                                while (iterator.hasNext()) {
                                    JSONObject courseInFile = (JSONObject) iterator.next();
                                    String courseID = (String) courseInFile.get("courseID");
                                    String courseName = (String) courseInFile.get("courseName");
                                    System.out.println(courseID + " - " + courseName);

                                    Scanner scanner = new Scanner(System.in);
                                    System.out.println("Do you want to approve (A) or reject (R) the course selection for this student?");
                                    String approvalDecision = scanner.next();

                                    if (approvalDecision.equalsIgnoreCase("A")) {
                                        System.out.println("Course selection approved!");
                                    } else if (approvalDecision.equalsIgnoreCase("R")) {
                                        String courseToRemove1 = courseName;
                                        removeCourseInfoFromStudentFile(studentFileName, courseToRemove1);

                                        iterator.remove();

                                        System.out.println("Course selection rejected!");
                                    } else {
                                        System.out.println("Invalid input. Please enter 'A' for approve or 'R' for reject.");
                                    }
                                }
                            }
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("No student has chosen courses yet.");
                    }

                    System.out.println("------");
                }
            }
        }
    }


    private void removeCourseInfoFromStudentFile(String studentFileName, String courseToRemove) {
        try {
            FileReader fileReader = new FileReader(studentFileName);
            JSONParser jsonParser = new JSONParser();
            JSONObject studentInFile = (JSONObject) jsonParser.parse(fileReader);

            JSONArray enrolledCoursesArray = (JSONArray) studentInFile.get("enrolledCourses");

            int indexToRemove = -1;
            for (int i = 0; i < enrolledCoursesArray.size(); i++) {
                JSONObject courseInFile = (JSONObject) enrolledCoursesArray.get(i);
                String courseName = (String) courseInFile.get("courseName");
                if (courseName.equalsIgnoreCase(courseToRemove)) {
                    indexToRemove = i;
                    break;
                }
            }
            if (indexToRemove != -1) {
                enrolledCoursesArray.remove(indexToRemove);

                try (FileWriter fileWriter = new FileWriter(studentFileName)) {
                    fileWriter.write(studentInFile.toJSONString());  
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
        private boolean hasEnrollmentFile(Student student) {
            String studentFileName = student.getStudentID() + ".json";
            File studentFile = new File(studentFileName);
            return studentFile.exists();
        }
        
        public void loadTranscriptsFromJson(String filePath) {
            JSONParser parser = new JSONParser();

            try (FileReader reader = new FileReader(filePath)) {
                Object obj = parser.parse(reader);
                JSONArray transcriptArray = (JSONArray) obj;

                for (Object transcriptObj : transcriptArray) {
                    JSONObject transcriptInFile = (JSONObject) transcriptObj;

                    String studentID = (String) transcriptInFile.get("studentID");

                    // Find the student associated with the transcript
                    Student student = findStudentByID(studentID);

                    if (student != null) {
                        Transcript transcript = new Transcript(student.getStudentID());

                        transcript.setCompletedCourses(castToStringList(transcriptInFile.get("completedCourses")));
                        transcript.setFailedCourses(castToStringList(transcriptInFile.get("failedCourses")));
                        transcript.setGPA(((Double) transcriptInFile.get("gpa")).doubleValue());
                        //transcript.setCompletedCredits(((Long) transcriptInFile.get("completedCredits")).intValue());
                        //transcript.setCreditsTaken(((Long) transcriptInFile.get("creditsTaken")).intValue());

                        // Set the transcript for the student
                        student.setTranscript(transcript);
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        
        private List<String> castToStringList(Object rawList) {
            List<String> stringList = new ArrayList<>();
            if (rawList instanceof List<?>) {
                for (Object item : (List<?>) rawList) {
                    if (item instanceof String) {
                        stringList.add((String) item);
                    }
                    else {
                        // Handle other types or log a warning
                    }
                }
            }
            return stringList;
        }

        Student findStudentByID(String studentID) {
            for (Student student : students) {
                if (student.getStudentID().equals(studentID)) {
                    return student;
                }
            }
            return null;
        }


    


        public static void main(String[] args) {
            CourseRegistrationSystem registrationSystem = new CourseRegistrationSystem();
            registrationSystem.loadStudentsFromJson("students.json");
            registrationSystem.loadCoursesFromJson("courses.json");
            registrationSystem.loadAdvisorsFromJson("advisors.json");
            registrationSystem.loadTranscriptsFromJson("transcripts.json");

            Scanner scanner = new Scanner(System.in);
            boolean continueLogin = true;

            while (continueLogin) {
                System.out.println("How do you wish to enter the system?");
                System.out.println("1. Student");
                System.out.println("2. Advisor");
                System.out.println("0. Exit");

                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.print("Enter Student ID: ");
                    String studentID = scanner.next();

                    Student selectedStudent = null;
                    for (Student student : registrationSystem.students) {
                        if (student.getStudentID().equals(studentID)) {
                            selectedStudent = student;
                            break;
                        }
                    }

                    if (selectedStudent != null) {
                        registrationSystem.authenticateAndEnroll(selectedStudent);
                    } else {
                        System.out.println("Student not found. Please try again.");
                    }
                } else if (choice == 2) {
                    System.out.print("Enter Advisor First Name: ");
                    String advisorFirstName = scanner.next().toLowerCase();
                    System.out.print("Enter Advisor Last Name: ");
                    String advisorLastName = scanner.next().toLowerCase();

                    Advisor selectedAdvisor = null;
                    for (Advisor advisor : registrationSystem.advisors) {
                        if (advisor.getFirstName().toLowerCase().equals(advisorFirstName) && advisor.getLastName().toLowerCase().equals(advisorLastName)) {
                            selectedAdvisor = advisor;
                            break;
                        }
                    }

                    if (selectedAdvisor != null) {
                        registrationSystem.authenticateAndEnroll(selectedAdvisor);
                    } else {
                        System.out.println("Advisor not found. Please try again.");
                    }
                }

                 else if (choice == 0) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                 else {
                    System.out.println("Invalid choice. Exiting...");
                    continueLogin = false;
                }

                if (continueLogin) {
                    scanner.nextLine();
                    System.out.println("Do you wish to enter the system again? (yes/no)");
                    String again = scanner.nextLine().toLowerCase();
                    if (again.equals("no")) {
                        System.out.println("Exiting...");
                        continueLogin = false;
                    }
                    else if (again.equals("yes")){
                    	// do nothing
                    }
                    else {
                    	System.out.println("Invalid choice. Exiting...");
                    	continueLogin = false;
                    }
                }
            }
        } 
	





public Advisor findAdvisorByName(String firstName, String lastName) {
    for (Advisor advisor : advisors) {
        if (advisor.getFirstName().equalsIgnoreCase(firstName) && advisor.getLastName().equalsIgnoreCase(lastName)) {
            return advisor;
        }
    }
    return null;
}

public Course findCourseByID(String courseID) {
    for (Course course : courses) {
        if (course.getCourseID().equalsIgnoreCase(courseID)) {
            return course;
        }
    }
    return null;
}}