package dao;

import entity.Book;
import java.util.List;

public interface BookDAO {
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(String isbn);
    Book getBookByISBN(String isbn);
    List<Book> getAllBooks();
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    boolean updateBookAvailability(String isbn, int delta);
}