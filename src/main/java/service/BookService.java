package service;

import dao.BookDAO;
import dao.impl.BookDAOImpl;
import entity.Book;
import java.util.List;

public class BookService {
    private BookDAO bookDAO = new BookDAOImpl();

    public boolean addBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getIsbn().isEmpty()) {
            return false;
        }
        if (bookDAO.getBookByISBN(book.getIsbn()) != null) {
            return false;
        }
        book.updateStatus();
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getIsbn().isEmpty()) {
            return false;
        }
        Book existingBook = bookDAO.getBookByISBN(book.getIsbn());
        if (existingBook == null) {
            return false;
        }
        book.updateStatus();
        return bookDAO.updateBook(book);
    }

    public boolean deleteBook(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        Book book = bookDAO.getBookByISBN(isbn);
        if (book == null) {
            return false;
        }
        if (book.getAvailableQuantity() < book.getTotalQuantity()) {
            return false;
        }
        return bookDAO.deleteBook(isbn);
    }

    public Book getBookByISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return null;
        }
        return bookDAO.getBookByISBN(isbn);
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooksByTitle(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooksByAuthor(author);
    }

    public boolean updateBookAvailability(String isbn, int delta) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        Book book = bookDAO.getBookByISBN(isbn);
        if (book == null) {
            return false;
        }
        int newAvailability = book.getAvailableQuantity() + delta;
        if (newAvailability < 0 || newAvailability > book.getTotalQuantity()) {
            return false;
        }
        boolean result = bookDAO.updateBookAvailability(isbn, delta);
        if (result) {
            Book updatedBook = bookDAO.getBookByISBN(isbn);
            updatedBook.updateStatus();
            bookDAO.updateBook(updatedBook);
        }
        return result;
    }
}