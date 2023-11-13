package it.voxibyte.privateislands.island;

import java.util.UUID;

public class Island {
    private final UUID ownerUid;
    private final UUID worldUid;

    public Island(final UUID ownerUid, final UUID worldUid) {
        this.ownerUid = ownerUid;
        this.worldUid = worldUid;
    }

    public UUID getOwnerUid() {
        return ownerUid;
    }

    public UUID getWorldUid() {
        return worldUid;
    }
}
