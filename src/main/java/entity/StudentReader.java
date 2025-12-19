package entity;

public class StudentReader extends Reader {
    public StudentReader() {
        super();
        setType("本科生");
        setMaxBorrowCount(5);
    }

    public StudentReader(String id, String name) {
        super(id, name, "本科生", 0, 5);
    }

    public StudentReader(String id, String name, int currentBorrowedCount) {
        super(id, name, "本科生", currentBorrowedCount, 5);
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