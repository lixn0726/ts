package indl.lixn.ts.core.timerwheel;

import indl.lixn.ts.core.listener.Event;
import indl.lixn.ts.core.listener.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixn
 * @description
 * @date 2023/02/24 14:41
 **/
public abstract class ObservableTimerWheel {

    private Map<Event, List<Listener>> listenersByEvent = new HashMap<>();

    public void registerListener(Event event, Listener listener) {
//        listenersByEvent.putIfAbsent()
    }

    public void callListener(Event event) {

    }


}