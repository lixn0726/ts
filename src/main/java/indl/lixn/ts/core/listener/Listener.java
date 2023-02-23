package indl.lixn.ts.core.listener;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 13:19
 **/
public interface Listener<T> {

    void onHappen(T event);

    void onError(T event);
}
