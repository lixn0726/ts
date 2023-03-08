package indl.lixn.ts.core.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/24 15:13
 **/
public class EventWithListener {

    private final List<Event> interestedEvent = new ArrayList<>();

    private Listener listener;

    public boolean isInterested(Event event) {
        return this.interestedEvent.contains(event);
    }

    public void onHappen(Event event) {
        // do something
    }

    private static class AppStartRunningEvent implements Event {}

    private static class PointerMoveEvent implements Event {}

    private static class JobExecuteStartEvent implements Event {}

    private static class JobExecuteFinishEvent implements Event {}



}
