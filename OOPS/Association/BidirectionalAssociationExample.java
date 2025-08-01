package OOPS.Association;
import java.util.*;

class Book {
    private String title;
    private Library library;
    
    public Book(String title, Library library) {
        this.title = title;
        this.library = library;
    }
    
    public void showLibrary() {
        System.out.println(title + " is in " + library.getName());
    }
    
    public String getTitle() {
        return title;
    }
}

class Library {
    private String name;
    private List<Book> books;
    
    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }
    
    public void addBook(Book book) {
        books.add(book);
    }
    
    public String getName() {
        return name;
    }
    
    public void showBooks() {
        System.out.println("Books in " + name + ":");
        for (Book book : books) {
            System.out.println(" - " + book.getTitle());
        }
    }
}

public class BidirectionalAssociationExample  {
    public static void main(String[] args) {
        Library library = new Library("City Library");
        Book book1 = new Book("1984", library);
        Book book2 = new Book("Brave New World", library);
        
        library.addBook(book1);
        library.addBook(book2);
        
        library.showBooks();
        book1.showLibrary();
        book2.showLibrary();
    }
}
/**
 * 
 * We’re using association here because we need to model a real-world relationship where two independent entities know about each other and can interact: a library has books, and each book knows which library it belongs to. 
 * That’s exactly what association is—one object holds a reference to another so it can use or query it. In this case it’s bidirectional association because you can go from library to its books (library.showBooks()) and from a book back to its library (book1.showLibrary()).

The reason to structure it this way is practical: it gives you two-way navigability 
without implying ownership in the strong sense (like composition) or an “is-a” relationship (like inheritance). 
You can list all books in a library, and you can ask any book where it came from. 
That reflects the real domain and makes code that needs either direction convenient. 
The trade-off is you must keep both sides consistent (e.g., when you create a book and add it to a library, you manually update both objects), so real implementations often encapsulate that synchronization in helper methods or factory logic to avoid mismatches.
 */