import unittest
from CourseRegistrationSystem import CourseRegistrationSystem
from Student import Student
from Advisor import Advisor
from Course import Course
import os

class TestCourseRegistrationSystem(unittest.TestCase):

    def setUp(self):
        self.registrationSystem = CourseRegistrationSystem()

    def test_display_enrolled_students_for_advisor(self):
        student = Student("Ahmet", "Kaya", "150120027", "ahmet027", 3.5, 5)
        advisor = Advisor("Serap", "Korkmaz", "marmara", [], [])
        course = Course("C101", "Introduction to Programming", 3, None, None, "", 5)
        student.add_course(course)
        advisor.set_students([student])
        self.assertTrue(advisor.has_student(student))


    def test_select_courses(self):
        student = Student("Ahmet", "Kaya", "150120027", "ahmet027", 3.5, 5)
        course5 = Course("C101", "Introduction to Programming", 3, None, None, "", 5)
        course2 = Course("C102", "Introduction to Programming2", 3, None, None, "", 5)
        course3 = Course("C103", "Introduction to Programming3", 3, None, None, "", 5)

        self.registrationSystem.get_courses().append(course5)
        self.registrationSystem.get_courses().append(course2)
        self.registrationSystem.get_courses().append(course3)

        self.registrationSystem.select_courses(student)

        available_courses = self.registrationSystem.get_available_courses(student)
        self.assertFalse(len(available_courses) == 0)

    def test_get_available_courses(self):
        student = Student("Ahmet", "Kaya", "150120025", "ahmet025", 3.5, 5)
        course1 = Course("C101", "Introduction to Programming1", 3, None, None, "", 5)
        course2 = Course("C102", "Introduction to Programming223", 3, None, None, "", 5)
        course3 = Course("C103", "Introduction to Programming3", 3, None, None, "", 7)
        course4 = Course("C104", "Introduction to Programming4", 3, None, None, "", 7)
        course5 = Course("C105", "Introduction to Programming5", 3, None, None, "", 6)

        self.registrationSystem.get_courses().append(course1)
        self.registrationSystem.get_courses().append(course2)
        self.registrationSystem.get_courses().append(course3)
        self.registrationSystem.get_courses().append(course4)
        self.registrationSystem.get_courses().append(course5)

        student.add_course(course5)

        available_courses = self.registrationSystem.get_available_courses(student)

        self.assertEqual(2, len(available_courses))
        self.assertIn(course1, available_courses)
        self.assertIn(course2, available_courses)
        self.assertNotIn(course3, available_courses)
        self.assertNotIn(course4, available_courses)
        self.assertNotIn(course5, available_courses)

    def test_generate_student_files(self):
        student = Student("Ahmet", "Kaya", "150120030", "ahmet030", 3.5, 5)
        self.registrationSystem.generate_student_files(student)
        student_file_name = f"{student.get_student_id()}.json"
        self.assertTrue(os.path.exists(student_file_name))

    def test_authenticate_and_enroll(self):
        student = Student("Ahmet", "Kaya", "150120027", "ahmet031", 3.5, 5)
        self.registrationSystem.authenticate_and_enroll(student)


if __name__ == '__main__':
    unittest.main()
