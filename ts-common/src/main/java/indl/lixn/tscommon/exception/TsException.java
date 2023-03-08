package indl.lixn.tscommon.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TsException extends RuntimeException {

    private String msg;

    private Throwable cause;

    public TsException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public TsException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
        this.cause = cause;
    }

    public static boolean isTsException(Throwable t) {
        if (t == null) {
            return false;
        }
        return t instanceof TsException;
    }

}
