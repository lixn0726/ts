package indl.lixn.ts;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;

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
        System.out.println(cur.getTime()/1000);
        String futureStr = "2023-2-22 20:20:20";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date future = format.parse(futureStr);

        int diff = (int) ((future.getTime() - cur.getTime()) / 1000);
        System.out.println("Divided by minute : " + (diff / 60));
        System.out.println("Divided by hour : " + (diff / 60 / 60));
        System.out.println("Divided by day : " + (diff / 60 / 60 / 24));
    }
}
