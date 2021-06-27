package red.felnull.imp.data.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdministratorInformation implements ITAGSerializable {
    public static final AdministratorInformation EMPTY = new AdministratorInformation(new HashMap<>());
    private Map<UUID, AuthorityType> adminData = new HashMap<>();

    public AdministratorInformation(CompoundTag tag) {
        this.load(tag);
    }

    public AdministratorInformation(Map<UUID, AuthorityType> adminData) {
        this.adminData = adminData;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        NbtUtils.writeAdminData(tag, "AdminData", adminData);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        NbtUtils.readAdminData(tag, "AdminData", adminData);
    }

    public Map<UUID, AuthorityType> getAdminData() {
        return adminData;
    }

    public AuthorityType getAuthority(UUID plId) {
        if (adminData.containsKey(plId))
            return adminData.get(plId);

        return AuthorityType.NORMAL_USER;
    }

    public static enum AuthorityType {
        OWNER("owner", true, true, true, true),
        ADMINISTRATOR("administrator", false, true, true, true),
        NORMAL_USER("normal_user", false, true, false, true),
        READ_ONLY("read_only", false, true, false, false),
        BAN("ban", false, false, false, false);

        private final String name;
        private final boolean owner;
        private final boolean read;
        private final boolean save;
        private final boolean add;

        private AuthorityType(String name, boolean owner, boolean read, boolean save, boolean add) {
            this.name = name;
            this.owner = owner;
            this.read = read;
            this.save = save;
            this.add = add;
        }

        public String getNmae() {
            return name;
        }

        public boolean canRead() {
            return read;
        }

        public boolean canAdd() {
            return add;
        }

        public boolean canSave() {
            return save;
        }

        public boolean isOwner() {
            return owner;
        }

        public static AuthorityType getAuthorityTypeByName(String name) {
            for (AuthorityType it : values()) {
                if (it.getNmae().equals(name))
                    return it;
            }
            return OWNER;
        }
    }
}
