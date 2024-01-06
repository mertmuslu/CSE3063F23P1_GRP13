from Person import Person


class Student(Person):
    def __init__(self, first_name, last_name, student_id, password, gpa, semester):
        super().__init__(first_name, last_name)
        self.student_id = student_id
        self.password = password
        self.gpa = gpa
        self.semester = semester
        self.courses = []
        self.transcript = None

    def get_password(self):
        return self.password

    def set_password(self, password):
        self.password = password

    def get_gpa(self):
        return self.gpa

    def set_gpa(self, gpa):
        self.gpa = gpa

    def get_student_id(self):
        return self.student_id

    def set_student_id(self, student_id):
        self.student_id = student_id

    def get_semester(self):
        return self.semester

    def set_semester(self, semester):
        self.semester = semester

    def add_course(self, course):
        self.courses.append(course)

    def get_courses(self):
        return self.courses

    def get_transcript(self):
        return self.transcript

    def get_first_name(self):
        return self.first_name

    def get_last_name(self):
        return self.last_name

    def set_transcript(self, transcript):
        self.transcript = transcript

    def create_email_address(self):
        return self.first_name.lower() + self.last_name.lower() + "@marun.edu.tr"
