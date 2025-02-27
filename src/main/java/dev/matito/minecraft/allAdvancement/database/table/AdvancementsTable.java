package dev.matito.minecraft.allAdvancement.database.table;

import de.mineking.databaseutils.Table;
import de.mineking.databaseutils.Where;
import de.mineking.databaseutils.exception.ConflictException;
import dev.matito.minecraft.allAdvancement.AllAdvancement;
import dev.matito.minecraft.allAdvancement.database.object.Advancements;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.advancement.Advancement;

import java.time.Instant;

public interface AdvancementsTable extends Table<Advancements> {

    default boolean addAdvancement(OfflinePlayer player, Advancement advancement) {
        AllAdvancement.INSTANCE.getLogger().info(PlainTextComponentSerializer.plainText().serialize(advancement.displayName()));
        try {
            insert(new Advancements(
                    PlainTextComponentSerializer.plainText().serialize(advancement.displayName()),
                    player,
                    Instant.now()
            ));
            return true;
        } catch (ConflictException e) {
            return false;
        }
    }

    default int getCount() {
        return getRowCount();
    }

    default int getCount(OfflinePlayer player) {
        return getRowCount(Where.equals("player", player));
    }

    default boolean removeAll() {
        //not the cleanes way to clear the database
        return delete(Where.isNotNull("advancement")) > 0;
    }
}
