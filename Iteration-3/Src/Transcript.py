class Transcript:
    def __init__(self, student_id):
        self.student_id = student_id
        self.gpa = 0.0
        self.completed_courses = []
        self.failed_courses = []
        self.completed_credits = 0
        self.credits_taken = 0

    def get_student_id(self):
        return self.student_id

    def set_student_id(self, student_id):
        self.student_id = student_id

    def get_gpa(self):
        return self.gpa

    def set_gpa(self, gpa):
        self.gpa = gpa

    def get_completed_courses(self):
        return self.completed_courses

    def set_completed_courses(self, course_list):
        self.completed_courses = course_list

    def get_failed_courses(self):
        return self.failed_courses

    def set_failed_courses(self, course_list):
        self.failed_courses = course_list

    def get_completed_credits(self):
        return self.completed_credits

    def set_completed_credits(self, completed_credits):
        self.completed_credits = completed_credits

    def get_credits_taken(self):
        return self.credits_taken

    def set_credits_taken(self, credits_taken):
        self.credits_taken = credits_taken

    def add_completed_course(self, course):
        self.completed_courses.append(course)
        self.completed_credits += course.get_credit()
        self.credits_taken += course.get_credit()

    def add_failed_course(self, course):
        self.failed_courses.append(course)
        self.credits_taken += course.get_credit()
