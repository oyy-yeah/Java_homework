import service.BookService;
import entity.Book;
import java.util.List;

public class TestSystemQuery {
    public static void main(String[] args) {
        BookService bookService = new BookService();
        
        System.out.println("=== 系统查询功能测试 ===\n");
        
        // 测试书名查询
        System.out.println("1. 书名查询测试:");
        testTitleQuery(bookService, "三体");
        testTitleQuery(bookService, "夜");
        testTitleQuery(bookService, "百年");
        testTitleQuery(bookService, "不存在的书");
        
        // 测试作者查询
        System.out.println("\n2. 作者查询测试:");
        testAuthorQuery(bookService, "刘慈欣");
        testAuthorQuery(bookService, "东野");
        testAuthorQuery(bookService, "马尔克斯");
        testAuthorQuery(bookService, "不存在的作者");
        
        // 测试部分匹配
        System.out.println("\n3. 部分匹配测试:");
        testTitleQuery(bookService, "体");
        testTitleQuery(bookService, "三");
        testAuthorQuery(bookService, "刘");
        testAuthorQuery(bookService, "欣");
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    private static void testTitleQuery(BookService bookService, String title) {
        System.out.printf("搜索书名: '%s' -> ", title);
        List<Book> books = bookService.searchBooksByTitle(title);
        if (books.isEmpty()) {
            System.out.println("没有找到匹配的图书");
        } else {
            System.out.printf("找到 %d 本图书:\n", books.size());
            for (Book book : books) {
                System.out.println("  " + book);
            }
        }
    }
    
    private static void testAuthorQuery(BookService bookService, String author) {
        System.out.printf("搜索作者: '%s' -> ", author);
        List<Book> books = bookService.searchBooksByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("没有找到匹配的图书");
        } else {
            System.out.printf("找到 %d 本图书:\n", books.size());
            for (Book book : books) {
                System.out.println("  " + book);
            }
        }
    }
}