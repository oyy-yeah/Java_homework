import java.sql.*;

public class TestQuery {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db");
            
            // 首先检查数据库中有哪些书
            System.out.println("=== 数据库中的图书列表 ===");
            listAllBooks(conn);
            
            // 测试书名查询
            System.out.println("\n=== 测试书名查询 ===");
            testQuery(conn, "title", "三体");
            testQuery(conn, "title", "体");
            testQuery(conn, "title", "三");
            testQuery(conn, "title", "白夜行");
            testQuery(conn, "title", "夜");
            testQuery(conn, "title", "百年孤独");
            testQuery(conn, "title", "孤独");
            testQuery(conn, "title", "百年");
            
            // 测试作者查询
            System.out.println("\n=== 测试作者查询 ===");
            testQuery(conn, "author", "刘慈欣");
            testQuery(conn, "author", "刘");
            testQuery(conn, "author", "欣");
            testQuery(conn, "author", "东野圭吾");
            testQuery(conn, "author", "东野");
            testQuery(conn, "author", "加西亚·马尔克斯");
            testQuery(conn, "author", "马尔克斯");
            testQuery(conn, "author", "加西亚");
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void listAllBooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Books";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.printf("图书%d: %s (%s) - ISBN: %s, 出版社: %s, 价格: %.2f, 总数: %d, 可借: %d%n", 
                count, 
                rs.getString("title"), 
                rs.getString("author"), 
                rs.getString("ISBN"),
                rs.getString("publisher"),
                rs.getDouble("price"),
                rs.getInt("totalQuantity"),
                rs.getInt("availableQuantity"));
        }
        
        if (count == 0) {
            System.out.println("数据库中没有图书记录");
        } else {
            System.out.printf("共找到 %d 本图书%n", count);
        }
        
        rs.close();
        pstmt.close();
    }
    
    private static void testQuery(Connection conn, String field, String keyword) throws SQLException {
        String sql = "SELECT * FROM Books WHERE " + field + " LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + keyword + "%");
        
        ResultSet rs = pstmt.executeQuery();
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.printf("找到: %s (%s) - ISBN: %s%n", 
                rs.getString("title"), rs.getString("author"), rs.getString("ISBN"));
        }
        
        if (count == 0) {
            System.out.printf("没有找到包含 '%s' 的%s记录%n", keyword, field.equals("title") ? "书名" : "作者");
        } else {
            System.out.printf("共找到 %d 条记录%n", count);
        }
        
        rs.close();
        pstmt.close();
    }
}