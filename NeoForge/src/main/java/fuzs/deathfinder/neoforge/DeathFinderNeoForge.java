package fuzs.deathfinder.neoforge;

import fuzs.deathfinder.common.DeathFinder;
import fuzs.deathfinder.common.data.tags.ModEntityTypeTagProvider;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import net.neoforged.fml.common.Mod;

@Mod(DeathFinder.MOD_ID)
public class DeathFinderNeoForge {

    public DeathFinderNeoForge() {
        ModConstructor.construct(DeathFinder.MOD_ID, DeathFinder::new);
        DataProviderBuilder.of(DeathFinder.MOD_ID).addProvider(ModEntityTypeTagProvider::new);
    }
}
