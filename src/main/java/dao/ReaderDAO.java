package dao;

import entity.Reader;
import java.util.List;

public interface ReaderDAO {
    boolean addReader(Reader reader);
    boolean updateReader(Reader reader);
    boolean deleteReader(String id);
    Reader getReaderById(String id);
    List<Reader> getAllReaders();
    List<Reader> searchReadersByName(String name);
    boolean updateReaderBorrowCount(String readerId, int delta);
}