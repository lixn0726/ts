package indl.lixn.ts;

import indl.lixn.ts.util.TimeUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 22:01
 **/
public class CommonTest {

    @Test
    public void test_mod() {
        System.out.println(102 % 100);
    }

    @Test
    public void test_array_elements_null() {
        Object[] objArray = new Object[1024];
        System.out.println(objArray[100] == null);
        Object obj = objArray[101];
        objArray[101] = new Object();
        System.out.println(obj == null);
    }

    @Test
    public void test_move_pointer() {
        int cur = 0;
        int circleCount = 0;
        int max = 60;
        int loopLimit = 500;
        for (int i = 0; i < loopLimit; i++) {
            cur++;
            if (cur == max) {
                cur %= max;
                circleCount++;
            }
            System.out.printf("当前%d圈，指针指向%d%n", circleCount, cur);
        }
    }

    @Test
    public void test_time_gap() throws ParseException {
        Date cur = new Date();
        System.out.println(cur.getTime() / 1000);
        String futureStr = "2023-2-22 20:20:20";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date future = format.parse(futureStr);

        int diff = (int) ((future.getTime() - cur.getTime()) / 1000);
        System.out.println("Divided by minute : " + (diff / 60));
        System.out.println("Divided by hour : " + (diff / 60 / 60));
        System.out.println("Divided by day : " + (diff / 60 / 60 / 24));
    }

    @Test
    public void test_local_timezone_id() {
        System.out.println(TimeZone.getDefault().toZoneId().getId());
    }

    @Test
    public void test_mod_equals_0() throws Exception {
        String lastStr = "2023-2-23 14:50:00";
        String previousStr = "2023-2-23 14:49:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date last = format.parse(lastStr);
        Date previous = format.parse(previousStr);

        int pointer = 0;

        int secondDiff = (int) ((last.getTime() - previous.getTime()) / 1000);
        System.out.println(secondDiff);

        int[] buckets = new int[secondDiff];
        int bucketLength = buckets.length;

        System.out.println(secondDiff / bucketLength);
        System.out.println(secondDiff % bucketLength);

    }

    @Test
    public void test_indexGet() {
        int jobDiff = 60;
        int interval = 60;
        int round = jobDiff / interval;
        int index = jobDiff % interval;
        System.out.println("层级" + round + " 坐标" + index);
    }

    @Test
    public void virtual_job_place_index_expression() {
        int curSecond = TimeUtils.currentTimeInSecond();
        System.out.println(curSecond);

    }

}
