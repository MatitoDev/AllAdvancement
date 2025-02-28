package dev.matito.minecraft.allAdvancement;

import de.mineking.databaseutils.DatabaseManager;
import dev.matito.minecraft.allAdvancement.commands.ResetCommand;
import dev.matito.minecraft.allAdvancement.database.PlayerTypeMapper;
import dev.matito.minecraft.allAdvancement.database.object.Advancements;
import dev.matito.minecraft.allAdvancement.database.table.AdvancementsTable;
import dev.matito.minecraft.allAdvancement.listener.AdvancementListener;
import dev.matito.minecraft.allAdvancement.listener.Hud;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AllAdvancement extends JavaPlugin {


    public static AllAdvancement INSTANCE;
    public final static Dotenv CREDENTIALS = Dotenv.configure().filename("credentials").load();

    private DatabaseManager database;

    private AdvancementsTable advancementsTable;

    public Hud hud;

    public static DatabaseManager getDatabase() {return INSTANCE.database;}

    @Override
    public void onLoad() {
        if (INSTANCE != null) throw new RuntimeException("Plugin already loaded");
        INSTANCE = this;
        this.hud = new Hud();

        registerCommands();
    }

    @Override
    public void onEnable() {
        registerListeners();
        setupDatabase();
        hud.update();
    }

    private void registerCommands() {
        ResetCommand.register();
    }

    private void registerListeners() {
        registerEvent(new AdvancementListener());
        registerEvent(hud);
    }

    private void registerEvent(@NotNull Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void setupDatabase() {
        try {
            //Force server to load the driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        database = new DatabaseManager("jdbc:postgresql://" + CREDENTIALS.get("DATABASE_HOST"), CREDENTIALS.get("DATABASE_USER"), CREDENTIALS.get("DATABASE_PASSWORD"));
        database.getDriver().installPlugin(new PostgresPlugin());

        database.addMapper(new PlayerTypeMapper());


        advancementsTable = AllAdvancement.getDatabase().getTable(Advancements.class, Advancements::new).name("advancements").table(AdvancementsTable.class).create();
    }

    public static TextComponent getPrefix() {
        return Component.empty()
                .append(Component.text("[", NamedTextColor.DARK_GRAY)
                        .append(Component.text("All", TextColor.color(0x508d98)))
                        .append(Component.text("Advancements", TextColor.color(0x1b5a65)))
                        .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                ).color(NamedTextColor.GRAY);
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
    }
}
