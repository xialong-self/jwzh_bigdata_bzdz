package bzdz.wtsjfx;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import utils.BzdzData;
import utils.ImportMysql;
import utils.SparkConnection;

/**
 * @author 夏龙
 * @date 2020-11-05
 */
public class Bzdz_wtsjfx_all {
    public static void main(String[] args) {
        SparkSession sparkSession= SparkConnection.SparkConnectionx();
        Dataset<Row> ml_data=BzdzData.Mljbxxb(sparkSession).cache();
        Dataset<Row> ch_data=BzdzData.Chjbxxb(sparkSession).cache();
        Dataset<Row> jlx_data=BzdzData.Jlxxxb(sparkSession).cache();
        Dataset<Row> ml_data_wx= ml_data.filter("xt_zxbz='1'").cache();
        Dataset<Row> ml_data_yx= ml_data.filter("xt_zxbz='0'").cache();
        Dataset<Row> ch_data_yx= ch_data.filter("xt_zxbz='0'").cache();
        Dataset<Row> jlx_data_wx= jlx_data.filter("xt_zxbz='1'").cache();
        Dataset<Row> jlx_data_yx= jlx_data.filter("xt_zxbz='0'").cache();
        //门牌号上下级地址名称不一致（DZMC）
        Dataset<Row> dzmc_ml_data= ml_data_yx
                .selectExpr("mldzid","mljlxdm","dzjb","dqjlxmc");
        Dataset<Row> dzmc_jlx_data=jlx_data_yx
                .selectExpr("jlxdm","sjjlxmc");
        Dataset<Row> dzmc_data=dzmc_ml_data
                .join(dzmc_jlx_data,dzmc_ml_data.col("mljlxdm").equalTo(dzmc_jlx_data.col("jlxdm")))
                .filter("dqjlxmc<>sjjlxmc")
                .selectExpr("mldzid as dzid","jlxdm","dzjb","dqjlxmc","sjjlxmc")
                .distinct();
        ImportMysql.saveTjjgToEsMYSQL(dzmc_data,"bzdz_wtsjfx_dzmc");
        //上下级管辖单位不一致（GXDW）
        Dataset<Row> gxdw_ml_data=ml_data_yx
                .selectExpr("mldzid","sjdm as sjsjdm","fxjdm as sjfxjdm","pcsdm as sjpcsdm","zrqdm as sjzrqdm");
        Dataset<Row> gxdw_ch_data=ch_data_yx
                .selectExpr("mldzid","chdzid","dzjb","sjdm as dqsjdm","fxjdm as dqfxjdm","pcsdm as dqpcsdm","zrqdm as dqzrqdm");
        Dataset<Row> gxdw_data_yx=gxdw_ml_data
                .join(gxdw_ch_data,gxdw_ml_data.col("mldzid").equalTo(gxdw_ch_data.col("mldzid")))
                .selectExpr("chdzid as dzid","dzjb","sjsjdm","sjfxjdm","sjpcsdm","sjzrqdm","dqsjdm","dqfxjdm","dqpcsdm","dqzrqdm")
                .cache();
        Dataset<Row> gxdw_data_1=gxdw_data_yx.filter("sjsjdm<>dqsjdm");
        Dataset<Row> gxdw_data_2=gxdw_data_yx.filter("sjfxjdm<>dqfxjdm");
        Dataset<Row> gxdw_data_3=gxdw_data_yx.filter("sjpcsdm<>dqpcsdm");
        Dataset<Row> gxdw_data_4=gxdw_data_yx.filter("sjzrqdm<>dqzrqdm");
        Dataset<Row> gxdw_data_5=gxdw_data_yx.filter("sjsjdm is null and sjfxjdm is null and sjpcsdm is null and sjzrqdm is null and dqsjdm is null and dqfxjdm is null and dqpcsdm is null and dqzrqdm is null");
        Dataset<Row> gxdw_data=gxdw_data_1
                .union(gxdw_data_2)
                .union(gxdw_data_3)
                .union(gxdw_data_4)
                .union(gxdw_data_5);
        ImportMysql.saveTjjgToEsMYSQL(gxdw_data,"bzdz_wtsjfx_gxdw");
        gxdw_data_yx.unpersist();
        //门牌号不正确（门牌号，门牌号后缀，号附号等）（MPH）
        Dataset<Row> mph_ml_data=ml_data_yx
                .filter("mph is not null and mphhz is not null and mph is not null ")
                .selectExpr("mldzid as dzid","dzjb","mph","mphhz","mphhz_hfh","mphhz_zhfh")
                .cache();
        Dataset<Row> mph_data_1=mph_ml_data.filter("mphhz='01' and mph>=1 and mph<=9999 ")
                .selectExpr("dzid","dzjb","mph","mphhz","mphhz_hfh","mphhz_zhfh");
        Dataset<Row> mph_data_2=mph_ml_data.filter("mphhz='02' and mph>=1 and mph<=9999  and mphhz_hfh>=1 and mphhz_hfh<=9999 ")
                .selectExpr("dzid","dzjb","mph","mphhz","mphhz_hfh","mphhz_zhfh");
        Dataset<Row> mph_data_3=mph_ml_data.filter("mphhz='03' and mph>=1 and mph<=9999 and mphhz_hfh>=1 and mphhz_hfh<=9999 ")
                .selectExpr("dzid","dzjb","mph","mphhz","mphhz_hfh","mphhz_zhfh");
        Dataset<Row> mph_data_4=mph_ml_data.filter("mphhz='04' and mph>=1 and mph<=9999 and mphhz_hfh>=1 and mphhz_hfh<=9999 and mphhz_zhfh>=1 and mphhz_zhfh<=9999")
                .selectExpr("dzid","dzjb","mph","mphhz","mphhz_hfh","mphhz_zhfh");
        Dataset<Row> mph_mldzid=mph_data_1
                .union(mph_data_2)
                .union(mph_data_3)
                .union(mph_data_4);
        Dataset<Row> mph_data=mph_ml_data.except(mph_mldzid).distinct();
        ImportMysql.saveTjjgToEsMYSQL(mph_data,"bzdz_wtsjfx_mph");
        mph_ml_data.unpersist();
        //行政管辖不正确（XZGX）
        Dataset<Row> xzgx_ml_data=ml_data_yx
                .selectExpr("mldzid","dzjb","shiid","qxgxid","xzjdbscid","sqdm")
                .cache();
        Dataset<Row> xzgx_ml_data_1=xzgx_ml_data.filter("(shiid regexp'[^0-9.]')=1");
        Dataset<Row> xzgx_ml_data_2=xzgx_ml_data.filter("(qxgxid regexp'[^0-9.]')=1");
        Dataset<Row> xzgx_ml_data_3=xzgx_ml_data.filter("(xzjdbscid regexp'[^0-9.]')=1");
        Dataset<Row> xzgx_ml_data_4=xzgx_ml_data.filter("(sqdm regexp'[^0-9.]')=1");
        Dataset<Row> xzqh_data=xzgx_ml_data_1
                .union(xzgx_ml_data_2)
                .union(xzgx_ml_data_3)
                .union(xzgx_ml_data_4)
                .selectExpr("mldzid as dzid","dzjb","shiid","qxgxid","xzjdbscid","sqdm");
        ImportMysql.saveTjjgToEsMYSQL(xzqh_data,"bzdz_wtsjfx_xzgx");

        //派出所代码与责任区代码不一致的（ZRQDM）
        Dataset<Row> zrqdm_ml_data=ml_data_yx
                .filter("substr(zrqdm,1,12)<>pcsdm")
                .selectExpr("mldzid as dzid","dzjb","pcsdm","zrqdm");
        Dataset<Row> zrqdm_ch_data=ch_data_yx
                .filter("substr(zrqdm,1,12)<>pcsdm")
                .selectExpr("chdzid as dzid","dzjb","pcsdm","zrqdm");
        Dataset<Row> zrqdm_ml_data_null=ml_data_yx
                .filter("pcsdm ='' and zrqdm='' ")
                .selectExpr("mldzid as dzid","dzjb","pcsdm","zrqdm");
        Dataset<Row> zrqdm_ch_data_null=ch_data_yx
                .filter("pcsdm ='' and zrqdm='' ")
                .selectExpr("chdzid as dzid","dzjb","pcsdm","zrqdm");
        Dataset<Row> zrqdm_data=zrqdm_ml_data
                .union(zrqdm_ch_data)
                .union(zrqdm_ml_data_null)
                .union(zrqdm_ch_data_null);

        ImportMysql.saveTjjgToEsMYSQL(zrqdm_data,"bzdz_wtsjfx_zrq");

        //上级地址注销，下级地址未同步注销的情况（DELETE）
        Dataset<Row> delete_jlx_data=jlx_data_wx
                .selectExpr("jlxdm");
        Dataset<Row> delete_ml_data_yx=ml_data_yx
                .selectExpr("mldzid","dzjb","mljlxdm");
        Dataset<Row> delete_ml_data_wx=ml_data_wx
                .select("mldzid");
        Dataset<Row> delete_ch_data_yx=ch_data_yx
                .selectExpr("chdzid","dzjb","mldzid");
        //门楼
        Dataset<Row> delete_jlx_ml_data=delete_jlx_data
                .join(delete_ml_data_yx,delete_jlx_data.col("jlxdm").equalTo(delete_ml_data_yx.col("mljlxdm")))
                .selectExpr("mldzid as dzid","dzjb");
        //层户
        Dataset<Row> delete_ml_ch_data=delete_ml_data_wx
                .join(delete_ch_data_yx,delete_ml_data_wx.col("mldzid").equalTo(delete_ch_data_yx.col("mldzid")))
                .selectExpr("chdzid as dzid","dzjb");

        Dataset<Row> delete_data=delete_jlx_ml_data.union(delete_ml_ch_data);
        ImportMysql.saveTjjgToEsMYSQL(delete_data,"bzdz_wtsjfx_delete");

        //门楼为房屋，但存在下级地址（FWXJDZ）
        Dataset<Row> fwxjdz_ml_data=ml_data_yx
                .filter("sfscfw='1' ")
                .selectExpr("mldzid","dzjb");
        Dataset<Row> fwxjdz_ch_data=ch_data_yx
                .selectExpr("mldzid as chmldzid");

        Dataset<Row> fwxjdz_data=fwxjdz_ml_data
                .join(fwxjdz_ch_data,fwxjdz_ml_data.col("mldzid").equalTo(fwxjdz_ch_data.col("chmldzid")))
                .selectExpr("mldzid as dzid","dzjb")
                .distinct();
        ImportMysql.saveTjjgToEsMYSQL(fwxjdz_data,"bzdz_wtsjfx_fwxjdz");

        sparkSession.stop();

    }
}
