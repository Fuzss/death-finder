package fuzs.deathfinder.client;

import fuzs.deathfinder.client.handler.CompassTooltipHandler;
import fuzs.deathfinder.client.handler.DeathScreenHandler;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.event.v1.gui.ItemTooltipCallback;
import fuzs.puzzleslib.common.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.common.api.client.event.v1.gui.ScreenOpeningCallback;
import net.minecraft.client.gui.screens.DeathScreen;

public class DeathFinderClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ScreenEvents.afterRender(DeathScreen.class).register(DeathScreenHandler::onAfterExtract);
        ScreenOpeningCallback.EVENT.register(DeathScreenHandler::onScreenOpening);
        ItemTooltipCallback.EVENT.register(CompassTooltipHandler::onItemTooltip);
    }
}
