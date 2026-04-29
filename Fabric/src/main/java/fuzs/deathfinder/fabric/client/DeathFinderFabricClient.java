package fuzs.deathfinder.fabric.client;

import fuzs.deathfinder.common.DeathFinder;
import fuzs.deathfinder.common.client.DeathFinderClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class DeathFinderFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(DeathFinder.MOD_ID, DeathFinderClient::new);
    }
}
