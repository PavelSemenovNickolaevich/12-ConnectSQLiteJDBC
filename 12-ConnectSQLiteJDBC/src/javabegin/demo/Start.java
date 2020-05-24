package javabegin.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Start {

    public static void main(String[] args) {
        try {
 
            Scanner scanner = new Scanner(new FileInputStream("sql.txt"), "UTF-8");

            String dbPath = scanner.nextLine();

            System.out.println("База данных: " + dbPath);

            if (!new File(dbPath).exists()) {
                System.out.println("Файл базы данных не найден!");
                return;
            }
            
            DBUtils.openConnection(dbPath); // открыть соединение перед всеми операциями

            DBUtils.showPrepStatement();

            StringBuilder builder = new StringBuilder();
            
            try {
                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine());
                }
            } finally {
                scanner.close();
            }

            String[] sql = builder.toString().split(";");



            StringBuilder resultBuilder = new StringBuilder();

            for (String sqlStr : sql) {
                resultBuilder.append("Запрос: ").append(sqlStr).append("\n");
                resultBuilder.append("Результат:" + "\n");
                
                ArrayList<SprObject> list = DBUtils.getResultList(sqlStr);
                
                for (SprObject obj : list) {
                    resultBuilder.append(obj.getId()).append(",").append(obj.getName_ru()).append(",").append(obj.getName_en()).append("\n");
                }
                resultBuilder.append("\n");
            }

            System.out.println(resultBuilder.toString());

            writeTextToFile(resultBuilder.toString());

            DBUtils.closeConnection();// после всех операций - закрыть соединение

        } catch (IOException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // запись текстовых данных в файл
    private static void writeTextToFile(String str) {
        try {
            
            // все объявленные ресурсы в скобках try будут закрываться автоматически после выполнения блока
            try (Writer fw = new FileWriter("result.txt")) {
                fw.write(str);
                fw.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
