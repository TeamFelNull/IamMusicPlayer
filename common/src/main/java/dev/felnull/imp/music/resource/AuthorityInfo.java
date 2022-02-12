package dev.felnull.imp.music.resource;

import dev.felnull.imp.util.IMPNbtUtil;
import dev.felnull.otyacraftengine.server.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AuthorityInfo implements ITAGSerializable {
    private boolean publiced;
    private UUID owner;
    private String ownerName;
    private Map<UUID, AuthorityType> authority = new HashMap<>();
    private AuthorityType initialAuthority;

    public AuthorityInfo() {
    }

    public AuthorityInfo(boolean publiced, UUID owner, String ownerName, AuthorityType initialAuthority) {
        this(publiced, owner, ownerName, new HashMap<>(), initialAuthority);
    }

    public AuthorityInfo(boolean publiced, UUID owner, String ownerName, Map<UUID, AuthorityType> authority, AuthorityType initialAuthority) {
        this.publiced = publiced;
        this.owner = owner;
        this.authority = authority;
        this.ownerName = ownerName;
        this.initialAuthority = initialAuthority;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Public", publiced);
        tag.putUUID("Owner", owner);
        IMPNbtUtil.writeAuthority(tag, "Authority", authority);
        tag.putString("OwnerName", ownerName);
        tag.putString("InitialAuthority", initialAuthority.getName());
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.publiced = tag.getBoolean("Public");
        this.owner = tag.getUUID("Owner");
        IMPNbtUtil.readAuthority(tag, "Authority", authority);
        this.ownerName = tag.getString("OwnerName");
        this.initialAuthority = AuthorityType.getByName(tag.getString("InitialAuthority"));
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isPublic() {
        return publiced;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Map<UUID, AuthorityType> getRawAuthority() {
        return authority;
    }

    @NotNull
    public AuthorityType getAuthorityType(@NotNull UUID playerId) {
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

    public boolean canJoin(UUID playerId) {
        var type = getAuthorityType(playerId);
        return (!type.isBan() && publiced) || (type.isInvitation() && !publiced);
    }

    public AuthorityType getInitialAuthority() {
        return initialAuthority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityInfo that = (AuthorityInfo) o;
        return publiced == that.publiced && Objects.equals(owner, that.owner) && Objects.equals(ownerName, that.ownerName) && Objects.equals(authority, that.authority) && initialAuthority == that.initialAuthority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publiced, owner, ownerName, authority, initialAuthority);
    }

    @Override
    public String toString() {
        return "AuthorityInfo{" +
                "publiced=" + publiced +
                ", owner=" + owner +
                ", ownerName='" + ownerName + '\'' +
                ", authority=" + authority +
                ", initialAuthority=" + initialAuthority +
                '}';
    }

    public static enum AuthorityType {
        OWNER("owner", 4),
        ADMIN("admin", 3),
        MEMBER("member", 2),
        READ_ONLY("read_only", 1),
        INVITATION("invitation", 0),
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

        public boolean isInvitation() {
            return this == INVITATION;
        }

        public static AuthorityType getByName(String name) {
            for (AuthorityType type : values()) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            return NONE;
        }

        public boolean canEdit() {
            return isMoreAdmin();
        }

        public boolean canDelete() {
            return this == OWNER;
        }
    }
}
