package com.example.springSQL;

import com.example.springSQL.dao.StudentDAO;
import com.example.springSQL.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSqlApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO){
		return runner -> {
			createstudent(studentDAO);
			//readStudent(studentDAO);
			//queryForStudents(studentDAO);
			//queryForStudentsByLastName(studentDAO);
			//updateStudent(studentDAO);
			//deleteStudent(studentDAO);

		};
	}

	private void deleteStudent(StudentDAO studentDAO) {
		int id =2;
		System.out.println("deleting student id: " + id);
		studentDAO.delete(id);
	}

	private void updateStudent(StudentDAO studentDAO) {
		int studentId = 1;
		Student student = studentDAO.findById(studentId);
		student.setFirstName("Mickie");
		studentDAO.update(student);

		System.out.println(student);
	}

	private void queryForStudentsByLastName(StudentDAO studentDAO) {
		List<Student> students = studentDAO.findByLastName("Bullata");
		for(Student s: students){
			System.out.println(s);
		}
	}

	private void queryForStudents(StudentDAO studentDAO) {
		List<Student> students = studentDAO.findAll();
		for(Student temp: students){
			System.out.println(temp);
		}
	}

	private void readStudent(StudentDAO studentDAO) {
		System.out.println("Creating Student ...");
		Student temp = new Student("Hanna", "Bullata", "realtime@gmail.com");

		System.out.println("Saving Student ....");
		studentDAO.save(temp);

		int theId = temp.getId();

		System.out.println("Retrieving id: " + theId);

		Student newo = studentDAO.findById(theId);

		System.out.println("Found the student: " + newo);

	}

	private void createstudent(StudentDAO studentDAO) {

		System.out.println("Creating a new student object .....");
		Student tempStudent = new Student("Michael", "DeSanta", "mikchel@gmail.com");
		studentDAO.save(tempStudent);
		tempStudent = new Student("Franklyn", "Clinton", "frankie@gmail.com");
		studentDAO.save(tempStudent);
		tempStudent = new Student("Trevor", "Phillips", "T@gmail.com");
		studentDAO.save(tempStudent);
		System.out.println("Saving ...");
	}

}
