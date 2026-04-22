package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;

import com.github.mdcdi1315.basemodslib.eventapi.IEvent;

import com.github.mdcdi1315.mdex.MDEXModInstance;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Event class firing when a teleport request is either scheduled or completed. <br />
 * The event does also provide the server-side player that initiated the request.
 * @param state The {@link TeleportRequestState}.
 * @param player The server-side player.
 * @since 2.2.7
 */
public record TeleportResultReceivedEvent(@NotNull TeleportRequestState state, @NotNull ServerPlayer player)
        implements IEvent
{
    public static void MDEXDefaultHandler(TeleportResultReceivedEvent event)
    {
        if (event.state.EqualsWith(TeleportRequestState.SCHEDULED)) {
            event.player.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleport_scheduled") , false);
        } else if (event.state.EqualsWith(TeleportRequestState.FAILED)) {
            event.player.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleport_failed") , false);
        } else if (event.state.EqualsWith(TeleportRequestState.COMPLETED) && MDEXModInstance.MANAGER.GetMiningDimensionLevel() == event.player.serverLevel()) {
            event.player.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleport_succeeded"), false);
        }
    }
}
