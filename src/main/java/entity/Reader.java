package entity;

public abstract class Reader extends Person {
    private String type;
    private int currentBorrowedCount;
    private int maxBorrowCount;

    public Reader() {
    }

    public Reader(String id, String name, String type, int currentBorrowedCount, int maxBorrowCount) {
        super(id, name);
        this.type = type;
        this.currentBorrowedCount = currentBorrowedCount;
        this.maxBorrowCount = maxBorrowCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrentBorrowedCount() {
        return currentBorrowedCount;
    }

    public void setCurrentBorrowedCount(int currentBorrowedCount) {
        this.currentBorrowedCount = currentBorrowedCount;
    }

    public int getMaxBorrowCount() {
        return maxBorrowCount;
    }

    public void setMaxBorrowCount(int maxBorrowCount) {
        this.maxBorrowCount = maxBorrowCount;
    }

    public boolean canBorrowMore() {
        return currentBorrowedCount < maxBorrowCount;
    }

    @Override
    public String toString() {
        return "读者信息：" +
                "\n  读者ID：" + getId() +
                "\n  姓名：" + getName() +
                "\n  类型：" + type +
                "\n  当前借阅数量：" + currentBorrowedCount +
                "\n  最大借阅数量：" + maxBorrowCount +
                "\n";
    }
}