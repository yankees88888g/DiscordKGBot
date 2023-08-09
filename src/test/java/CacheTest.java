import com.google.gson.reflect.TypeToken;
import com.yankees88888g.Cache.PlayerTime;
import io.github.emcw.core.EMCMap;
import io.github.emcw.core.EMCWrapper;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import io.github.emcw.utils.GsonUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CacheTest {
    static EMCWrapper emc = new EMCWrapper(true, false);
    static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4);
    static Type playerListType = new TypeToken<Map<String, PlayerTime>>() {}.getType();
    static Map<String, PlayerTime> players = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ScheduledFuture<?> cacheFuture = scheduler.scheduleAtFixedRate(() -> {
            try {
                //players = GsonUtil.deserialize("db.json", playerListType);
                updateCache(emc.getAurora());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static void updateCache(EMCMap map) throws IOException {
        System.out.println("Updating cache");

        Map<String, Player> ops = emc.getAurora().Players.online();
        players = ops.values().stream().parallel()
                .filter(player -> !player.underground())
                .collect(Collectors.toConcurrentMap(Player::getName, v -> new PlayerTime(v, System.currentTimeMillis() / 1000)));

        System.out.println(GsonUtil.serialize(players));
    }

    public static void writeToFile(String name, String data) {
        try (FileWriter writer = new FileWriter(name)) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static String getFileContents(String name) {
        try {
            return Files.readString(Paths.get(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}