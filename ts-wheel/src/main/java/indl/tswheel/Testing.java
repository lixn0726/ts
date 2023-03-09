package indl.tswheel;

import indl.tswheel.enums.TimeUnitDimension;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 21:07
 **/
public class Testing {

    public static void main(String[] args) {
        TimeUnit minute = TimeUnit.DAYS;
        System.out.println(TimeUnitDimension.getScalesToSecond(minute));
    }

}
