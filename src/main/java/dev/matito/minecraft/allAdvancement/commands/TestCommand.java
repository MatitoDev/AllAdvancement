package dev.matito.minecraft.allAdvancement.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.matito.minecraft.allAdvancement.AllAdvancement;
import dev.matito.minecraft.allAdvancement.database.table.AdvancementsTable;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class TestCommand {
    public static void test(CommandSender sender) {
        AdvancementsTable table = AllAdvancement.INSTANCE.getAdvancementsTable();
        sender.sendMessage(Component.text("all:" + table.getCount()));
        sender.sendMessage(Component.text("player:" + table.getCount((OfflinePlayer) sender)));
    }

    public static void register() {
        new CommandAPICommand("testallad")
                .withPermission(CommandPermission.OP)
                .executes((sender, args) -> {
                    test(sender);
                })
                .register(AllAdvancement.INSTANCE);
    }
}
