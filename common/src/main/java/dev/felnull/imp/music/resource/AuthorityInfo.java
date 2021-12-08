package dev.felnull.imp.music.resource;

import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class AuthorityInfo implements ITAGSerializable {
    private boolean publiced;
    private UUID owner;
    private Map<UUID, AuthorityType> authority = new HashMap<>();

    public AuthorityInfo() {
    }

    public AuthorityInfo(boolean publiced, UUID owner) {
        this(publiced, owner, new HashMap<>());
    }

    public AuthorityInfo(boolean publiced, UUID owner, Map<UUID, AuthorityType> authority) {
        this.publiced = publiced;
        this.owner = owner;
        this.authority = authority;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Public", publiced);
        tag.putUUID("Owner", owner);
        IMPNbtUtil.writeAuthority(tag, "Authority", authority);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.publiced = tag.getBoolean("Public");
        this.owner = tag.getUUID("Owner");
        IMPNbtUtil.readAuthority(tag, "Authority", authority);
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isPublic() {
        return publiced;
    }

    public AuthorityType getAuthorityType(UUID playerId) {
        if (owner.equals(playerId)) return AuthorityType.OWNER;
        var au = authority.get(playerId);
        return au == null ? getDefaultAuthority() : au;
    }

    public AuthorityType getDefaultAuthority() {
        return isPublic() ? AuthorityType.NONE : AuthorityType.BAN;
    }

    public Map<UUID, AuthorityType> getPlayersAuthority() {
        Map<UUID, AuthorityType> na = new HashMap<>(authority);
        na.put(owner, AuthorityType.OWNER);
        return Collections.unmodifiableMap(na);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityInfo that = (AuthorityInfo) o;
        return publiced == that.publiced && Objects.equals(owner, that.owner) && Objects.equals(authority, that.authority);
    }

    @Override
    public String toString() {
        return "AuthorityInfo{" +
                "publiced=" + publiced +
                ", owner=" + owner +
                ", authority=" + authority +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(publiced, owner, authority);
    }

    public static enum AuthorityType {
        OWNER("owner", 4),
        ADMIN("admin", 3),
        MEMBER("member", 2),
        READ_ONLY("read_only", 1),
        BAN("ban", 0),
        NONE("none", 0);
        private final String name;
        private final int level;

        private AuthorityType(String name, int level) {
            this.name = name;
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }

        public boolean isMoreOwner() {
            return this.level >= 4;
        }

        public boolean isMoreAdmin() {
            return this.level >= 3;
        }

        public boolean isMoreMember() {
            return this.level >= 2;
        }

        public boolean isMoreReadOnly() {
            return this.level >= 1;
        }

        public boolean isBan() {
            return this == BAN;
        }

        public static AuthorityType getByName(String name) {
            for (AuthorityType type : values()) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            return NONE;
        }
    }
}
