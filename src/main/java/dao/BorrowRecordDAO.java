package dao;

import entity.BorrowRecord;
import java.util.Date;
import java.util.List;

public interface BorrowRecordDAO {
    boolean createBorrowRecord(BorrowRecord record);
    boolean updateReturnDate(int recordId, Date returnDate);
    BorrowRecord getBorrowRecordById(int id);
    List<BorrowRecord> getBorrowRecordsByReaderId(String readerId);
    List<BorrowRecord> getBorrowRecordsByBookISBN(String bookISBN);
    List<BorrowRecord> getCurrentBorrowRecords();
    List<BorrowRecord> getAllBorrowRecords();
}