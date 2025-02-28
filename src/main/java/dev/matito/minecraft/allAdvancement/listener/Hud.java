package dev.matito.minecraft.allAdvancement.listener;


import dev.matito.minecraft.allAdvancement.AllAdvancement;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Hud implements Listener {
    private BossBar bossBar;
    private final int MAX_ADVANCEMENTS = 117;

    public Hud() {
        this.bossBar = BossBar.bossBar(Component.text("0 / " + MAX_ADVANCEMENTS),
                0f / MAX_ADVANCEMENTS,
                BossBar.Color.BLUE,
                BossBar.Overlay.PROGRESS);
    }

    public void update() {
        bossBar.name(Component.text(AllAdvancement.INSTANCE.getAdvancementsTable().getCount() + " / " + MAX_ADVANCEMENTS))
                .color(BossBar.Color.BLUE)
                .progress((float) AllAdvancement.INSTANCE.getAdvancementsTable().getCount() / MAX_ADVANCEMENTS);

        showBossBar();
    }

    public void showBossBar() {
        AllAdvancement.INSTANCE.getServer().getOnlinePlayers().forEach(p -> p.showBossBar(bossBar));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        update();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        update();
    }


}
