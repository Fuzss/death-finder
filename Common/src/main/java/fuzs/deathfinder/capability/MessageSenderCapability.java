package fuzs.deathfinder.capability;

import fuzs.deathfinder.DeathFinder;
import fuzs.deathfinder.network.ClientboundAdvancedSystemChatMessage;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import fuzs.puzzleslib.api.network.v4.NetworkingHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MessageSenderCapability extends CapabilityComponent<ServerPlayer> {
    public static final String TAG_VANILLA_CLIENT = DeathFinder.id("vanilla_client").toString();

    private boolean isVanillaClient = true;

    public void setModAvailableForClient() {
        if (this.isVanillaClient) {
            this.isVanillaClient = false;
            this.setChanged();
        }
    }

    public static void sendSystemMessage(ServerPlayer player, Component component, boolean bypassHiddenChat) {
        if (NetworkingHelper.isModPresentClientside(player, DeathFinder.MOD_ID)) {
            DeathFinder.NETWORK.sendMessage(PlayerSet.ofPlayer(player),
                    new ClientboundAdvancedSystemChatMessage(component, bypassHiddenChat));
        } else {
            player.sendSystemMessage(component, bypassHiddenChat);
        }
    }

    @Override
    public void write(CompoundTag compoundTag, HolderLookup.Provider registries) {
        compoundTag.putBoolean(TAG_VANILLA_CLIENT, this.isVanillaClient);
    }

    @Override
    public void read(CompoundTag compoundTag, HolderLookup.Provider registries) {
        if (compoundTag.contains(TAG_VANILLA_CLIENT)) {
            this.isVanillaClient = compoundTag.getBoolean(TAG_VANILLA_CLIENT);
        }
    }
}
