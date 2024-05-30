import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class StudentManagement {
    private static final String FILE_NAME = "student.json";

    // Read data from JSON file
    @SuppressWarnings("unchecked")
    private static JSONArray loadData() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            JSONParser jsonParser = new JSONParser();
            return (JSONArray) jsonParser.parse(reader);
        } catch (FileNotFoundException e) {
            return new JSONArray();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // Save data to JSON file
    private static void saveData(JSONArray data) {
        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(data.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display all students
    private static void displayStudents(JSONArray data) {
        System.out.println("List of students:");
        for (Object obj : data) {
            JSONObject student = (JSONObject) obj;
            System.out.println("ID: " + student.get("ID"));
            System.out.println("Name: " + student.get("Name"));

            // Check if subjects is null
            if (student.get("Subjects") != null) {
                JSONObject subjects = (JSONObject) student.get("Subjects");
                System.out.println("Subjects and scores:");
                for (Object key : subjects.keySet()) {
                    System.out.println("- " + key + ": " + subjects.get(key));
                }
            } else {
                System.out.println("No subjects registered.");
            }

            System.out.println();
        }
    }

    // Add new student
    @SuppressWarnings("unchecked")
    private static void addStudent(JSONArray data) {
        Scanner scanner = new Scanner(System.in);
        JSONObject newStudent = new JSONObject();
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        newStudent.put("ID", id);
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        newStudent.put("Name", name);
        JSONObject subjects = new JSONObject();
        System.out.print("Enter number of subjects: ");
        int numSubjects = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numSubjects; i++) {
            System.out.print("Enter subject name: ");
            String subject = scanner.nextLine();
            System.out.print("Enter score: ");
            String score = scanner.nextLine();
            subjects.put(subject, score);
        }
        newStudent.put("Subjects", subjects);
        data.add(newStudent);
        saveData(data);
        System.out.println("New student has been added to the list.");
    }

    // Edit student information
    private static void editStudent(JSONArray data) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID of student to edit: ");
        String studentId = scanner.nextLine();
        for (Object obj : data) {
            JSONObject student = (JSONObject) obj;
            if (studentId.equals(student.get("ID"))) {
                System.out.println("1. Edit name");
                System.out.println("2. Add subjects and scores");
                System.out.println("3. Remove subjects");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        student.put("Name", newName);
                        break;
                    case 2:
                        JSONObject subjects = (JSONObject) student.get("Subjects");
                        System.out.print("Enter number of subjects to add: ");
                        int numSubjects = Integer.parseInt(scanner.nextLine());
                        for (int i = 0; i < numSubjects; i++) {
                            System.out.print("Enter subject name: ");
                            String subject = scanner.nextLine();
                            System.out.print("Enter score: ");
                            String score = scanner.nextLine();
                            subjects.put(subject, score);
                        }
                        student.put("Subjects", subjects);
                        break;
                    case 3:
                        JSONObject subjectsToRemove = (JSONObject) student.get("Subjects");
                        System.out.print("Enter subject name to remove: ");
                        String subjectToRemove = scanner.nextLine();
                        subjectsToRemove.remove(subjectToRemove);
                        student.put("Subjects", subjectsToRemove);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                saveData(data);
                System.out.println("Student information has been updated.");
                return;
            }
        }
        System.out.println("Student with this ID not found.");
    }

    // Search for a student by name or ID
    private static void searchStudent(JSONArray data) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student name or ID: ");
        String searchTerm = scanner.nextLine();
        boolean found = false;
        for (Object obj : data) {
            JSONObject student = (JSONObject) obj;
            String id = (String) student.get("ID");
            String name = (String) student.get("Name");
            if (name.toLowerCase().contains(searchTerm.toLowerCase()) || id.equals(searchTerm)) {
                System.out.println("Student information:");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                JSONObject subjects = (JSONObject) student.get("Subjects");
                System.out.println("Subjects and scores:");
                for (Object key : subjects.keySet()) {
                    System.out.println("- " + key + ": " + subjects.get(key));
                }
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Student not found.");
        }
    }

    public static void main(String[] args) {
        JSONArray data = loadData();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Display all students");
            System.out.println("2. Add a student");
            System.out.println("3. Edit student information");
            System.out.println("4. Search for a student");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    displayStudents(data);
                    break;
                case 2:
                    addStudent(data);
                    break;
                case 3:
                    editStudent(data);
                    break;
                case 4:
                    searchStudent(data);
                    break;
                case 5:
                    System.out.println("Exiting the program.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
