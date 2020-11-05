package utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author 夏龙
 * @date 2020-11-05
 */
public class BzdzData {
    public static String BZDZ_MLJBXXB="JWZH_BZDZ.BZDZ_ADD_MLDZDXB";//标准地址门楼基本信息表
    public static String BZDZ_CHJBXXB="JWZH_BZDZ.BZDZ_ADD_CHDZDXB";//标准地址层户基本信息表
    public static String BZDZ_JLXXXB="JWZH_BZDZ.BZDZ_ADD_JLXXXB";//标准地址街路巷信息表

    public static Dataset<Row> Mljbxxb(SparkSession sparkSession){
        String sql="select mldzid,'3' dzjb,jlxdm as mljlxdm,jlxmc as dqjlxmc,sjdm,fxjdm,pcsdm,zrqdm,mphhz,mph,mphhz_hfh,mphhz_zhfh,shiid,qxgxid,xzjdbscid,sqdm,sfscfw,xt_zxbz from "+BZDZ_MLJBXXB+"";
        Dataset<Row> Data=sparkSession.sql(sql);
        return Data;
    }

    public static Dataset<Row> Chjbxxb(SparkSession sparkSession){
        String sql="select chdzid,mldzid,dzjb,sjdm,fxjdm,pcsdm,zrqdm,xt_zxbz from "+BZDZ_CHJBXXB+" ";
        Dataset<Row> Data=sparkSession.sql(sql);
        return Data;
    }

    public static Dataset<Row> Jlxxxb(SparkSession sparkSession){
        String sql="select jlxdm,jlxmc as sjjlxmc,xt_zxbz from "+BZDZ_JLXXXB+" ";
        Dataset<Row> Data=sparkSession.sql(sql);
        return Data;
    }

}
