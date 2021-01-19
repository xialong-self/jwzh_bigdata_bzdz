package bzdz.wtsjfx;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import utils.BzdzData;
import utils.DateTimeUtils;
import utils.ImportMysql;
import utils.SparkConnection;

import java.util.Date;

/**
 * @author 夏龙
 * @date 2021-01-19
 */
public class Jzgj_bzdz_wtsjfx {
    public static void main(String[] args) {
        String Date= DateTimeUtils.DateToString(new Date(), DateTimeUtils.YYYY_MM_DD);
        String Tjzq=DateTimeUtils
                .DateToString(DateTimeUtils.StringToDate(Date, DateTimeUtils.YYYY_MM_DD),DateTimeUtils.YYYY_MM_DD)
                .replace("-","_");

        SparkSession sparkSession= SparkConnection.SparkConnectionx();
        Dataset<Row> ml_data= BzdzData.Mljbxxb(sparkSession).filter("xt_zxbz='0'")
                .selectExpr("mldzid as dzid","dzjb","sjdm","fxjdm","pcsdm","zrqdm");
        Dataset<Row> ch_data=BzdzData.Chjbxxb(sparkSession).filter("xt_zxbz='0'")
                .selectExpr("chdzid as dzid","dzjb","sjdm","fxjdm","pcsdm","zrqdm").cache();
        Dataset<Row> jzgj_data=BzdzData.Jzgjxxb(sparkSession).filter("xt_zxbz='0'")
                .selectExpr("id as jzgjid","jzd_dzid","jzd_sjdm","jzd_fxjdm","jzd_pcsdm","jzd_zrqdm","xt_lrsj")
                .repartition(100);

        //取出有效地址
        Dataset<Row> ch4_data=ch_data.filter("dzjb='4'");
        Dataset<Row> ch7_data=ch_data.filter("dzjb='7'");
        Dataset<Row> bzdz_data=ch4_data.union(ch7_data).union(ml_data);
        //关联bzdz
        Dataset<Row> bzdz_jzgj_data=jzgj_data
                .join(bzdz_data,jzgj_data.col("jzd_dzid").equalTo(bzdz_data.col("dzid")))
                .selectExpr("xt_lrsj","jzgjid","dzid","jzd_dzid","dzjb","sjdm","fxjdm","pcsdm","zrqdm","jzd_sjdm","jzd_fxjdm","jzd_pcsdm","jzd_zrqdm")
                .cache();
        //筛选
        Dataset<Row> jzgj_bzdz_wtsj1=bzdz_jzgj_data.filter("sjdm<>jzd_sjdm")
                .selectExpr("jzgjid","jzd_dzid","dzjb","sjdm as bzdz_jgdm","jzd_sjdm as jzd_jgdm","xt_lrsj");
        Dataset<Row> jzgj_bzdz_wtsj2=bzdz_jzgj_data.filter("fxjdm<>jzd_fxjdm")
                .selectExpr("jzgjid","jzd_dzid","dzjb","fxjdm as bzdz_jgdm","jzd_fxjdm as jzd_jgdm","xt_lrsj");
        Dataset<Row> jzgj_bzdz_wtsj3=bzdz_jzgj_data.filter("pcsdm<>jzd_pcsdm")
                .selectExpr("jzgjid","jzd_dzid","dzjb","pcsdm as bzdz_jgdm","jzd_pcsdm as jzd_jgdm","xt_lrsj");
        Dataset<Row> jzgj_bzdz_wtsj4=bzdz_jzgj_data.filter("zrqdm<>jzd_zrqdm")
                .selectExpr("jzgjid","jzd_dzid","dzjb","zrqdm as bzdz_jgdm","jzd_zrqdm as jzd_jgdm","xt_lrsj");

        //结果输出
        Dataset<Row> jzgz_bzdz_sc=jzgj_bzdz_wtsj1
                .union(jzgj_bzdz_wtsj2)
                .union(jzgj_bzdz_wtsj3)
                .union(jzgj_bzdz_wtsj4)
                .distinct();

        ImportMysql.saveTjjgToEsMYSQL(jzgz_bzdz_sc,"bzdz_jzgj_gxdwbyz"+Tjzq);
        sparkSession.stop();

    }
}
