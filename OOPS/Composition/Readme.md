# Introduction
Composition is one of the fundamental principles of object-oriented programming (OOP). It allows objects to be built using other objects, promoting code reuse, flexibility, and better maintainability. Unlike inheritance, which establishes an "is-a" relationship, composition represents a "has-a" relationship.

# What is Composition?
Composition is a way to build complex objects by combining simpler ones. In Java, it means a class contains references to other objects as its fields, and those contained objects become part of the owning object's internal structure. The owner "has" those parts, and their lifecycles are often tied: if the owner is destroyed, its parts usually go away too. This is sometimes called a "part-of" relationship.

Composition is a design principle in OOP where one class contains an instance (or instances) of another class as a field. The contained class is often called a component, and the containing class is referred to as a composite class. This helps in building complex systems by combining simpler objects.

## Real-world Analogy
Think of a car. A car is composed of an engine, wheels, seats, and a dashboard. You don't say a car is an engine (that would be inheritance); you say a car has an engine, has wheels, etc. If the car is scrapped, normally its engine and other internal parts are not used separately unless removed deliberately—so the car owns those pieces in a tight way.

## Example: A Car and its Components
Consider a Car that consists of multiple components like an Engine, Wheel, and Transmission. Instead of inheriting from these components, a Car object will contain them as fields.

# When to Use Composition?
- When building complex objects that consist of multiple components.
- When you want to achieve code reusability without rigid inheritance hierarchies.
- When different behaviors need to be swapped dynamically (e.g., using different types of engines in a vehicle).
- When following the favor composition over inheritance principle. 