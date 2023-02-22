package indl.lixn.ts.core.callback;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 13:19
 **/
public interface Listener<T> {

    void onHappen(T event);

}
