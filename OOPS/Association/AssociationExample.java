package OOPS.Association;
import java.util.*;

class Student{
    private String name;
    Student(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
}
class Teacher{
    private String name;
    List<Student> students;

    Teacher(String name)
    {
        this.name=name;
        this.students=new ArrayList<>();
    }
    public void addStudent(Student student)
    {
        students.add(student);
    }
    public void showStudents()
    {
        System.out.println("Class teacher:"+name);
        for(Student student:students)
        {
          
            System.out.println(" - " + student.getName());
        }
    }
}

public class AssociationExample {
    public static void main(String[] args) {
        Teacher teacher1 = new Teacher("Mr. Smith");
        Teacher teacher2 = new Teacher("Mrs. Johnson");
        
        Student student1 = new Student("Alice");
        Student student2 = new Student("Bob");
        
        teacher1.addStudent(student1);
        teacher1.addStudent(student2);
        teacher2.addStudent(student2);
        
        teacher1.showStudents();
        teacher2.showStudents();
    }
}
