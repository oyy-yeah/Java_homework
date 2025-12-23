package util;

import entity.Book;
import service.BookService;

public class DataInitializer {
    private static BookService bookService = new BookService();
    
    public static void initializeBooks() {
        System.out.println("开始初始化图书数据...");
        
        // 图书信息数组
        String[][] booksData = {
            {"1", "三体", "刘慈欣", "重庆出版社", "23.8", "50", "50"},
            {"2", "白夜行", "东野圭吾", "南海出版公司", "39.5", "50", "50"},
            {"3", "人类简史：从动物到上帝", "尤瓦尔・赫拉利", "中信出版社", "45", "50", "50"},
            {"4", "活着", "余华", "作家出版社", "19", "50", "50"},
            {"5", "小王子", "安托万・德・圣-埃克苏佩里", "译林出版社", "22", "50", "50"},
            {"6", "百年孤独", "加西亚・马尔克斯", "南海出版公司", "36", "50", "50"},
            {"7", "明朝那些事儿", "当年明月", "北京联合出版公司", "28", "50", "50"},
            {"8", "解忧杂货店", "东野圭吾", "南海出版公司", "39.5", "50", "50"},
            {"9", "追风筝的人", "卡勒德・胡赛尼", "上海人民出版社", "36", "50", "50"},
            {"10", "你当像鸟飞往你的山", "塔拉・韦斯特弗", "南海出版公司", "45", "50", "50"}
        };
        
        int successCount = 0;
        
        for (String[] bookData : booksData) {
            String isbn = bookData[0];
            String title = bookData[1];
            String author = bookData[2];
            String publisher = bookData[3];
            double price = Double.parseDouble(bookData[4]);
            int totalQuantity = Integer.parseInt(bookData[5]);
            int availableQuantity = Integer.parseInt(bookData[6]);
            
            Book book = new Book(isbn, title, author, publisher, price, totalQuantity, availableQuantity, availableQuantity > 0);
            
            if (bookService.addBook(book)) {
                System.out.println("✓ 添加成功: " + title);
                successCount++;
            } else {
                System.out.println("✗ 添加失败: " + title + " (可能ISBN已存在)");
            }
        }
        
        System.out.println("图书数据初始化完成！成功添加 " + successCount + " 本图书。");
    }
    
    public static void main(String[] args) {
        initializeBooks();
    }
}