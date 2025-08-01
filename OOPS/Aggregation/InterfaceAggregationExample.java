package OOPS.Aggregation;

import java.util.ArrayList;
import java.util.List;

interface Teachable {
    void teach();
}

class Professor implements Teachable {
    private String name;
    private String subject;

    public Professor(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    @Override
    public void teach() {
        System.out.println(name + " is teaching " + subject);
    }
}

class University {
    private String universityName;
    private List<Teachable> professors;

    public University(String universityName) {
        this.universityName = universityName;
        this.professors = new ArrayList<>();
    }

    public void addProfessor(Teachable professor) {
        professors.add(professor);
    }

    public void showProfessors() {
        System.out.println("Professors at " + universityName + ":");
        for (Teachable professor : professors) {
            professor.teach();
        }
    }
}


public class InterfaceAggregationExample {
    public static void main(String[] args) {
        Professor prof1 = new Professor("Dr. Adams", "Physics");
        Professor prof2 = new Professor("Dr. Lee", "Chemistry");

        University university = new University("MIT");
        university.addProfessor(prof1);
        university.addProfessor(prof2);

        university.showProfessors();

    }
}

/*
COMPREHENSIVE EXPLANATION OF CODE CONCEPTS AND COMPILE-TIME VS RUNTIME DIFFERENCES:

1. ENCAPSULATION:
   - Professor class hides name and subject as private fields
   - University class encapsulates the list of professors
   - Data is protected from direct external access

2. INTERFACE-BASED POLYMORPHISM:
   - Teachable interface defines a contract (teach() method)
   - Both Professor and GuestLecturer implement this interface
   - University works with Teachable references, not concrete types

3. AGGREGATION:
   - University "has-a" relationship with Teachable objects
   - Professors can exist independently of University
   - If University is destroyed, Professors still exist

4. COMPILE-TIME VS RUNTIME DIFFERENCES:

   COMPILE-TIME:
   - Compiler sees List<Teachable> as a list of interface references
   - Method calls are validated against interface contract
   - Type checking ensures only Teachable objects are added
   - No knowledge of concrete implementations

   RUNTIME:
   - Actual objects in memory are Professor or GuestLecturer instances
   - Dynamic dispatch: JVM determines which teach() method to call
   - When professor.teach() is called:
     * Compile-time: Compiler sees Teachable.teach()
     * Runtime: JVM checks actual object type and calls Professor.teach() or GuestLecturer.teach()

5. POLYMORPHIC BEHAVIOR:
   - Same interface reference (Teachable) can point to different concrete objects
   - Method calls are resolved at runtime based on actual object type
   - Enables loose coupling and extensibility

6. EXTENSION WITHOUT MODIFICATION:
   - Adding GuestLecturer doesn't require changes to University class
   - Open/Closed Principle: Open for extension, closed for modification
   - New teachable types can be added without affecting existing code

7. TYPE SAFETY:
   - Compile-time type checking prevents invalid operations
   - Runtime type information enables correct method dispatch
   - Interface ensures all implementations provide required methods


   The internal professors list looks like:

graphql
Copy
Edit
professors --> [ Professor@1a2b3c (Dr. Adams, Physics), Professor@4d5e6f (Dr. Lee, Chemistry) ]
             ^-- references of type Teachable, actual objects are Professor

    - Each Professor object is stored in the list, but accessed through Teachable interface
    - This allows University to treat all professors uniformly while still allowing for specific behaviors defined in each implementation       
    Professor@1a2b3c is the default toString() output:

It’s ClassName@ followed by the object’s hashcode in hex.

It identifies the particular object instance, not its position in a list.

If you want to see the list with indexes, do something like:


for (int i = 0; i < professors.size(); i++) {
    Teachable t = professors.get(i);
    System.out.println(i + ": " + t); // requires meaningful toString()
}
And in Professor, override toString() so it’s readable:
@Override
public String toString() {
    return name + " (" + subject + ")";
}
Then output might be:

0: Dr. Adams (Physics)
1: Dr. Lee (Chemistry)
So:

Professor@1a2b3c is not an index, it’s the identity (hash) of the object.

To show positions, use the loop with the index as above.

*/