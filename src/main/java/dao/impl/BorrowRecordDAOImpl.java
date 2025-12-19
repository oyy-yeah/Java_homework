package dao.impl;

import dao.BorrowRecordDAO;
import dao.BookDAO;
import dao.ReaderDAO;
import entity.BorrowRecord;
import util.DBUtil;
import entity.Book;
import entity.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowRecordDAOImpl implements BorrowRecordDAO {
    private BookDAO bookDAO = new BookDAOImpl();
    private ReaderDAO readerDAO = new ReaderDAOImpl();

    @Override
    public boolean createBorrowRecord(BorrowRecord record) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO BorrowRecords (readerId, bookISBN, borrowDate, dueDate, returnDate, status) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, record.getReader().getId());
            pstmt.setString(2, record.getBook().getIsbn());
            pstmt.setDate(3, new java.sql.Date(record.getBorrowDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(record.getDueDate().getTime()));
            if (record.getReturnDate() != null) {
                pstmt.setDate(5, new java.sql.Date(record.getReturnDate().getTime()));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            pstmt.setString(6, record.getStatus());
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
    public boolean updateReturnDate(int recordId, Date returnDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE BorrowRecords SET returnDate = ?, status = '已还' WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(returnDate.getTime()));
            pstmt.setInt(2, recordId);
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
    public BorrowRecord getBorrowRecordById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM BorrowRecords WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBorrowRecord(rs);
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
    public List<BorrowRecord> getBorrowRecordsByReaderId(String readerId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM BorrowRecords WHERE readerId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, readerId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return records;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByBookISBN(String bookISBN) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM BorrowRecords WHERE bookISBN = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookISBN);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return records;
    }

    @Override
    public List<BorrowRecord> getCurrentBorrowRecords() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM BorrowRecords WHERE status IN ('在借', '超期')";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return records;
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM BorrowRecords";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return records;
    }

    private BorrowRecord mapResultSetToBorrowRecord(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setId(rs.getInt("id"));
        
        String readerId = rs.getString("readerId");
        Reader reader = readerDAO.getReaderById(readerId);
        record.setReader(reader);
        
        String bookISBN = rs.getString("bookISBN");
        Book book = bookDAO.getBookByISBN(bookISBN);
        record.setBook(book);
        
        record.setBorrowDate(rs.getDate("borrowDate"));
        record.setDueDate(rs.getDate("dueDate"));
        record.setReturnDate(rs.getDate("returnDate"));
        record.setStatus(rs.getString("status"));
        
        return record;
    }
}