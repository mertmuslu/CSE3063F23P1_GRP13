import json
import os
from Course import Course
from Student import Student
from Advisor import Advisor
from Transcript import Transcript

class CourseRegistrationSystem:
    def __init__(self):
        self.students = []
        self.courses = []
        self.advisors = []
        self.transcripts = []

    def has_prerequisites(self, student, course):
        prerequisites = course.get_prerequisite()

        if not prerequisites:
            return True

        completed_courses = [c["courseID"] for c in student.get_transcript().get_completed_courses()]
        return any(prerequisite in prerequisites for prerequisite in completed_courses)

    def get_available_courses(self, student):
        available_courses = []

        for course in self.courses:
            if student.get_semester() == course.get_course_semester():
                available_courses.append(course)

        return available_courses

    def get_students(self):
        return self.students

    def get_courses(self):
        return self.courses

    def load_students_from_json(self, file_path):
        try:
            with open(file_path, 'r') as file:
                student_data = json.load(file)
                for student_info in student_data:
                    student = Student(student_info["firstName"], student_info["lastName"],
                                      student_info["studentID"], student_info["password"],
                                      student_info["GPA"], student_info["semester"])
                    self.students.append(student)
        except FileNotFoundError:
            print(f"File not found: {file_path}")

    def load_courses_from_json(self, file_path):
        try:
            with open(file_path, 'r') as file:
                course_data = json.load(file)
                for course_info in course_data:
                    course = Course(course_info["courseID"], course_info["courseName"],
                                    course_info["credit"], None, None,
                                    course_info["prerequisite"], course_info["coursesemester"])
                    self.courses.append(course)
        except FileNotFoundError:
            print(f"File not found: {file_path}")

    def load_advisors_from_json(self, file_path):
        try:
            with open(file_path, 'r') as file:
                advisor_data = json.load(file)
                for advisor_info in advisor_data:
                    advisor = Advisor(advisor_info["firstName"],
                                      advisor_info["lastName"],
                                      advisor_info["password"],
                                      [],
                                      advisor_info["students"])
                    self.advisors.append(advisor)

        except FileNotFoundError:
            print(f"File not found: {file_path}")

    def authenticate_and_enroll(self, person):
        while True:
            email = input("E-mail Address: ").lower()
            password = input("Password: ")

            if isinstance(person, Student):
                current_student = person
                signed_email = current_student.create_email_address().lower()
                signed_password = current_student.get_password()

                if signed_email == email and signed_password == password:
                    self.select_courses(current_student)
                    self.generate_student_files(current_student)
                    again = input("Do you want to exit from system? (yes/no): ").lower()
                    if(again == 'yes'):
                        break
                else:
                    print("Login Failed. Please try again.")
            elif isinstance(person, Advisor):
                current_advisor = person
                signed_email = current_advisor.create_email_address().lower()
                signed_password = current_advisor.get_password()
                if signed_email == email and signed_password == password:
                    print("Advisor" + current_advisor.first_name + current_advisor.last_name + ", login successful.")
                    self.display_enrolled_students_for_advisor(current_advisor)
                    again = input("Do you want to exit from system? (yes/no): ").lower()
                    if(again == 'yes'):
                        break
                else:
                    print("Login Failed. Please try again.")

    def remove_course_info_from_student_file(self, student_file_name, course_info):
        if os.path.exists(student_file_name):
            with open(student_file_name, "r") as file:
                data = json.load(file)
                enrolled_courses = data.get("enrolledCourses")

            if enrolled_courses:
                for course in enrolled_courses:
                    if course["courseID"] == course_info["courseID"]:
                        enrolled_courses.remove(course)

                with open(student_file_name, "w") as file:
                    json.dump(data, file, indent=4)

    def select_courses(self, authenticated_student):
        print(f"Welcome {authenticated_student.first_name} {authenticated_student.last_name}\n")
        available_courses = self.get_available_courses(authenticated_student)

        enrolled_courses = []
        student_file_name = f"{authenticated_student.get_student_id()}.json"
        if os.path.exists(student_file_name):
            with open(student_file_name, "r") as file:
                data = json.load(file)
                enrolled_course_ids = [course['courseID'] for course in data.get("enrolledCourses", [])]
                enrolled_courses = [self.find_course_by_id(course_id) for course_id in enrolled_course_ids]
        available_courses = [course for course in available_courses if course not in enrolled_courses]
        if not available_courses:
            print("There is not any course for your semester.")
            return

        courses_enrolled = 0

        if self.has_enrollment_file(authenticated_student):
            student_file_name = f"{authenticated_student.get_student_id()}.json"
            if os.path.exists(student_file_name):
                with open(student_file_name, "r") as file:
                    data = json.load(file)
                    enrolled_courses = data.get("enrolledCourses")
                    courses_enrolled = len(enrolled_courses) if enrolled_courses else 0

        while courses_enrolled < 5:
            if not available_courses:
                print("There is no available course.")
                break

            print("Available Courses:")

            for i, course in enumerate(available_courses, start=1):
                print(f"{i}. {course.get_course_name()}")

            print("To choose courses, enter their numbers line by line or press \"0\" to exit from selection system:")
            user_input = input()

            if user_input == "0":
                break
            selected_course_numbers = user_input.split(",")

            for course_number in selected_course_numbers:
                try:
                    choice = int(course_number)

                    if 1 <= choice <= len(available_courses):
                        selected_course = available_courses[choice - 1]

                        if self.has_prerequisites(authenticated_student, selected_course):

                            authenticated_student.add_course(selected_course)
                            print(f"You enrolled {selected_course.get_course_id()} - {selected_course.get_course_name()}.")
                            available_courses.remove(selected_course)
                            courses_enrolled += 1
                        else:
                            print(f"You has not completed prerequisite of {selected_course.get_course_id()} yet.")
                    else:
                        print(f"Invalid Choice: {course_number}.")
                except ValueError:
                    print(f"Invalid choice: {course_number}.")

    def generate_student_files(self, student):
        student_file_name = f"{student.get_student_id()}.json"
        if os.path.exists(student_file_name):
            with open(student_file_name, "r") as file:
                student_data = json.load(file)
                # Assume that 'enrolledCourses' is a list of course dictionaries
                existing_courses = {course["courseID"]: course for course in student_data.get("enrolledCourses", [])}
        else:
            student_data = {
                "studentID": student.get_student_id(),
                "firstName": student.get_first_name(),
                "lastName": student.get_last_name(),
                "semester": student.get_semester(),
                "GPA": student.get_gpa(),
                "enrolledCourses": []
            }
            existing_courses = {}

        # Add new courses to the existing ones, avoiding duplicates
        for course in student.get_courses():
            if course.get_course_id() not in existing_courses:
                course_data = {
                    "courseID": course.get_course_id(),
                    "courseName": course.get_course_name(),
                    "credit": course.get_credit(),
                    "prerequisite": course.get_prerequisite(),
                    "coursesemester": course.get_course_semester()
                }
                student_data["enrolledCourses"].append(course_data)

        # Save the updated data back to the JSON file
        with open(student_file_name, "w") as file:
            json.dump(student_data, file, indent=4)

    def display_enrolled_students_for_advisor(self, advisor):
        print(f"Welcome {advisor.get_first_name()} {advisor.get_last_name()}, your students: ")

        while True:
            for student in advisor.get_students():
                print(f"{student['firstName']} {student['lastName']} - {student['studentID']}")

            student_id = input("Enter a student ID or type 'exit' to exit from system: ")

            if student_id.lower() == 'exit':
                break
            selected_student = self.find_student_by_id(student_id)
            if selected_student:
                self.approve_courses_for_student(selected_student)
            else:
                print("Student can not be found.")

    def approve_courses_for_student(self, student):
        student_file_name = f"{student.get_student_id()}.json"
        if os.path.exists(student_file_name):
            with open(student_file_name, "r") as file:
                data = json.load(file)
                enrolled_courses = data.get("enrolledCourses")
                for course in enrolled_courses:
                    print(f"To approve/deny the Course: {course['courseID']} - {course['courseName']} (yes/no):")
                    approval = input().lower()
                    if approval == 'yes':
                        print(f"{course['courseID']} - {course['courseName']} onaylandı.")
                    else:
                        print(f"{course['courseID']} - {course['courseName']} reddedildi.")
                        self.remove_course_info_from_student_file(student_file_name, course)
                print(f"Courses approving/denying process is completed for this student.")
        else:
            print("This student has not enrolled any courses yet.")

    def has_enrollment_file(self, student):
        student_file_name = f"{student.get_student_id()}.json"
        return os.path.exists(student_file_name)

    def load_transcripts_from_json(self, file_path):
        try:
            with open(file_path, 'r') as file:
                transcript_data = json.load(file)
                for transcript_info in transcript_data:
                    student = self.find_student_by_id(transcript_info["studentID"])
                    if student:
                        transcript = Transcript(student.get_student_id())
                        transcript.set_completed_courses(transcript_info["completedCourses"])
                        transcript.set_failed_courses(transcript_info["failedCourses"])
                        transcript.set_gpa(transcript_info["gpa"])
                        student.set_transcript(transcript)

        except FileNotFoundError:
            print(f"File not found: {file_path}")
        except json.JSONDecodeError as e:
            print(f"Error occurred during the parsing of JSON file: {e}")

    def find_advisor_by_name(self, first_name, last_name):
        for advisor in self.advisors:
            if advisor.get_first_name().lower() == first_name.lower() and advisor.get_last_name().lower() == last_name.lower():
                return advisor
        return None

    def find_course_by_id(self, course_id):
        for course in self.courses:
            if course.get_course_id().lower() == course_id.lower():
                return course
        return None

    def find_student_by_id(self, student_id):
        for student in self.students:
            if student.get_student_id().lower() == student_id.lower():
                return student
        return None
if __name__ == "__main__":
    registration_system = CourseRegistrationSystem()
    registration_system.load_students_from_json("students.json")
    registration_system.load_courses_from_json("courses.json")
    registration_system.load_advisors_from_json("advisors.json")
    registration_system.load_transcripts_from_json("transcripts.json")

    continue_login = True

    while continue_login:
        print("How do you wish to enter the system?")
        print("1. Student")
        print("2. Advisor")
        print("0. Exit")

        choice = input()

        if choice == '1':
            student_id = input("Enter Student ID: ")


            selected_student = None
            for student in registration_system.students:
                if student.get_student_id() == student_id:
                    selected_student = student
                    break

            if selected_student is not None:
                registration_system.authenticate_and_enroll(selected_student)
            else:
                print("Student not found. Please try again.")
        elif choice == '2':

            advisor_first_name = input("Enter Advisor First Name: ").lower()
            advisor_last_name = input("Enter Advisor Last Name: ").lower()
            selected_advisor = None
            for advisor in registration_system.advisors:
                if advisor.get_first_name().lower() == advisor_first_name and advisor.get_last_name().lower() == advisor_last_name:
                    selected_advisor = advisor
                    break

            if selected_advisor is not None:
                registration_system.authenticate_and_enroll(selected_advisor)
            else:
                print("Advisor not found. Please try again.")
        elif choice == '0':
            print("Exiting...")
            continue_login = False
        else:
            print("Invalid choice. Exiting...")

        if continue_login:
            print("Do you wish to enter the system again? (yes/no)")
            again = input().lower()
            if again == "no":
                print("Exiting...")
                continue_login = False
            elif again != "yes":
                print("Invalid choice. Exiting...")
                continue_login = False
