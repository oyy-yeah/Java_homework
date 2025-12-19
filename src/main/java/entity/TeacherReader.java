package entity;

public class TeacherReader extends Reader {
    public TeacherReader() {
        super();
        setType("教师");
        setMaxBorrowCount(10);
    }

    public TeacherReader(String id, String name) {
        super(id, name, "教师", 0, 10);
    }

    public TeacherReader(String id, String name, int currentBorrowedCount) {
        super(id, name, "教师", currentBorrowedCount, 10);
    }

    @Override
    public boolean borrowBook() {
        if (canBorrowMore()) {
            setCurrentBorrowedCount(getCurrentBorrowedCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnBook() {
        if (getCurrentBorrowedCount() > 0) {
            setCurrentBorrowedCount(getCurrentBorrowedCount() - 1);
            return true;
        }
        return false;
    }
}