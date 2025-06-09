package sircow.preservedblizzard.other;

import java.util.Optional;

public interface IPlayerTimeData {
    Optional<Integer> getOnlineTimeTicks();
    void setOnlineTimeTicks(int ticks);
}
