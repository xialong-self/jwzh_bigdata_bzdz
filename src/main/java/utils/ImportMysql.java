package utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import java.util.Properties;

/**
 * @author 夏龙
 * @date 2020-11-05
 */
public class ImportMysql {
    //输出mysql
    public static void saveTjjgToEsMYSQL(Dataset<Row> tjjg, String tablename) {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "Founder#123");
        connectionProperties.put("Driver", "com.mysql.jdbc.Driver");
        tjjg.write().mode(SaveMode.Overwrite).jdbc("jdbc:mysql://80.2.21.211:32006/bzdz_wtsjfx?characterEncoding=utf8",tablename, connectionProperties);

    }
}
