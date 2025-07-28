# Method Overriding in Inheritance
Method overriding allows a child class to redefine a method from the parent class.

- Occurs when a subclass provides its own implementation of a method that is already defined in its superclass.

- The overriding method must have the same name, same return type, and same parameter list as the method in the superclass.

- Enables runtime polymorphism: when you call the method on a superclass reference pointing to a subclass object, the JVM dispatches to the subclass’s version.

# Super

A special reference in a subclass, used to access members of its immediate superclass.

super()

Invokes the superclass’s constructor. Must be the first statement in a subclass constructor if used.

super.methodName(...)

Calls the superclass’s version of an overridden method.

super.fieldName

Refers to a hidden field in the superclass when the subclass has declared a field with the same name.