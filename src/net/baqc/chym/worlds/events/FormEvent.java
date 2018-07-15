package net.baqc.chym.worlds.events;

import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import net.baqc.chym.worlds.CHYMWorlds;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FormEvent extends CHYMWorlds implements Listener {

    public CHYMWorlds getPlugin() {
        return CHYMWorlds.getInstance();
    }

    @EventHandler
    public void formRespond(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        FormWindow window = event.getWindow();
        if (event.getResponse() == null) return;
        if (window instanceof FormWindowSimple) {
            String title = ((FormWindowSimple) event.getWindow()).getTitle();
            String button = ((FormResponseSimple) event.getResponse()).getClickedButton().getText();
            if (!event.wasClosed()) {
                if (title.equals(TextFormat.GREEN + "传送菜单") && !button.equals(TextFormat.RED + "关闭")) {
                    Level level = getPlugin().getServer().getLevelByName(button);
                    if (getPlugin().getServer().isLevelLoaded(level.getFolderName())) {
                        player.teleport(level.getSafeSpawn());
                    } else {
                        player.sendMessage(TextFormat.RED + "ERROR:菜单无效");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void Interact(PlayerInteractEvent event){
        Config config = new Config(new File("plugins/CHYMWorlds/data.yml"));
        String item = config.getString("item");
        List<String> whitelist = config.getStringList("WhiteList");
        Player player = event.getPlayer();
        int id = event.getItem().getId();
        int damage = event.getItem().getDamage();
        if ((String.valueOf(id) + ":" + String.valueOf(damage)).equals(item)){
                final FormWindowSimple window = new FormWindowSimple(TextFormat.GREEN + "传送菜单", TextFormat.DARK_BLUE + "点击你想前往的世界");
                Map<Integer, Level> level = getPlugin().getServer().getLevels();
                window.addButton(new ElementButton(TextFormat.RED + "关闭"));
                for (Level lvl : level.values()) {
                    if (whitelist.contains(lvl.getName())){
                        window.addButton(new ElementButton(lvl.getFolderName()));
                    }
                }
                player.showFormWindow(window);
        }
        if (event.getItem().getCustomName().equals(TextFormat.GREEN + "传送菜单")){
            event.setCancelled();
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent event){
        Config config = new Config(new File("plugins/CHYMWorlds/data.yml"));
        String[] id = config.getString("item").split("\\:");
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        Boolean rs = true;
        for (int i = 0; i <= inventory.getSize(); i++){
            Item item = inventory.getItem(i);
            if (String.valueOf(item.getId()).equals(id[0]) && String.valueOf(item.getDamage()).equals(id[1])){
                rs = false;
            }
        }
        if (rs){
            Item it = new Item(Integer.valueOf(id[0]),0,1);
            it.setDamage(Integer.valueOf(id[1]));
            Item item = it.setCustomName(TextFormat.GREEN + "传送菜单");
            inventory.addItem(item);
        }
    }


}
