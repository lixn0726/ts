package indl.lixn.ts.core.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/24 15:15
 **/
public class EventDispatcher {

    private final List<EventWithListener> queue = new ArrayList<>();

    public void addListener(EventWithListener listener) {
        this.queue.add(listener);
    }

    public void dispatch(Event event) {
        final List<EventWithListener> list = this.queue;
        for (EventWithListener listener : list) {
            if (listener.isInterested(event)) {
                listener.onHappen(event);
            }
        }
    }

}
