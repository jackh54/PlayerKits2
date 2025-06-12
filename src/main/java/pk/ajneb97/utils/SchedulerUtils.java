package pk.ajneb97.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * Utility class to handle scheduling tasks on both Bukkit and Folia platforms
 */
public class SchedulerUtils {
    
    private static final boolean IS_FOLIA = checkFolia();
    
    private static boolean checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public static boolean isFolia() {
        return IS_FOLIA;
    }
    
    /**
     * Run a task on the main thread
     */
    public static void runTask(Plugin plugin, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
    
    /**
     * Run a task asynchronously
     */
    public static void runTaskAsynchronously(Plugin plugin, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }
    
    /**
     * Run a task later on the main thread
     */
    public static void runTaskLater(Plugin plugin, Runnable task, long delay) {
        if (IS_FOLIA) {
            // Folia requires delay to be > 0, so ensure minimum of 1 tick
            long foliaDelay = Math.max(1, delay);
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), foliaDelay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }
    
    /**
     * Run a task later asynchronously
     */
    public static void runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay) {
        if (IS_FOLIA) {
            long delayMs = delay * 50; // Convert ticks to milliseconds
            Bukkit.getAsyncScheduler().runDelayed(plugin, scheduledTask -> task.run(), delayMs, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        }
    }
    
    /**
     * Run a repeating task on the main thread
     */
    public static Object runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        if (IS_FOLIA) {
            // Folia requires delay to be > 0, so ensure minimum of 1 tick
            long foliaDelay = Math.max(1, delay);
            return Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), foliaDelay, period);
        } else {
            return Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        }
    }
    
    /**
     * Run a repeating task asynchronously
     */
    public static Object runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period) {
        if (IS_FOLIA) {
            // Folia requires delay to be > 0, so ensure minimum of 1 tick (50ms)
            long delayMs = Math.max(50, delay * 50); // Convert ticks to milliseconds
            long periodMs = period * 50; // Convert ticks to milliseconds
            return Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delayMs, periodMs, TimeUnit.MILLISECONDS);
        } else {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
        }
    }
    
    /**
     * Run a task for a specific entity (region-specific on Folia)
     */
    public static void runEntityTask(Plugin plugin, Entity entity, Runnable task) {
        if (IS_FOLIA) {
            entity.getScheduler().run(plugin, scheduledTask -> task.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
    
    /**
     * Run a task for a specific location (region-specific on Folia)
     */
    public static void runLocationTask(Plugin plugin, Location location, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getRegionScheduler().run(plugin, location, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
    
    /**
     * Cancel a task
     */
    public static void cancelTask(Object task) {
        if (IS_FOLIA) {
            if (task instanceof io.papermc.paper.threadedregions.scheduler.ScheduledTask) {
                ((io.papermc.paper.threadedregions.scheduler.ScheduledTask) task).cancel();
            }
        } else {
            if (task instanceof org.bukkit.scheduler.BukkitTask) {
                ((org.bukkit.scheduler.BukkitTask) task).cancel();
            }
        }
    }
} 