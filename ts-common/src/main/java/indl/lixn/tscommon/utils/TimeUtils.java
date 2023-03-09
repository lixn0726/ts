package indl.lixn.tscommon.utils;

/**
 * @author lixn
 * @description
 * @date 2023/03/09 20:45
 **/
public class TimeUtils {

    public static int currentSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
