from Lecturer import Lecturer

class Advisor(Lecturer):
    def __init__(self, first_name, last_name, password, assigned_courses, students):
        super().__init__(first_name, last_name, assigned_courses)
        self.password = password
        self.students = students

    def get_password(self):
        return self.password

    def set_password(self, password):
        self.password = password

    def get_students(self):
        return self.students

    def set_students(self, students):
        self.students = students

    def has_student(self, student):
        return student in self.students
