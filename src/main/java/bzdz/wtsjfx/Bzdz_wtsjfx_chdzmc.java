package bzdz.wtsjfx;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import utils.BzdzData;
import utils.ImportMysql;
import utils.SparkConnection;

/**
 * @author 夏龙
 * @date 2020-11-13
 */
public class Bzdz_wtsjfx_chdzmc {
    public static void main(String[] args) {



//        Dataset<Row> ch_data_ml=ch_data_yx.join(ml_data_yx,ch_data_yx.col("chmldzid").equalTo(ml_data_yx.col("mldzid")))
//                .select("chdzid","mldzid","mldzmc","dzmc","dzjb","lphhz","lphhzhfh","lph","dyh","lcwz","lch","fjhhz","fjh","fjhhzhfh","sflj");
//
//        //四级地址
//         Dataset<Row> ch_data_4sj=ch_data_ml.filter("dzjb='4'")
//                .selectExpr("chdzid","dzmc as dqdzmc","mldzmc","lph","lphhzhfh","lphhz","sflj").cache();
//        Dataset<Row> ch_data_4new01=ch_data_4sj.filter("lphhz='01'")
//                .selectExpr("chdzid","dqdzmc","mldzmc+lph+'栋' as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_4new02=ch_data_4sj.filter("lphhz='02'")
//                .selectExpr("chdzid","dqdzmc","mldzmc+lph+'栋附'+lphhzhfh+'号' as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_4=ch_data_4new01.union(ch_data_4new02).distinct();
//
//        //五级地址
//        Dataset<Row> ch_data_5=ch_data_ml.filter("dzjb='5'")
//                .selectExpr("chdzid","dqdzmc","mldz+dyh+'单元' as zqdzmc","dzjb","sflj");










        SparkSession sparkSession= SparkConnection.SparkConnectionx();
//        Dataset<Row> ml_data_yx= BzdzData.Mljbxxb(sparkSession).filter("xt_zxbz='0'")
//                .selectExpr("mldzid","dzmc as mldzmc","dzjb as mldzjb")
//                .cache();
//        Dataset<Row> ch_data_yx=BzdzData.Chjbxxb(sparkSession)
//                .withColumnRenamed("mldzid","chmldzid")
//                .filter("xt_zxbz='0'").cache();
//
//        //四级地址
//        Dataset<Row> ch_data_4sj=ch_data_yx.filter("dzjb='4'")
//                .join(ml_data_yx,ch_data_yx.col("chmldzid").equalTo(ml_data_yx.col("mldzid")))
//                .selectExpr("chdzid","mldzid as sjdzid","dzmc as dqdzmc","mldzmc","lph","lphhzhfh","lphhz","sflj","dzjb").cache();
//        Dataset<Row> ch_data_4new01=ch_data_4sj.filter("lphhz='01'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(mldzmc,lph),'栋') as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_4new02=ch_data_4sj.filter("lphhz='02'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(concat(concat(mldzmc,lph),'栋附'),lphhzhfh),'号') as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_4=ch_data_4new01.union(ch_data_4new02).distinct();
//        //五级地址
//        Dataset<Row> ch_data_4new=ch_data_4.selectExpr("chdzid as chdzid4","zqdzmc as zqdzmc4");
//        Dataset<Row> ch_data_5sj=ch_data_yx.filter("dzjb='5'")
//                .join(ch_data_4new,ch_data_yx.col("parenttreepath").equalTo(ch_data_4new.col("chdzid4")))
//                .selectExpr("chdzid","parenttreepath as sjdzid","dzmc as dqdzmc","zqdzmc4","dyh","dzjb","sflj");
//        Dataset<Row> ch_data_5=ch_data_5sj
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(zqdzmc4,dyh),'单元') as zqdzmc","dzjb","sflj");
//
//        //六级地址
//        Dataset<Row> ch_data_5new=ch_data_5.selectExpr("chdzid as chdzid5","zqdzmc as zqdzmc5");
//        Dataset<Row> ch_data_6yx=ch_data_yx.filter("dzjb='6'")
//                .selectExpr("chdzid","dzjb","substr(parenttreepath,34,32) as parentid","dzmc","lcwz","lch","sflj");
//        Dataset<Row> ch_data_6sj=ch_data_6yx
//                .join(ch_data_5new,ch_data_6yx.col("parentid").equalTo(ch_data_5new.col("chdzid5")))
//                .selectExpr("chdzid","parentid as sjdzid","dzmc as dqdzmc","zqdzmc5","dzjb","lcwz","lch","sflj").cache();
//        Dataset<Row> ch_data_6newdx=ch_data_6sj.filter("lcwz='地下'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(concat(zqdzmc5,'负'),lch),'楼') as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_6newds=ch_data_6sj.filter("lcwz<>'地下'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(zqdzmc5,lch),'楼') as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_6=ch_data_6newdx.union(ch_data_6newds).distinct();
//
//        //七级地址
//        Dataset<Row> ch_data_6new=ch_data_6.selectExpr("chdzid as chdzid6","zqdzmc as zqdzmc6");
//        Dataset<Row> ch_data_7yx=ch_data_yx.filter("dzjb='7'")
//                .selectExpr("chdzid","dzjb","substr(parenttreepath,65,32) as parentid","dzmc","fjhhz","fjh","fjhhzhfh","sflj");
//        Dataset<Row> ch_data_7sj=ch_data_7yx
//                .join(ch_data_6new,ch_data_7yx.col("parentid").equalTo(ch_data_6new.col("chdzid6")))
//                .selectExpr("chdzid","parentid as sjdzid","dzmc as dqdzmc","zqdzmc6","dzjb","fjhhz","fjh","fjhhzhfh","sflj").cache();
//        Dataset<Row> ch_data_7new01=ch_data_7sj.filter("fjhhz='01'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(zqdzmc6,fjh),'号') as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_7new02=ch_data_7sj.filter("fjhhz='02'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(concat(concat(concat(zqdzmc6,fjh),'号附'),fjhhzhfh),'号') as zqdzmc","dzjb","sflj");
//        Dataset<Row> ch_data_7=ch_data_7new01.union(ch_data_7new02).distinct();
//
//        //临
//        Dataset<Row> ch_data_wc=ch_data_4.union(ch_data_5).union(ch_data_6).union(ch_data_7).distinct();
//        Dataset<Row> ch_data_wc_li=ch_data_wc.filter("sflj='1'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","concat(zqdzmc,'临') as zqdzmc","dzjb");
//        Dataset<Row> ch_data_wc_liN=ch_data_wc.filter("sflj<>'1'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","zqdzmc","dzjb");
//        Dataset<Row> ch_data_all=ch_data_wc_li.union(ch_data_wc_liN).distinct();
//
//        Dataset<Row> ch_data_chdzmchd_sc=ch_data_all.filter("dqdzmc<>'zqdzmc'")
//                .selectExpr("chdzid","sjdzid","dqdzmc","zqdzmc","dzjb");
//
//        ImportMysql.saveTjjgToEsMYSQL(ch_data_chdzmchd_sc,"bzdz_wtsjfx_chdzdxb");

    }
}
