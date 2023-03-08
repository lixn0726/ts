package indl.lixn.ts.common;

/**
 * @author lixn
 * @description
 * @date 2023/02/24 15:43
 **/
public class Configuration {

    private boolean clusterMode;

    private String nodeId;

    private Integer priority;

    private static boolean saveToMySQL;

    public static boolean saveToMySQL() {
        return saveToMySQL;
    }

}
