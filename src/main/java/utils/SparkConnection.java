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
		.config("spark.dynamicAllocation.enabled", true)
		.config("spark.shuffle.service.enabled", true)
		.config("spark.cores.max",56)//spark程序需要的总核数
		.config("spark.sql.broadcastTimeout","600s")//广播join时的超时时间
		.config("spark.dynamicAllocation.maxExecutors",100)//最大的Executors数，如果设置得太大，资源又不足，就会等待很久
		.config("spark.yarn.executor.memoryOverHead","2G")//堆外内存，即jvm自身的开销
		.config("spark.network.timeout","600s")//spark 超时时间
		.config("spark.sql.shuffle.partitions",800)//spark sql拆分的块的数量
		.config("spark.executor.memory","2G")//jvm程序的开销
		.config("spark.sql.autoBroadcastJoinThreshold",-1)//大表join请关闭此项，小表join可以开启
		.config("spark.default.parallelism",20)//spark rdd拆分的块的数量
		.config("spark_cores_max",56)
		.getOrCreate();
		return sparkSession;
	}
	
	
	

}
