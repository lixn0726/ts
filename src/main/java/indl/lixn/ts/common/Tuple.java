package indl.lixn.ts.common;

import lombok.Data;

/**
 * @author lixn
 * @description
 * @date 2023/02/20 16:08
 **/
@Data
public class Tuple<Left, Right> {

    private Left left;
    private Right right;

    public Tuple() {}

    public Tuple(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

}
