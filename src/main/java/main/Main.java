package main;

import entity.Book;
import entity.BorrowRecord;
import entity.Reader;
import service.BookService;
import service.ReaderService;
import service.BorrowService;
import util.DataInitializer;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static BookService bookService = new BookService();
    private static ReaderService readerService = new ReaderService();
    private static BorrowService borrowService = new BorrowService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("欢迎使用小型图书借阅管理系统");
        
        // 检查是否需要初始化图书数据
        if (bookService.getAllBooks().isEmpty()) {
            System.out.println("检测到数据库为空，正在初始化图书数据...");
            DataInitializer.initializeBooks();
            System.out.println("图书数据初始化完成！\n");
        } else {
            System.out.println("检测到数据库中已有图书数据，跳过初始化。\n");
        }
        while (true) {
            showMainMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    handleBookManagement();
                    break;
                case 2:
                    handleReaderManagement();
                    break;
                case 3:
                    handleBorrowManagement();
                    break;
                case 4:
                    handleHistoryRecords();
                    break;
                case 0:
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n===== 主菜单 =====");
        System.out.println("1. 图书管理");
        System.out.println("2. 读者管理");
        System.out.println("3. 借阅管理");
        System.out.println("4. 历史记录查询");
        System.out.println("0. 退出系统");
        System.out.print("请输入选择: ");
    }

    private static int getChoice() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("输入错误！请输入正确的数字。");
            return -1;
        }
    }

    private static void handleBookManagement() {
        while (true) {
            System.out.println("\n===== 图书管理 =====");
            System.out.println("1. 录入图书信息");
            System.out.println("2. 修改图书信息");
            System.out.println("3. 删除图书信息");
            System.out.println("4. 查询图书信息");
            System.out.println("5. 查看所有图书");
            System.out.println("0. 返回主菜单");
            System.out.print("请输入选择: ");
            int choice = getChoice();
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    searchBooks();
                    break;
                case 5:
                    showAllBooks();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void addBook() {
        System.out.println("\n录入图书信息");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("书名: ");
        String title = scanner.nextLine();
        System.out.print("作者: ");
        String author = scanner.nextLine();
        System.out.print("出版社: ");
        String publisher = scanner.nextLine();
        
        double price = 0;
        while (true) {
            System.out.print("价格: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("输入错误！请输入正确的价格（数字）。");
            }
        }
        
        int totalQuantity = 0;
        while (true) {
            System.out.print("总数量: ");
            try {
                totalQuantity = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("输入错误！请输入正确的总数量（整数）。");
            }
        }
        
        int availableQuantity = 0;
        while (true) {
            System.out.print("可借数量: ");
            try {
                availableQuantity = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("输入错误！请输入正确的可借数量（整数）。");
            }
        }

        Book book = new Book(isbn, title, author, publisher, price, totalQuantity, availableQuantity, availableQuantity > 0);
        if (bookService.addBook(book)) {
            System.out.println("图书录入成功！");
        } else {
            System.out.println("图书录入失败！可能ISBN已存在或输入有误。");
        }
    }

    private static void updateBook() {
        System.out.println("\n修改图书信息");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        Book book = bookService.getBookByISBN(isbn);
        if (book == null) {
            System.out.println("找不到该图书！");
            return;
        }

        System.out.print("书名 (" + book.getTitle() + "): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            book.setTitle(title);
        }

        System.out.print("作者 (" + book.getAuthor() + "): ");
        String author = scanner.nextLine();
        if (!author.isEmpty()) {
            book.setAuthor(author);
        }

        System.out.print("出版社 (" + book.getPublisher() + "): ");
        String publisher = scanner.nextLine();
        if (!publisher.isEmpty()) {
            book.setPublisher(publisher);
        }

        System.out.print("价格 (" + book.getPrice() + "): ");
        String priceStr = scanner.nextLine();
        if (!priceStr.isEmpty()) {
            try {
                book.setPrice(Double.parseDouble(priceStr));
            } catch (NumberFormatException e) {
                System.out.println("输入错误！价格必须是数字，保持原值：" + book.getPrice());
            }
        }

        System.out.print("总数量 (" + book.getTotalQuantity() + "): ");
        String totalQuantityStr = scanner.nextLine();
        if (!totalQuantityStr.isEmpty()) {
            try {
                book.setTotalQuantity(Integer.parseInt(totalQuantityStr));
            } catch (NumberFormatException e) {
                System.out.println("输入错误！总数量必须是整数，保持原值：" + book.getTotalQuantity());
            }
        }

        System.out.print("可借数量 (" + book.getAvailableQuantity() + "): ");
        String availableQuantityStr = scanner.nextLine();
        if (!availableQuantityStr.isEmpty()) {
            try {
                book.setAvailableQuantity(Integer.parseInt(availableQuantityStr));
            } catch (NumberFormatException e) {
                System.out.println("输入错误！可借数量必须是整数，保持原值：" + book.getAvailableQuantity());
            }
        }

        if (bookService.updateBook(book)) {
            System.out.println("图书信息修改成功！");
        } else {
            System.out.println("图书信息修改失败！");
        }
    }

    private static void deleteBook() {
        System.out.println("\n删除图书信息");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        if (bookService.deleteBook(isbn)) {
            System.out.println("图书删除成功！");
        } else {
            System.out.println("图书删除失败！可能找不到该图书或有未归还的副本。");
        }
    }

    private static void searchBooks() {
        System.out.println("\n查询图书信息");
        System.out.println("1. 按ISBN查询");
        System.out.println("2. 按书名查询");
        System.out.println("3. 按作者查询");
        System.out.print("请输入选择: ");
        int choice = getChoice();
        List<Book> books;
        switch (choice) {
            case 1:
                System.out.print("ISBN: ");
                String isbn = scanner.nextLine();
                Book book = bookService.getBookByISBN(isbn);
                if (book != null) {
                    System.out.println(book);
                } else {
                    System.out.println("找不到该图书！");
                }
                return;
            case 2:
                System.out.print("书名: ");
                String title = scanner.nextLine();
                books = bookService.searchBooksByTitle(title);
                break;
            case 3:
                System.out.print("作者: ");
                String author = scanner.nextLine();
                books = bookService.searchBooksByAuthor(author);
                break;
            default:
                System.out.println("无效的选择！");
                return;
        }

        if (books.isEmpty()) {
            System.out.println("没有找到符合条件的图书！");
        } else {
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }

    private static void showAllBooks() {
        System.out.println("\n所有图书信息:");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("暂无图书信息！");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private static void handleReaderManagement() {
        while (true) {
            System.out.println("\n===== 读者管理 =====");
            System.out.println("1. 录入读者信息");
            System.out.println("2. 修改读者信息");
            System.out.println("3. 删除读者信息");
            System.out.println("4. 查询读者信息");
            System.out.println("5. 查看所有读者");
            System.out.println("0. 返回主菜单");
            System.out.print("请输入选择: ");
            int choice = getChoice();
            switch (choice) {
                case 1:
                    addReader();
                    break;
                case 2:
                    updateReader();
                    break;
                case 3:
                    deleteReader();
                    break;
                case 4:
                    searchReaders();
                    break;
                case 5:
                    showAllReaders();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void addReader() {
        System.out.println("\n录入读者信息");
        System.out.print("读者ID: ");
        String id = scanner.nextLine();
        System.out.print("姓名: ");
        String name = scanner.nextLine();
        System.out.println("读者类型:");
        System.out.println("1. 本科生");
        System.out.println("2. 研究生");
        System.out.println("3. 教师");
        System.out.print("请输入选择: ");
        int typeChoice = getChoice();

        boolean result;
        switch (typeChoice) {
            case 1:
            case 2:
                result = readerService.addStudentReader(id, name);
                break;
            case 3:
                result = readerService.addTeacherReader(id, name);
                break;
            default:
                System.out.println("无效的读者类型！");
                return;
        }

        if (result) {
            System.out.println("读者录入成功！");
        } else {
            System.out.println("读者录入失败！可能读者ID已存在或输入有误。");
        }
    }

    private static void updateReader() {
        System.out.println("\n修改读者信息");
        System.out.print("读者ID: ");
        String id = scanner.nextLine();
        Reader reader = readerService.getReaderById(id);
        if (reader == null) {
            System.out.println("找不到该读者！");
            return;
        }

        System.out.print("姓名 (" + reader.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            reader.setName(name);
        }

        if (readerService.updateReader(reader)) {
            System.out.println("读者信息修改成功！");
        } else {
            System.out.println("读者信息修改失败！");
        }
    }

    private static void deleteReader() {
        System.out.println("\n删除读者信息");
        System.out.print("读者ID: ");
        String id = scanner.nextLine();
        if (readerService.deleteReader(id)) {
            System.out.println("读者删除成功！");
        } else {
            System.out.println("读者删除失败！可能找不到该读者或有未归还的图书。");
        }
    }

    private static void searchReaders() {
        System.out.println("\n查询读者信息");
        System.out.println("1. 按ID查询");
        System.out.println("2. 按姓名查询");
        System.out.print("请输入选择: ");
        int choice = getChoice();
        List<Reader> readers;
        switch (choice) {
            case 1:
                System.out.print("读者ID: ");
                String id = scanner.nextLine();
                Reader reader = readerService.getReaderById(id);
                if (reader != null) {
                    System.out.println(reader);
                } else {
                    System.out.println("找不到该读者！");
                }
                return;
            case 2:
                System.out.print("姓名: ");
                String name = scanner.nextLine();
                readers = readerService.searchReadersByName(name);
                break;
            default:
                System.out.println("无效的选择！");
                return;
        }

        if (readers.isEmpty()) {
            System.out.println("没有找到符合条件的读者！");
        } else {
            for (Reader r : readers) {
                System.out.println(r);
            }
        }
    }

    private static void showAllReaders() {
        System.out.println("\n所有读者信息:");
        List<Reader> readers = readerService.getAllReaders();
        if (readers.isEmpty()) {
            System.out.println("暂无读者信息！");
        } else {
            for (Reader reader : readers) {
                System.out.println(reader);
            }
        }
    }

    private static void handleBorrowManagement() {
        while (true) {
            System.out.println("\n===== 借阅管理 =====");
            System.out.println("1. 图书借出");
            System.out.println("2. 图书归还");
            System.out.println("0. 返回主菜单");
            System.out.print("请输入选择: ");
            int choice = getChoice();
            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void borrowBook() {
        System.out.println("\n图书借出");
        System.out.print("读者ID: ");
        String readerId = scanner.nextLine();
        System.out.print("图书ISBN: ");
        String bookISBN = scanner.nextLine();

        if (borrowService.borrowBook(readerId, bookISBN)) {
            System.out.println("图书借出成功！");
        } else {
            System.out.println("图书借出失败！可能读者不存在、图书不存在、读者已达最大借阅数或图书已借出。");
        }
    }

    private static void returnBook() {
        System.out.println("\n图书归还");
        System.out.print("借阅记录ID: ");
        String recordIdStr = scanner.nextLine();
        
        try {
            int recordId = Integer.parseInt(recordIdStr);
            if (borrowService.returnBook(recordId)) {
                System.out.println("图书归还成功！");
            } else {
                System.out.println("图书归还失败！可能记录不存在或已归还。");
            }
        } catch (NumberFormatException e) {
            System.out.println("输入错误！请输入正确的借阅记录ID（数字）。");
        }
    }

    private static void handleHistoryRecords() {
        while (true) {
            System.out.println("\n===== 历史记录查询 =====");
            System.out.println("1. 查询所有借阅记录");
            System.out.println("2. 按读者ID查询借阅记录");
            System.out.println("3. 按图书ISBN查询借阅记录");
            System.out.println("4. 查询当前在借记录");
            System.out.println("0. 返回主菜单");
            System.out.print("请输入选择: ");
            int choice = getChoice();
            List<BorrowRecord> records;
            switch (choice) {
                case 1:
                    records = borrowService.getAllBorrowRecords();
                    break;
                case 2:
                    System.out.print("读者ID: ");
                    String readerId = scanner.nextLine();
                    records = borrowService.getBorrowRecordsByReaderId(readerId);
                    break;
                case 3:
                    System.out.print("图书ISBN: ");
                    String bookISBN = scanner.nextLine();
                    records = borrowService.getBorrowRecordsByBookISBN(bookISBN);
                    break;
                case 4:
                    records = borrowService.getCurrentBorrowRecords();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
                    continue;
            }

            if (records == null || records.isEmpty()) {
                System.out.println("没有找到符合条件的借阅记录！");
            } else {
                for (BorrowRecord record : records) {
                    System.out.println(record);
                }
            }
        }
    }
}