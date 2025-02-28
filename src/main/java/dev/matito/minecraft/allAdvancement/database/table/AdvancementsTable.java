package dev.matito.minecraft.allAdvancement.database.table;

import de.mineking.databaseutils.Table;
import de.mineking.databaseutils.Where;
import de.mineking.databaseutils.exception.ConflictException;
import dev.matito.minecraft.allAdvancement.database.object.Advancements;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.advancement.Advancement;

import java.time.Instant;
import java.util.Optional;

public interface AdvancementsTable extends Table<Advancements> {

    default boolean addAdvancement(OfflinePlayer player, Advancement advancement) {
        try {
            insert(new Advancements(
                    PlainTextComponentSerializer.plainText().serialize(advancement.getDisplay().title()),
                    player,
                    Instant.now()
            ));
            return true;
        } catch (ConflictException e) {
            return false;
        }
    }

    default Optional<Advancements> getAdvancement(Advancement advancement) {
        return selectOne(Where.equals("advancement", PlainTextComponentSerializer.plainText().serialize(advancement.getDisplay().title())));
    }

    default int getCount() {
        return getRowCount();
    }

    default int getCount(OfflinePlayer player) {
        return getRowCount(Where.equals("player", player));
    }

    default int removeAll() {
        //not the cleanes way to clear the database
        return delete(Where.isNotNull("advancement"));
    }
}
