package dev.matito.minecraft.allAdvancement.database.object;

import de.mineking.databaseutils.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Advancements {
    @Column(key = true)
    private String advancement;

    @Column
    private OfflinePlayer player;
    @Column
    private Instant time;
}
