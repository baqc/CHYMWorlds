package net.baqc.chxsb.chymworlds;

import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * CHYMWorlds
 * By Shangxin && LixWorth
 * Copyright (c) 2018 彼岸倾城工作室-CHXSBTeam
 */

public class FormEvent extends CHYMWorlds implements Listener {
    public FormEvent() {
    }

    public CHYMWorlds getPlugin() {
        return CHYMWorlds.getInstance();
    }

    @EventHandler
    public void formRespond(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        FormWindow window = event.getWindow();
        if (event.getResponse() != null) {
            if (window instanceof FormWindowSimple) {
                String title = ((FormWindowSimple)event.getWindow()).getTitle();
                String button = ((FormResponseSimple)event.getResponse()).getClickedButton().getText();
                if (!event.wasClosed() && title.equals(TextFormat.GREEN + "传送菜单") && !button.equals(TextFormat.RED + "关闭")) {
                    Level level = this.getPlugin().getServer().getLevelByName(button);
                    if (this.getPlugin().getServer().isLevelLoaded(level.getFolderName())) {
                        player.teleport(level.getSafeSpawn());
                    } else {
                        player.sendMessage(TextFormat.RED + "ERROR:菜单无效");
                    }
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Interact(PlayerInteractEvent event) {
        Config config = new Config(new File("plugins/CHYMWorlds/data.yml"));
        String item = config.getString("item");
        List<String> whitelist = config.getStringList("WhiteList");
        Player player = event.getPlayer();
        int id = event.getItem().getId();
        int damage = event.getItem().getDamage();
        if ((id + ":" + damage).equals(item)) {
            FormWindowSimple window = new FormWindowSimple(TextFormat.GREEN + "传送菜单", TextFormat.DARK_BLUE + "点击你想前往的世界");
            Map<Integer, Level> level = this.getPlugin().getServer().getLevels();
            Iterator var10 = level.values().iterator();

            while(var10.hasNext()) {
                Level lvl = (Level)var10.next();
                if (whitelist.contains(lvl.getName())) {
                    window.addButton(new ElementButton(lvl.getFolderName()));
                }
            }
            window.addButton(new ElementButton(TextFormat.RED + "关闭"));

            player.showFormWindow(window);
        }

        if (event.getItem().getCustomName().equals(TextFormat.GREEN + "传送菜单")) {
            event.setCancelled();
        }

    }

    @EventHandler
    public void Join(PlayerJoinEvent event) {
        Config config = new Config(new File("plugins/CHYMWorlds/data.yml"));
        String[] id = config.getString("item").split("\\:");
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        Boolean rs = true;

        Item item;
        for(int i = 0; i <= inventory.getSize(); ++i) {
            item = inventory.getItem(i);
            if (String.valueOf(item.getId()).equals(id[0]) && String.valueOf(item.getDamage()).equals(id[1])) {
                rs = false;
            }
        }

        if (rs) {
            Item it = new Item(Integer.valueOf(id[0]), 0, 1);
            it.setDamage(Integer.valueOf(id[1]));
            item = it.setCustomName(TextFormat.GREEN + "传送菜单");
            inventory.addItem(new Item[]{item});
        }

    }
}
