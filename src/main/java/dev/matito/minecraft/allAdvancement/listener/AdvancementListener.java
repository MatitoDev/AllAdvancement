package dev.matito.minecraft.allAdvancement.listener;

import dev.matito.minecraft.allAdvancement.AllAdvancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        AllAdvancement.INSTANCE.getAdvancementsTable().addAdvancement(event.getPlayer(), event.getAdvancement());
        //todo  nice sound
        //      message
        //      if false give feedback that its already grant
    }

}
