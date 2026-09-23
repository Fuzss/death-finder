package fuzs.deathfinder.common.data.client;

import fuzs.deathfinder.common.client.handler.CompassTooltipHandler;
import fuzs.deathfinder.common.client.handler.DeathScreenHandler;
import fuzs.deathfinder.common.network.chat.TeleportToDeathProblem;
import fuzs.deathfinder.common.util.DeathMessageBuilder;
import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        add(DeathMessageBuilder.KEY_DEATH_MESSAGE_POSITION,
                DeathMessageBuilder.FALLBACK_DEATH_MESSAGE_POSITION);
        add(DeathMessageBuilder.KEY_DEATH_MESSAGE_DIMENSION,
                DeathMessageBuilder.FALLBACK_DEATH_MESSAGE_DIMENSION);
        add(DeathMessageBuilder.KEY_DEATH_MESSAGE_DISTANCE_DIMENSION,
                DeathMessageBuilder.FALLBACK_DEATH_MESSAGE_DISTANCE_DIMENSION);
        add(DeathMessageBuilder.KEY_DEATH_MESSAGE_DISTANCE_CLOSE,
                DeathMessageBuilder.FALLBACK_DEATH_MESSAGE_DISTANCE_CLOSE);
        add(DeathMessageBuilder.KEY_DEATH_MESSAGE_DISTANCE_BLOCKS,
                DeathMessageBuilder.FALLBACK_DEATH_MESSAGE_DISTANCE_BLOCKS);
        add(DeathScreenHandler.KEY_DEATH_SCREEN_POSITION, "X: %s Y: %s Z: %s");
        add(CompassTooltipHandler.KEY_COMPASS_POSITION, "X: %s Y: %s Z: %s");
        add(CompassTooltipHandler.KEY_COMPASS_DIMENSION, "Dimension: %s");
        TeleportToDeathProblem.forEach((TeleportToDeathProblem teleportToDeathProblem) -> {
            teleportToDeathProblem.registerTranslation(this::add);
        });
    }
}
