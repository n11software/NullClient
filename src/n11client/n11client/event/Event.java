package n11client.event;

import java.util.ArrayList;

public class Event {
    public Event call() {
        final ArrayList<EventData> dataList = EventManager.get(this.getClass());

        if (dataList != null) {
            for (final EventData data : dataList) {
                try {
                    data.target.invoke(data.source, this);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
