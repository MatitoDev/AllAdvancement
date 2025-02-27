package dev.matito.minecraft.allAdvancement;

import de.mineking.databaseutils.DatabaseManager;
import dev.matito.minecraft.allAdvancement.commands.TestCommand;
import dev.matito.minecraft.allAdvancement.database.PlayerTypeMapper;
import dev.matito.minecraft.allAdvancement.database.object.Advancements;
import dev.matito.minecraft.allAdvancement.database.table.AdvancementsTable;
import dev.matito.minecraft.allAdvancement.listener.AdvancementListener;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AllAdvancement extends JavaPlugin {

    private DatabaseManager database;
    public static AllAdvancement INSTANCE;
    public final static Dotenv CREDENTIALS = Dotenv.configure().filename("credentials").load();

    private AdvancementsTable advancementsTable;

    public static DatabaseManager getDatabase() {return INSTANCE.database;}

    @Override
    public void onLoad() {
        if (INSTANCE != null) throw new RuntimeException("Plugin already loaded");
        INSTANCE = this;

        registerCommands();
    }

    @Override
    public void onEnable() {
        registerListeners();
        setupDatabase();

    }

    private void registerCommands() {
        TestCommand.register();
    }

    private void registerListeners() {
        registerEvent(new AdvancementListener());
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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
