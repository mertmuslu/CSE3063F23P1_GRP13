class Course:
    def __init__(self, course_id, course_name, credit, lecturer, students, prerequisite, course_semester):
        self.course_id = course_id
        self.course_name = course_name
        self.credit = credit
        self.students = []
        self.prerequisite = prerequisite
        self.course_semester = course_semester

    def get_course_id(self):
        return self.course_id

    def set_course_id(self, course_id):
        self.course_id = course_id

    def get_course_name(self):
        return self.course_name

    def set_course_name(self, course_name):
        self.course_name = course_name

    def get_credit(self):
        return self.credit

    def set_credit(self, credit):
        self.credit = credit

    def get_students(self):
        return self.students

    def set_students(self, student):
        self.students.append(student)

    def get_prerequisite(self):
        return self.prerequisite

    def get_course_semester(self):
        return self.course_semester
