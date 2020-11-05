package utils;

import org.apache.spark.sql.SparkSession;

public class SparkConnection {
	public static SparkSession SparkConnectionx() {
		SparkSession sparkSession = SparkSession.builder()
		.appName("Bzdzstsjfx")
//		.master("local")
		.enableHiveSupport()
		.config("spark.driver.host","80.2.36.26")
		.config("spark.yarn.jars","hdfs://80.2.36.26:8020/spark/cdhjars/*")
		.config("spark.yarn.queue", "hive")
		.config("spark.table.chech.partition",true)
		.config("spark.table.chech.partition.num",30)
		.config("spark.dynamicAllocation.enabled", true)
		.config("spark.shuffle.service.enabled", true)
		.getOrCreate();
		return sparkSession;
	}
	
	
	

}
