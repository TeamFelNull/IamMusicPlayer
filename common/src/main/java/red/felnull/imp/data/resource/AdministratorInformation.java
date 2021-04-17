package red.felnull.imp.data.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.data.ITAGSerializable;

import java.util.Map;
import java.util.UUID;

public class AdministratorInformation implements ITAGSerializable {
    private boolean publiced;
    private Map<UUID, AuthorityType> adminData;

    public AdministratorInformation(CompoundTag tag) {
        this.load(tag);
    }

    public AdministratorInformation(boolean publiced, Map<UUID, AuthorityType> adminData) {
        this.publiced = publiced;
        this.adminData = adminData;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Publiced", publiced);
        NbtUtils.writeAdminData(tag, "AdminData", adminData);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.publiced = tag.getBoolean("Publiced");
        NbtUtils.readAdminData(tag, "AdminData", adminData);
    }

    public Map<UUID, AuthorityType> getAdminData() {
        return adminData;
    }

    public static enum AuthorityType {
        OWNER("owner", true, true, true),
        ADMINISTRATOR("administrator", false, true, true),
        READ_ONLY("read_only", false, true, false),
        BAN("ban", false, false, false);

        private final String name;
        private final boolean owner;
        private final boolean read;
        private final boolean save;

        private AuthorityType(String name, boolean owner, boolean read, boolean save) {
            this.name = name;
            this.owner = owner;
            this.read = read;
            this.save = save;
        }

        public String getNmae() {
            return name;
        }

        public boolean canRead() {
            return read;
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
