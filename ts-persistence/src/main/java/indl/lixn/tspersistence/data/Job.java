package indl.lixn.tspersistence.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 21:57
 **/
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jobId;

    private Date submitDate;

    private boolean periodic;

}
