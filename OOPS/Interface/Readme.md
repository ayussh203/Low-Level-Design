# Interface

An interface is a crucial concept that defines a contract for classes to follow. It allows multiple classes to share a common structure while enforcing certain behaviors. Interfaces are widely used in Java and other OOP languages to achieve abstraction, polymorphism, and loose coupling.

An interface in Java is a collection of abstract methods (methods without implementation) that a class can implement. It defines a contract that the implementing classes must adhere to:

- It does not contain a constructor.
- It cannot be instantiated.
- All the fields in interfaces are public, static, and final by default.
- All methods are public and abstract by default.
- A class that implements an interface must implement all the methods declared in the interface.
- Interfaces support the functionality of multiple inheritance.

Abstraction
What it is: Hiding implementation details and exposing only the necessary operations.

How interfaces do it:

An interface declares what methods a class must provide, without saying how they work.

Clients code against the interface, not the concrete class, so they never see the inner workings.

Polymorphism
What it is: The ability to treat different types through a common interface.

How interfaces do it:

Any class implementing the interface can be passed to client code interchangeably.

At runtime, the JVM dispatches the call to the appropriate implementation.

Loose Coupling
What it is: Reducing dependencies between components so they can evolve independently.

How interfaces do it:

Client classes depend only on the interface, not on concrete implementations.

You can swap, extend or mock implementations without changing client code.
Swap implementations
You can choose at runtime whether you’re using PayPalProcessor, StripeProcessor, or any other real processor—just by passing a different object into CheckoutService. You don’t have to touch the CheckoutService class itself to switch from PayPal to Stripe.

Extend implementations
Later on you might add a completely new payment method, say CryptoProcessor, by writing a new class that implements PaymentProcessor. Again, you never edit CheckoutService; you only create your new CryptoProcessor and hand it in.

Mock implementations
For testing, you write a MockProcessor that also implements PaymentProcessor but fakes or records the work instead of calling a real API. Inject that into CheckoutService when running your unit tests—no production code needs to change.

Any method that takes an interface type parameter is acting as a client:

In all of these, the client cares only about “I have something that can process(...)” or “I have something that can log(...)”—it doesn’t need to know whether that something is PayPal, Stripe, Console, File, etc. That separation is what makes your code abstract, polymorphic and loosely coupled.