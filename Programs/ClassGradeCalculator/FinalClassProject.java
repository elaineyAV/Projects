/*
 * Name: Elaine Vizcarra
 * CS1050
 * Section: T/R
 * Due Date: 12/3/24
 * Assignment: Final Project Design and Implement Solution
 * This program will calculate final grades of all students in their course so they can post the final grades. 
 * It will display final grade percentages and letter grades, followed by class average, minimum and maximum then print that to a file.
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FinalClassProject {
	public static void main(String[] args) {
	
		//Array categories and percentage weights
		final int TOTAL_CATEGORIES_1050 = 5;
		String[] categories1050 = {"Class Participation","Guided Exploration",
				"Quizzes","Project Percent","Final Exam"};
		double[] percentWeights1050 = {.12,.22,.22,.22,.22};
		final int MAX_STUDENTS_1050 = 10;
		
		Course course1050 = new Course("CS1050", categories1050, percentWeights1050, MAX_STUDENTS_1050);
		
		try {
			System.out.println("\nSetting up course " + course1050.getCourseName());
            courseSetUp(course1050, TOTAL_CATEGORIES_1050, "course1050.txt");
            System.out.println("\nTotal Students in " + course1050.getCourseName()+ 
            		": " + course1050.getNumStudents());
            course1050.displayCourseGrading(categories1050, percentWeights1050);
            course1050.postFinalGrades();           
            course1050.displayGradingStatistics();
            course1050.writeSummaryToFile();

        } catch (FileNotFoundException e) {
            System.out.println("Error: Can't Upload course information" + e.getMessage());
        }
		
		//Array categories and percentage weights
		final int TOTAL_CATEGORIES_2040 = 4;
		String[] categories = {"Class Participation","Homework",
				"Midterm","Final Exam"};
		double[] percentWeights = {.15, .25, .3, .3};
		final int MAX_STUDENTS_2040 = 4;
		
		Course course2040 = new Course("CS2040", categories, percentWeights, MAX_STUDENTS_2040);
		
        try {
            System.out.println("\nSetting up course " + course2040.getCourseName());
            courseSetUp(course2040, TOTAL_CATEGORIES_2040, "course2040.txt");
            System.out.println("\nTotal Students in " + course2040.getCourseName() +
                    ": " + course2040.getNumStudents());
            course2040.displayCourseGrading(categories, percentWeights);
            course2040.postFinalGrades();
            course2040.displayGradingStatistics();
            course2040.writeSummaryToFile();

        } catch (FileNotFoundException e) {
            System.out.println("Error: Can't Upload course information " + e.getMessage());
        }

	}//end of maiN
	
	public static void courseSetUp(Course course, int numberCategories, String filename) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(new File(filename));
		while (fileScanner.hasNextLine())
		{
			String firstName = fileScanner.next().trim();
			String lastName = fileScanner.next().trim();
			String email = fileScanner.next().trim();
			
			Person person = new Person(firstName, lastName, email);
			double[] categoryGrades = new double[numberCategories];
			Student currentStudent;
			Instructor currentInstructor;

			// Will create Student if grades exist for Person on File
			if (fileScanner.hasNextDouble()) 
			{
				for (int i = 0; i < numberCategories; i++)
				{
					categoryGrades[i] = Double.parseDouble(fileScanner.next().trim());
				}
				
				currentStudent = new Student(person, categoryGrades);	
				course.addStudent(currentStudent);
				}
			else 
			{
				currentInstructor = new Instructor(person);
				course.addInstructor(currentInstructor);
			}
		}
		fileScanner.close();
	} //end of courseSetUp
}
	

class Course {
	private String courseName;
	private String[] categories;
	private double[] categoryWeights;
	private final int MAX_STUDENTS;
	private Student[] students;
	private Instructor instructor;
	private double[] finalGrades;
	private char[] finalLetterGrades;
	private int numStudents;
	
	
	public Course(String courseName, String[] categories, double[] categoryWeights, int MAX_STUDENTS) {
		this.courseName = courseName;
		this.categories = categories;
		this.categoryWeights = categoryWeights;
		this.MAX_STUDENTS = MAX_STUDENTS;
		this.students = new Student[MAX_STUDENTS];
		
	}//end of Course constructor
		
	public String getCourseName() {
		return this.courseName;
	}
		
	public int getNumStudents() {
		return numStudents;
	}
	
	// Posts class average, min and max for course
	public void displayGradingStatistics() {
		System.out.print("***** Class Grade Statistics*****");
		System.out.printf("\nClass average: %.2f", calculateClassAvg(finalGrades));		
		System.out.printf("\nClass min: %.2f", getMinGrade(finalGrades));
		System.out.printf("\nClass max: %.2f", getMaxGrade(finalGrades));
		System.out.println("\n___________________________________\n");
	}
	
	public void displayCourseGrading(String[] names, double[] weights) {
		System.out.println("**********************************\nCS1050 Final Grade Calculator\n**********************************");
		System.out.println("------------------------------\nCategory: Percent\n------------------------------");
		for (int i= 0; i < names.length; i++) 
		{
			System.out.println(names[i] + ": " + (weights[i]*100) + "%");
		}
		System.out.println("-------------------------------\nLetter Grade Range\n-------------------------------");
		System.out.println("A: 90 to 100");
		System.out.println("B: 80 to < 90");
		System.out.println("C: 70 to < 80");
		System.out.println("D: 60 to < 70");
		System.out.println("F: < 60");
		System.out.println("___________________________________\n");
	}
	
	// Calculates a final grade based on all weighted scores from a student
	public double calculateFinalGrades(double[] rawScores, double[] weights) {
		double[] weightedScores = new double[categories.length];
		double sum = 0;
		double totalScore = 0;
		for (int i = 0; i < categories.length; i++) {
			weightedScores[i] = rawScores[i] * weights[i];
			sum += weightedScores[i];
			totalScore = sum;
		}
		return totalScore;		
	}

	public char determineLetterGrade(double finalPercentage) {
		char gradeLetter;
		if (finalPercentage >= 90)
		{
			gradeLetter = 'A';
		}
		else if (finalPercentage >= 80) 
		{
			gradeLetter = 'B';
		}
		else if (finalPercentage >= 70)
		{	
			gradeLetter = 'C';
		}
		else if (finalPercentage >= 60)
		{	
			gradeLetter = 'D';
		}	
		else
		{	
			gradeLetter = 'F';
		}
		return gradeLetter;
	}
	
	//Posts final grades for entire course
	public void postFinalGrades() {
		System.out.println("\n*****Final Grades for " + this.courseName + "******");
		
		finalGrades = new double[numStudents];
		finalLetterGrades = new char[numStudents];
		for (int i = 0; i < numStudents; i++) 
		{
				finalGrades[i] = calculateFinalGrades(students[i].getRawGrades(), categoryWeights);
				finalLetterGrades[i] = determineLetterGrade(finalGrades[i]);
				System.out.printf("Student " + (i + 1) + ": %.2f %c\n", finalGrades[i], finalLetterGrades[i]);
		}
		System.out.print("\n");
	}
	
	
	private double calculateClassAvg(double[] finalGrades) {
		double average;
		double sum = 0;
		for (int i = 0; i < finalGrades.length; i++) 
		{
			sum = sum + finalGrades[i];
		}
		average = sum / finalGrades.length;
		return average;
	} 
	
	private double getMinGrade(double[] classFinalGrades) {
		double min = classFinalGrades[0];
		for (int i = 0; i < classFinalGrades.length; i++) 
		{
			if (classFinalGrades[i] < min) 
			{
				min = classFinalGrades[i];
			}
		}
		return min;
	}
	
	private double getMaxGrade(double[] finalClassGrades) {
		double max = finalClassGrades[0];
		for (int i = 0; i < finalClassGrades.length; i++) 
		{
			if (finalClassGrades[i] > max) 
			{
				max = finalClassGrades[i];
			}
		}
		return max;		
	}	

	// writes all students in a class's grades to a file
	// writes class average, minimum, and maximum to a file
	public void writeSummaryToFile() {
		String fileName = getCourseName() + "_summary.txt";
		File gradeFile = new File(fileName);
		PrintWriter outputFile = null;
		
		try {
			outputFile = new PrintWriter(fileName);
			
			for (int i = 0; i < numStudents; i++) 
			{
				outputFile.println(students[i].getFirstName() + " " + students[i].getLastName() + ": " + finalGrades[i]);
			}
			
			outputFile.print("***** Class Grade Statistics*****");
			outputFile.printf("\nClass average: %.2f", calculateClassAvg(finalGrades));		
			outputFile.printf("\nClass min: %.2f", getMinGrade(finalGrades));
			outputFile.printf("\nClass max: %.2f", getMaxGrade(finalGrades));
			outputFile.println("\n___________________________________\n");
		
		
		System.out.println("File found at " + gradeFile.getAbsolutePath());
		
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not write to the file " + gradeFile.getName());
		} finally {
			if (outputFile != null) {
				outputFile.close();
			}
		}
	}
	
	// Adds student to course as long as there is room in course
	public void addStudent(Student studentToAdd) {
		if (numStudents < MAX_STUDENTS) {
			students[numStudents] = studentToAdd;
			this.numStudents++;
		}
		else 
		{
			System.out.print("Class is full. Could not add " + studentToAdd.getFirstName() + studentToAdd.getLastName() + " to " + courseName);
		}
	}
	
	public void addInstructor(Instructor instructorToAdd) {
		instructor = instructorToAdd;
	}

}// end of Course class

class Person {
	private String firstName;
	private String lastName;
	private String email;
	
	public Person(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;	
	}	
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
} // end of Person class

class Student extends Person {
	private double[] grades;
	private double finalGrade;
	
	public Student(Person person, double[] grades) {
		super(person.getFirstName(), person.getLastName(), person.getEmail());
		this.grades = grades;
	}
	
	public double[] getRawGrades() {
		return grades;
	}
	
}//end of Student class

class Instructor extends Person {
	
	public Instructor(Person person) {
		super(person.getFirstName(), person.getLastName(), person.getEmail());
	}
} //end of Instructor class



