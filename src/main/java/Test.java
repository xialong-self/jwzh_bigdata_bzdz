import utils.DateTimeUtils;

import java.util.Date;

/**
 * @author 夏龙
 * @date 2020-11-30
 */
public class Test {
    public static void main(String[] args) {
        String Date= DateTimeUtils.DateToString(new Date(), DateTimeUtils.YYYY_MM_DD);
        String Tjzq=DateTimeUtils.DateToString(DateTimeUtils.StringToDate(Date, DateTimeUtils.YYYY_MM_DD),DateTimeUtils.YYYY_MM_DD);
        System.out.println(Tjzq);
    }
}
