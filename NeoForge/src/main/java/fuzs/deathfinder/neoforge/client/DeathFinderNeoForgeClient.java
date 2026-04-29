package fuzs.deathfinder.neoforge.client;

import fuzs.deathfinder.common.DeathFinder;
import fuzs.deathfinder.common.client.DeathFinderClient;
import fuzs.deathfinder.common.data.client.ModLanguageProvider;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = DeathFinder.MOD_ID, dist = Dist.CLIENT)
public class DeathFinderNeoForgeClient {

    public DeathFinderNeoForgeClient() {
        ClientModConstructor.construct(DeathFinder.MOD_ID, DeathFinderClient::new);
        DataProviderHelper.registerDataProviders(DeathFinder.MOD_ID, ModLanguageProvider::new);
    }
}
