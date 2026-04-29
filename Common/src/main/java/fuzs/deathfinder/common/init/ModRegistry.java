package fuzs.deathfinder.common.init;

import fuzs.deathfinder.common.DeathFinder;
import fuzs.deathfinder.common.attachment.DeathTracker;
import fuzs.puzzleslib.common.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.common.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.common.api.init.v3.tags.TagFactory;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ModRegistry {
    static final TagFactory TAGS = TagFactory.make(DeathFinder.MOD_ID);
    public static final TagKey<EntityType<?>> SILENT_DEATHS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag("silent_deaths");

    public static final DataAttachmentType<Entity, DeathTracker> DEATH_TRACKER_ATTACHMENT_TYPE = DataAttachmentRegistry.<DeathTracker>entityBuilder()
            .persistent(DeathTracker.CODEC)
            .copyOnDeath()
            .build(DeathFinder.id("death_tracker"));

    public static void bootstrap() {
        // NO-OP
    }
}
