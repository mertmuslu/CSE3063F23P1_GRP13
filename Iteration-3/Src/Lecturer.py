from Person import Person

class Lecturer(Person):
    def __init__(self, first_name, last_name, assigned_courses):
        super().__init__(first_name, last_name)
        self.assigned_courses = []

    def get_assigned_courses(self):
        return self.assigned_courses

    def set_assigned_courses(self, course):
        self.assigned_courses.append(course)

    def create_email_address(self):
        return self.first_name.lower() + self.last_name.lower() + "@marmara.edu.tr"
