package dev.matito.minecraft.allAdvancement.listener;

import dev.matito.minecraft.allAdvancement.AllAdvancement;
import dev.matito.minecraft.allAdvancement.database.table.AdvancementsTable;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        if (event.getAdvancement().getDisplay() == null
            || !event.getAdvancement().getDisplay().doesAnnounceToChat()
            || !(event.getPlayer().getGameMode() == GameMode.SURVIVAL)) return;

        AdvancementsTable table = AllAdvancement.INSTANCE.getAdvancementsTable();

        table.getAdvancement(event.getAdvancement())
                .ifPresentOrElse(advancement -> {
                            event.getPlayer().sendMessage(AllAdvancement.getPrefix().append(Component.text("Das Advancement \"" + advancement.getAdvancement() + "\" wurde bereits von ")
                                    .append(Component.text(event.getPlayer().getName(), TextColor.color(0x508d98)).hoverEvent(HoverEvent.showText(Component.text("Der Spieler hat bereits " + table.getCount(event.getPlayer()) + " Advancements gemacht!", NamedTextColor.GREEN))))
                                            .append(Component.text(" erledigt!", NamedTextColor.GRAY))
                                    ));
                        },
                    () -> {

                        table.addAdvancement(event.getPlayer(), event.getAdvancement());

                        Audience audience = BukkitAudiences.create(AllAdvancement.INSTANCE).players();
                        audience.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.NEUTRAL, 999.0f, 1.3f));
                        audience.sendMessage(AllAdvancement.getPrefix().append(Component.text(event.getPlayer().getName(), TextColor.color(0x508d98)).hoverEvent(HoverEvent.showText(Component.text("Der Spieler hat bereits " + table.getCount(event.getPlayer()) + " Advancements gemacht!", NamedTextColor.GREEN))))
                                .append(Component.text(" hat das ", NamedTextColor.YELLOW))
                                .append(Component.text(table.getCount() + ". Advancement", NamedTextColor.YELLOW, TextDecoration.BOLD).hoverEvent(HoverEvent.showText(event.getAdvancement().getDisplay().displayName())))
                                .append(Component.text(" erledigt! ", NamedTextColor.YELLOW))
                                );
                });
    }
}
