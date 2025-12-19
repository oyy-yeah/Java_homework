package dao.impl;

import dao.BookDAO;
import entity.Book;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    @Override
    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO Books (ISBN, title, author, publisher, price, totalQuantity, availableQuantity, borrowable) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setInt(6, book.getTotalQuantity());
            pstmt.setInt(7, book.getAvailableQuantity());
            pstmt.setBoolean(8, book.isBorrowable());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Books SET title=?, author=?, publisher=?, price=?, totalQuantity=?, availableQuantity=?, borrowable=? WHERE ISBN=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setInt(5, book.getTotalQuantity());
            pstmt.setInt(6, book.getAvailableQuantity());
            pstmt.setBoolean(7, book.isBorrowable());
            pstmt.setString(8, book.getIsbn());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public boolean deleteBook(String isbn) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM Books WHERE ISBN=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    @Override
    public Book getBookByISBN(String isbn) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Books WHERE ISBN=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Books";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return books;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Books WHERE title LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + title + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return books;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Books WHERE author LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + author + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return books;
    }

    @Override
    public boolean updateBookAvailability(String isbn, int delta) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Books SET availableQuantity = availableQuantity + ?, borrowable = (availableQuantity + ?) > 0 WHERE ISBN = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, delta);
            pstmt.setInt(2, delta);
            pstmt.setString(3, isbn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setIsbn(rs.getString("ISBN"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPrice(rs.getDouble("price"));
        book.setTotalQuantity(rs.getInt("totalQuantity"));
        book.setAvailableQuantity(rs.getInt("availableQuantity"));
        book.setBorrowable(rs.getBoolean("borrowable"));
        return book;
    }
}