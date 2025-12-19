package entity;

import java.util.Date;

public class BorrowRecord {
    private int id;
    private Reader reader;
    private Book book;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status;

    public BorrowRecord() {
    }

    public BorrowRecord(int id, Reader reader, Book book, Date borrowDate, Date dueDate, Date returnDate, String status) {
        this.id = id;
        this.reader = reader;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void updateStatus() {
        if (returnDate == null) {
            Date now = new Date();
            if (now.after(dueDate)) {
                status = "超期";
            } else {
                status = "在借";
            }
        } else {
            status = "已还";
        }
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", reader=" + reader.getName() +
                ", book=" + book.getTitle() +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                '}';
    }
}