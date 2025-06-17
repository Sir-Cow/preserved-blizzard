package sircow.preservedblizzard.other;

import java.util.Optional;

public interface IFirstJoinTracker {
    Optional<Boolean> preserved_blizzard$getHasJoinedBefore();
    void preserved_blizzard$setHasJoinedBefore(Optional<Boolean> value);

    boolean preserved_blizzard$getHasCheckedFirstJoin();
    void preserved_blizzard$setHasCheckedFirstJoin(boolean value);
}
