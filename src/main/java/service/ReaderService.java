package service;

import dao.ReaderDAO;
import dao.impl.ReaderDAOImpl;
import entity.Reader;
import entity.StudentReader;
import entity.TeacherReader;
import java.util.List;

public class ReaderService {
    private ReaderDAO readerDAO = new ReaderDAOImpl();

    public boolean addReader(Reader reader) {
        if (reader == null || reader.getId() == null || reader.getId().isEmpty()) {
            return false;
        }
        if (readerDAO.getReaderById(reader.getId()) != null) {
            return false;
        }
        return readerDAO.addReader(reader);
    }

    public boolean addStudentReader(String id, String name) {
        if (id == null || id.isEmpty() || name == null || name.isEmpty()) {
            return false;
        }
        StudentReader studentReader = new StudentReader(id, name);
        return addReader(studentReader);
    }

    public boolean addTeacherReader(String id, String name) {
        if (id == null || id.isEmpty() || name == null || name.isEmpty()) {
            return false;
        }
        TeacherReader teacherReader = new TeacherReader(id, name);
        return addReader(teacherReader);
    }

    public boolean updateReader(Reader reader) {
        if (reader == null || reader.getId() == null || reader.getId().isEmpty()) {
            return false;
        }
        Reader existingReader = readerDAO.getReaderById(reader.getId());
        if (existingReader == null) {
            return false;
        }
        return readerDAO.updateReader(reader);
    }

    public boolean deleteReader(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        Reader reader = readerDAO.getReaderById(id);
        if (reader == null) {
            return false;
        }
        if (reader.getCurrentBorrowedCount() > 0) {
            return false;
        }
        return readerDAO.deleteReader(id);
    }

    public Reader getReaderById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return readerDAO.getReaderById(id);
    }

    public List<Reader> getAllReaders() {
        return readerDAO.getAllReaders();
    }

    public List<Reader> searchReadersByName(String name) {
        if (name == null || name.isEmpty()) {
            return getAllReaders();
        }
        return readerDAO.searchReadersByName(name);
    }

    public boolean updateReaderBorrowCount(String readerId, int delta) {
        if (readerId == null || readerId.isEmpty()) {
            return false;
        }
        Reader reader = readerDAO.getReaderById(readerId);
        if (reader == null) {
            return false;
        }
        int newCount = reader.getCurrentBorrowedCount() + delta;
        if (newCount < 0 || newCount > reader.getMaxBorrowCount()) {
            return false;
        }
        boolean result = readerDAO.updateReaderBorrowCount(readerId, delta);
        return result;
    }

    public boolean canBorrowMore(String readerId) {
        Reader reader = getReaderById(readerId);
        return reader != null && reader.canBorrowMore();
    }
}