package pk.ajneb97.tasks;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pk.ajneb97.PlayerKits2;
import pk.ajneb97.managers.*;
import pk.ajneb97.model.inventory.InventoryPlayer;
import pk.ajneb97.utils.InventoryUtils;
import pk.ajneb97.utils.ItemUtils;
import pk.ajneb97.utils.SchedulerUtils;

import java.util.ArrayList;

public class InventoryUpdateTaskManager {

    private PlayerKits2 plugin;
    private Object task;
    
    public InventoryUpdateTaskManager(PlayerKits2 plugin){
        this.plugin = plugin;
    }

    public void start(){
        task = SchedulerUtils.runTaskTimer(plugin, this::execute, 0L, 20L);
    }
    
    public void stop(){
        if (task != null) {
            SchedulerUtils.cancelTask(task);
        }
    }

    public void execute(){
        InventoryManager inventoryManager = plugin.getInventoryManager();
        KitsManager kitsManager = plugin.getKitsManager();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        KitItemManager kitItemManager = plugin.getKitItemManager();

        ArrayList<InventoryPlayer> players = inventoryManager.getPlayers();
        for(InventoryPlayer player : players){
            Inventory inv = InventoryUtils.getTopInventory(player.getPlayer());
            if(inv == null){
                continue;
            }
            ItemStack[] contents = inv.getContents();
            for(int i=0;i<contents.length;i++){
                ItemStack item = contents[i];
                if(item == null || item.getType().equals(Material.AIR)){
                    continue;
                }

                String kitName = ItemUtils.getTagStringItem(plugin,item,"playerkits_kit");
                if(kitName != null){
                    inventoryManager.setKit(kitName,player.getPlayer(),inv,i,kitsManager,
                            playerDataManager,kitItemManager);
                }
            }
        }
    }
}
