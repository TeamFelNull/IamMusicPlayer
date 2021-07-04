package red.felnull.imp.client.gui.components;

import net.minecraft.client.multiplayer.PlayerInfo;
import red.felnull.imp.client.data.SimplePlayerData;
import red.felnull.imp.data.resource.AdministratorInformation;

public record AdminInfoData(SimplePlayerData playerInfo, AdministratorInformation.AuthorityType type) {
}
