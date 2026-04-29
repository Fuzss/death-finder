package fuzs.deathfinder.handler;

import fuzs.deathfinder.DeathFinder;
import fuzs.deathfinder.config.ServerConfig;
import fuzs.deathfinder.init.ModRegistry;
import fuzs.deathfinder.util.DeathMessageBuilder;
import fuzs.deathfinder.util.DeathMessageSender;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.scores.Team;

public class DeathMessageHandler {

    public static EventResult onLivingDeath(LivingEntity livingEntity, DamageSource damageSource) {
        if (!(livingEntity.level() instanceof ServerLevel serverLevel) || !serverLevel.getGameRules()
                .get(GameRules.SHOW_DEATH_MESSAGES)) {
            return EventResult.PASS;
        }

        for (DeathMessageSource deathSource : DeathMessageSource.values()) {
            if (deathSource.test(livingEntity)) {
                DeathMessageBuilder builder = new DeathMessageBuilder(livingEntity).withPosition(DeathFinder.CONFIG.get(
                                ServerConfig.class).components.positionComponent)
                        .withDimension(DeathFinder.CONFIG.get(ServerConfig.class).components.dimensionComponent)
                        .withDistance(DeathFinder.CONFIG.get(ServerConfig.class).components.distanceComponent);
                switch (deathSource) {
                    case PLAYER -> handlePlayer((ServerPlayer) livingEntity,
                            builder,
                            new DeathMessageSender(serverLevel.getServer()));
                    case PET -> {
                        if (((TamableAnimal) livingEntity).getOwner() instanceof ServerPlayer serverPlayer) {
                            Component component = builder.build(serverPlayer);
                            serverPlayer.sendSystemMessage(component, false);
                        }
                    }
                    case VILLAGER -> new DeathMessageSender(serverLevel.getServer()).sendToAll(builder, false);
                    default -> new DeathMessageSender(serverLevel.getServer()).sendToAll(builder);
                }

                break;
            }
        }

        return EventResult.PASS;
    }

    private static void handlePlayer(ServerPlayer player, DeathMessageBuilder builder, DeathMessageSender sender) {
        Component deathMessageComponent = player.getCombatTracker().getDeathMessage();
        player.connection.send(new ClientboundPlayerCombatKillPacket(player.getId(), deathMessageComponent),
                PacketSendListener.exceptionallySend(() -> {
                    String s = deathMessageComponent.getString(256);
                    Component component = Component.translatable("death.attack.message_too_long",
                            Component.literal(s).withStyle(ChatFormatting.YELLOW));
                    Component component2 = (Component.translatable("death.attack.even_more_magic",
                            player.getDisplayName())).withStyle((Style style) -> {
                        return style.withHoverEvent(new HoverEvent.ShowText(component));
                    });
                    return new ClientboundPlayerCombatKillPacket(player.getId(), component2);
                }));
        Team team = player.getTeam();
        if (team != null && team.getDeathMessageVisibility() != Team.Visibility.ALWAYS) {
            if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
                sender.sendMessageToAllTeamMembers(player, builder);
            } else if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
                sender.sendMessageToTeamOrAllPlayers(player, builder);
            }
        } else {
            sender.sendToAll(builder);
        }
    }

    private enum DeathMessageSource {
        // enum order matters
        // players should be handled differently (in regard to teams) even when allDeaths is active
        PLAYER() {
            @Override
            boolean getConfigValue() {
                return DeathFinder.CONFIG.get(ServerConfig.class).messages.allDeaths || DeathFinder.CONFIG.get(
                        ServerConfig.class).messages.playerDeaths;
            }

            @Override
            boolean isValidEntity(LivingEntity entity) {
                return entity instanceof ServerPlayer && !entity.isSpectator();
            }
        },
        ALL() {
            @Override
            boolean getConfigValue() {
                return DeathFinder.CONFIG.get(ServerConfig.class).messages.allDeaths;
            }

            @Override
            boolean isValidEntity(LivingEntity entity) {
                return !entity.is(ModRegistry.SILENT_DEATHS_ENTITY_TYPE_TAG);
            }
        },
        NAMED() {
            @Override
            boolean getConfigValue() {
                return DeathFinder.CONFIG.get(ServerConfig.class).messages.namedEntityDeaths;
            }

            @Override
            boolean isValidEntity(LivingEntity entity) {
                return entity.hasCustomName();
            }
        },
        VILLAGER() {
            @Override
            boolean getConfigValue() {
                return DeathFinder.CONFIG.get(ServerConfig.class).messages.villagerDeaths;
            }

            @Override
            boolean isValidEntity(LivingEntity entity) {
                return entity instanceof Villager;
            }
        },
        PET() {
            @Override
            boolean getConfigValue() {
                return DeathFinder.CONFIG.get(ServerConfig.class).messages.petDeaths;
            }

            @Override
            boolean isValidEntity(LivingEntity entity) {
                return entity instanceof TamableAnimal animal && animal.getOwner() instanceof ServerPlayer;
            }
        };

        abstract boolean getConfigValue();

        abstract boolean isValidEntity(LivingEntity entity);

        public boolean test(LivingEntity entity) {
            return this.getConfigValue() && this.isValidEntity(entity);
        }
    }
}
