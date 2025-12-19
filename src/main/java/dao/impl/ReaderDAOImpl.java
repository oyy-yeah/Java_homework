package dao.impl;

import dao.ReaderDAO;
import entity.Reader;
import entity.StudentReader;
import entity.TeacherReader;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAOImpl implements ReaderDAO {
    @Override
    public boolean addReader(Reader reader) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO Readers (id, name, type, currentBorrowedCount, maxBorrowCount) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reader.getId());
            pstmt.setString(2, reader.getName());
            pstmt.setString(3, reader.getType());
            pstmt.setInt(4, reader.getCurrentBorrowedCount());
            pstmt.setInt(5, reader.getMaxBorrowCount());
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
    public boolean updateReader(Reader reader) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Readers SET name=?, type=?, currentBorrowedCount=?, maxBorrowCount=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reader.getName());
            pstmt.setString(2, reader.getType());
            pstmt.setInt(3, reader.getCurrentBorrowedCount());
            pstmt.setInt(4, reader.getMaxBorrowCount());
            pstmt.setString(5, reader.getId());
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
    public boolean deleteReader(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM Readers WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
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
    public Reader getReaderById(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Readers WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReader(rs);
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
    public List<Reader> getAllReaders() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reader> readers = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Readers";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                readers.add(mapResultSetToReader(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return readers;
    }

    @Override
    public List<Reader> searchReadersByName(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reader> readers = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Readers WHERE name LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                readers.add(mapResultSetToReader(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
        return readers;
    }

    @Override
    public boolean updateReaderBorrowCount(String readerId, int delta) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Readers SET currentBorrowedCount = currentBorrowedCount + ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, delta);
            pstmt.setString(2, readerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeStatement(pstmt);
            DBUtil.closeConnection(conn);
        }
    }

    private Reader mapResultSetToReader(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String type = rs.getString("type");
        int currentBorrowedCount = rs.getInt("currentBorrowedCount");
        int maxBorrowCount = rs.getInt("maxBorrowCount");

        if ("本科生".equals(type) || "研究生".equals(type)) {
            return new StudentReader(id, name, currentBorrowedCount);
        } else if ("教师".equals(type)) {
            return new TeacherReader(id, name, currentBorrowedCount);
        } else {
            return new StudentReader(id, name, currentBorrowedCount);
        }
    }
}