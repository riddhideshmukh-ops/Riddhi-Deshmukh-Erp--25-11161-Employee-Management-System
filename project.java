import java.io.*;
import java.util.*;

class Employee {
    String id, name, department;
    double salary;

    Employee(String id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String toString() {
        String sal = (salary == (long) salary) ?
                String.valueOf((long) salary) : String.valueOf(salary);
        return id + " | " + name + " | " + department + " | " + sal;
    }
}

class Node {
    Employee data;
    Node next;

    Node(Employee data) {
        this.data = data;
        next = null;
    }
}

class EmployeeList {
    Node head, tail;
    int size = 0, nextId = 101;

    HashMap<String, Employee> map = new HashMap<>();

    // Add employee to linked list
    void addNode(Employee emp) {
        Node newNode = new Node(emp);

        if (head == null)
            head = tail = newNode;
        else {
            tail.next = newNode;
            tail = newNode;
        }

        map.put(emp.id, emp);
        size++;
    }

    // Add employee
    void addEmployee(String name, String dept, double salary) {
        String id = "E" + nextId++;
        Employee emp = new Employee(id, name, dept, salary);

        addNode(emp);
        System.out.println("Employee added. ID: " + emp);
    }

    // Display all employees
    void displayAll() {
        if (head == null) {
            System.out.println("No employees found.");
            return;
        }

        Node temp = head;

        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }

        System.out.println("Total employees: " + size);
    }

    // Search by ID
    void searchById(String id) {
        Employee emp = map.get(id);

        if (emp == null)
            System.out.println("Employee " + id + " not found.");
        else
            System.out.println(emp);
    }

    // Search by name
    void searchByName(String name) {
        Node temp = head;
        boolean found = false;

        while (temp != null) {
            if (temp.data.name.toLowerCase().contains(name.toLowerCase())) {
                System.out.println(temp.data);
                found = true;
            }
            temp = temp.next;
        }

        if (!found)
            System.out.println("Employee " + name + " not found.");
    }

    // Delete employee
    void deleteEmployee(String id) {
        Node temp = head, prev = null;

        while (temp != null && !temp.data.id.equals(id)) {
            prev = temp;
            temp = temp.next;
        }

        if (temp == null) {
            System.out.println("Employee " + id + " not found.");
            return;
        }

        if (prev == null)
            head = temp.next;
        else
            prev.next = temp.next;

        if (temp == tail)
            tail = prev;

        map.remove(id);
        size--;

        System.out.println("Employee " + id + " deleted.");
        System.out.println("Total employees: " + size);
    }

    // Sort salary from high to low
    void sortBySalary() {
        if (head == null) {
            System.out.println("No employees to sort.");
            return;
        }

        Employee arr[] = new Employee[size];
        Node temp = head;
        int i = 0;

        while (temp != null) {
            arr[i++] = temp.data;
            temp = temp.next;
        }

        // Insertion sort
        for (int j = 1; j < size; j++) {
            Employee key = arr[j];
            int k = j - 1;

            while (k >= 0 && arr[k].salary < key.salary) {
                arr[k + 1] = arr[k];
                k--;
            }

            arr[k + 1] = key;
        }

        for (int j = 0; j < size; j++) {
            System.out.println((j + 1) + ". " + arr[j]);
        }
    }

    // Load data from file
    void loadData() {
        try (BufferedReader br = new BufferedReader(
                new FileReader("employees.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {
                try {
                    String data[] = line.split(",", -1);

                    if (data.length != 4)
                        continue;

                    String id = data[0].trim();
                    String name = data[1].trim();
                    String dept = data[2].trim();
                    double salary = Double.parseDouble(data[3].trim());

                    if (!id.matches("E[0-9]+") || name.isEmpty() ||
                        dept.isEmpty() || salary <= 0 ||
                        Double.isInfinite(salary) || Double.isNaN(salary) ||
                        map.containsKey(id))
                        continue;

                    int num = Integer.parseInt(id.substring(1));

                    Employee emp = new Employee(id, name, dept, salary);
                    addNode(emp);

                    if (num >= nextId)
                        nextId = num + 1;

                } catch (Exception e) {
                    continue;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found.");
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }

    // Save data to file
    void saveData() {
        try (PrintWriter pw = new PrintWriter(
                new FileWriter("employees.txt"))) {

            Node temp = head;

            while (temp != null) {
                Employee emp = temp.data;

                pw.println(emp.id + "," + emp.name + "," +
                        emp.department + "," + emp.salary);

                temp = temp.next;
            }

            System.out.println("Data saved successfully.");

        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }
}

public class project {
    static Scanner sc = new Scanner(System.in);
    static EmployeeList list = new EmployeeList();

    // Add employee
    static void addEmployee() {
        System.out.print("Name: ");
        String name = sc.nextLine().trim().replace(",", " ");

        System.out.print("Department: ");
        String dept = sc.nextLine().trim().replace(",", " ");

        if (name.isEmpty() || dept.isEmpty()) {
            System.out.println("Name and department cannot be empty.");
            return;
        }

        System.out.print("Salary: ");

        try {
            double salary = Double.parseDouble(sc.nextLine().trim());

            if (salary <= 0 || Double.isInfinite(salary) ||
                    Double.isNaN(salary)) {
                System.out.println("Salary must be greater than zero.");
                return;
            }

            list.addEmployee(name, dept, salary);

        } catch (NumberFormatException e) {
            System.out.println("Invalid salary.");
        }
    }

    // Search employee
    static void searchEmployee() {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Enter choice: ");

        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Employee ID: ");
            list.searchById(sc.nextLine().trim().toUpperCase());

        } else if (choice.equals("2")) {
            System.out.print("Employee Name: ");
            String name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Enter a valid name.");
                return;
            }

            list.searchByName(name);

        } else {
            System.out.println("Invalid choice.");
        }
    }

    // Delete employee
    static void deleteEmployee() {
        System.out.print("Employee ID: ");
        list.deleteEmployee(sc.nextLine().trim().toUpperCase());
    }

    public static void main(String[] args) {
        list.loadData();

        while (true) {
            System.out.println("\n===== EMPLOYEE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Sort by Salary (High to Low)");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addEmployee();
                    break;

                case "2":
                    list.displayAll();
                    break;

                case "3":
                    searchEmployee();
                    break;

                case "4":
                    deleteEmployee();
                    break;

                case "5":
                    list.sortBySalary();
                    break;

                case "6":
                    list.saveData();
                    System.out.println("Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}