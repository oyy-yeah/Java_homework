package service;

import dao.BorrowRecordDAO;
import dao.impl.BorrowRecordDAOImpl;
import entity.Book;
import entity.Reader;
import entity.BorrowRecord;
import util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BorrowService {
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAOImpl();
    private BookService bookService = new BookService();
    private ReaderService readerService = new ReaderService();

    public boolean borrowBook(String readerId, String bookISBN) {
        if (readerId == null || readerId.isEmpty() || bookISBN == null || bookISBN.isEmpty()) {
            return false;
        }

        Reader reader = readerService.getReaderById(readerId);
        Book book = bookService.getBookByISBN(bookISBN);

        if (reader == null || book == null) {
            return false;
        }

        if (!reader.canBorrowMore()) {
            return false;
        }

        if (!book.isBorrowable()) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Date borrowDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrowDate);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date dueDate = calendar.getTime();

            BorrowRecord record = new BorrowRecord();
            record.setReader(reader);
            record.setBook(book);
            record.setBorrowDate(borrowDate);
            record.setDueDate(dueDate);
            record.setReturnDate(null);
            record.setStatus("在借");

            boolean recordResult = borrowRecordDAO.createBorrowRecord(record);
            boolean bookResult = bookService.updateBookAvailability(bookISBN, -1);
            boolean readerResult = readerService.updateReaderBorrowCount(readerId, 1);

            if (recordResult && bookResult && readerResult) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean returnBook(int recordId) {
        BorrowRecord record = borrowRecordDAO.getBorrowRecordById(recordId);
        if (record == null) {
            return false;
        }

        if (record.getReturnDate() != null) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Date returnDate = new Date();
            boolean recordResult = borrowRecordDAO.updateReturnDate(recordId, returnDate);
            boolean bookResult = bookService.updateBookAvailability(record.getBook().getIsbn(), 1);
            boolean readerResult = readerService.updateReaderBorrowCount(record.getReader().getId(), -1);

            if (recordResult && bookResult && readerResult) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public BorrowRecord getBorrowRecordById(int id) {
        return borrowRecordDAO.getBorrowRecordById(id);
    }

    public List<BorrowRecord> getBorrowRecordsByReaderId(String readerId) {
        if (readerId == null || readerId.isEmpty()) {
            return null;
        }
        return borrowRecordDAO.getBorrowRecordsByReaderId(readerId);
    }

    public List<BorrowRecord> getBorrowRecordsByBookISBN(String bookISBN) {
        if (bookISBN == null || bookISBN.isEmpty()) {
            return null;
        }
        return borrowRecordDAO.getBorrowRecordsByBookISBN(bookISBN);
    }

    public List<BorrowRecord> getCurrentBorrowRecords() {
        return borrowRecordDAO.getCurrentBorrowRecords();
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordDAO.getAllBorrowRecords();
    }

    public void updateAllBorrowStatus() {
        List<BorrowRecord> records = borrowRecordDAO.getAllBorrowRecords();
        for (BorrowRecord record : records) {
            record.updateStatus();
        }
    }
}