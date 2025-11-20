package eu.jodelahithit;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;

public class Session {
    private final EnumMap<NotificationType, Instant> skillInstants = new EnumMap<>(NotificationType.class);
    private final SkillingNotificationsPlugin plugin;

    private Instant walkingInstant = Instant.now();
    private Instant sailingInstant = Instant.now();

    public Session(SkillingNotificationsPlugin plugin) {
        this.plugin = plugin;
    }

    public static boolean checkInstant(Instant instant, long timeout) {
        if (instant == null) return false;
        return Duration.between(instant, Instant.now()).toMillis() < timeout;
    }

    public void updateInstant(NotificationType notificationType) {
        skillInstants.put(notificationType, Instant.now());
    }

    public boolean isSkillActive(NotificationType notificationType) {
        Instant instant = skillInstants.get(notificationType);
        if (instant != null) {
            long delay = 500 + Math.max(plugin.getExtraSkillDelay(notificationType), 0);
            return checkInstant(instant, delay);
        }
        return false;
    }

    public void updateWalkingInstant(){
        walkingInstant = Instant.now();
    }

    public void updateSailingInstant(){
        sailingInstant = Instant.now();
    }

    public boolean isWalking(long extraTimeout){
        return checkInstant(walkingInstant, 1L + extraTimeout);
    }

    public boolean isSailing(){
        return checkInstant(sailingInstant, 2000L);
    }
}
