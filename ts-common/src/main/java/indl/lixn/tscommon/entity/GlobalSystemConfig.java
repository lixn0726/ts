package indl.lixn.tscommon.entity;

import lombok.Data;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 21:54
 **/
@Data
public class GlobalSystemConfig {

    private String instanceId;

    private String instanceName;

    private int dimensionLevel;

    private String databaseUrl;

    private String databaseUserName;

    private String databasePassword;

    private String databaseTablePrefix;

    private boolean autoCreateUpper;



}