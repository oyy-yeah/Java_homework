import service.ReaderService;
import entity.Reader;
import java.util.List;

public class ResetReaders {
    public static void main(String[] args) {
        ReaderService readerService = new ReaderService();
        
        System.out.println("=== 重置读者数据 ===\n");
        
        // 显示当前所有读者
        System.out.println("当前所有读者：");
        List<Reader> existingReaders = readerService.getAllReaders();
        if (existingReaders.isEmpty()) {
            System.out.println("数据库中没有读者记录");
        } else {
            for (Reader reader : existingReaders) {
                System.out.println(reader);
            }
        }
        
        System.out.println("\n删除现有读者...");
        for (Reader reader : existingReaders) {
            boolean result = readerService.deleteReader(reader.getId());
            if (result) {
                System.out.printf("  删除读者 %s 成功%n", reader.getId());
            } else {
                System.out.printf("  删除读者 %s 失败（可能还有借阅未还）%n", reader.getId());
            }
        }
        
        // 添加三个本科生读者
        System.out.println("\n添加三个本科生读者...");
        addStudentReader(readerService, "1", "张三");
        addStudentReader(readerService, "2", "李四");
        addStudentReader(readerService, "3", "王五");
        
        // 显示所有读者
        System.out.println("\n=== 当前所有读者 ===");
        List<Reader> readers = readerService.getAllReaders();
        if (readers.isEmpty()) {
            System.out.println("数据库中没有读者记录");
        } else {
            for (Reader reader : readers) {
                System.out.println(reader);
            }
        }
        
        System.out.println("\n=== 重置完成 ===");
    }
    
    private static void addStudentReader(ReaderService readerService, String id, String name) {
        System.out.printf("正在添加读者: ID=%s, 姓名=%s, 类型=本科生%n", id, name);
        boolean result = readerService.addStudentReader(id, name);
        if (result) {
            System.out.println("  添加成功！");
        } else {
            System.out.println("  添加失败！");
        }
    }
}