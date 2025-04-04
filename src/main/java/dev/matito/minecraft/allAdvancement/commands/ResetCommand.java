package dev.matito.minecraft.allAdvancement.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.matito.minecraft.allAdvancement.AllAdvancement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;

public class ResetCommand {
    public static void reset(CommandSender sender) {
        sender.sendMessage(AllAdvancement.getPrefix().append(
                Component.text("Click ", NamedTextColor.YELLOW)
                        .append(Component.text("HERE", NamedTextColor.YELLOW, TextDecoration.BOLD, TextDecoration.UNDERLINED).clickEvent(ClickEvent.callback(audience -> {
                            try {
                                FileUtils.cleanDirectory(new File("world/advancements"));
                                sender.sendMessage(AllAdvancement.getPrefix().append(Component.text("Successfully removed all advancements from Players!", NamedTextColor.YELLOW)));
                            } catch (IOException e) {
                                sender.sendMessage(AllAdvancement.getPrefix().append(Component.text("Failed to remove all advancements from Players!")));
                            }

                            int i = AllAdvancement.INSTANCE.getAdvancementsTable().removeAll();
                            if (i <= 0) {
                                sender.sendMessage(AllAdvancement.getPrefix().append(Component.text("Failed to remove all advancements from Database!")));
                                AllAdvancement.INSTANCE.getHud().update();
                                return;
                            }

                            AllAdvancement.INSTANCE.getHud().update();
                            sender.sendMessage(AllAdvancement.getPrefix().append(Component.text("Successfully removed " + i + " advancements from Database!", NamedTextColor.YELLOW)));

                        })))
                        .append(Component.text(" to reset all advancements.", NamedTextColor.YELLOW))));
    }


    public static void register() {
        new CommandAPICommand("reset_advancements")
                .withPermission("alladvancements.reset")
                .executesPlayer((sender, args) -> {
                    reset(sender);
                })
                .register(AllAdvancement.INSTANCE);
    }
}
