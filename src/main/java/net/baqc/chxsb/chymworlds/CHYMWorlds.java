package net.baqc.chxsb.chymworlds;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CHYMWorlds extends PluginBase {

    private static CHYMWorlds instance;

    public static CHYMWorlds getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        File file = new File(this.getDataFolder() + "/data.yml");
        Config config = new Config(file);
        if (config.get("item") == null){
            config.set("item","381:1");
        }
        if (config.get("WhiteList") == null){
            List<String> list = new ArrayList<String>();
            list.add("world");
            config.set("WhiteList",list);
        }
        config.save();
        instance = this;
        getServer().getPluginManager().registerEvents(new net.baqc.chxsb.chymworlds.FormEvent(), this);
        LoadAllLevels();
        getServer().getLogger().info(TextFormat.GREEN + "CHYMWorlds 已载入 https://github.com/CHXSB/CHYMWorlds ");
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info(TextFormat.GREEN + "CHYMWorlds 已卸载");
    }

    public void LoadAllLevels() {
        File path = new File(getServer().getDataPath() + "/worlds/");
        File[] file = path.listFiles();
        for (File f:file){
            boolean rs = getServer().loadLevel(f.getName());
        }
    }

}