package tk.complexicon.supermotd;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Main extends Plugin implements Listener {

    Logger l;
    Configuration cfg;

    String heading;
    List<String> motd;

    @Override
    public void onEnable() {
        l = getLogger();
        l.info("Registering Events...");
        getProxy().getPluginManager().registerListener(this, this);

        l.info("Loading Config...");
        mkDefaultConf();
        try{
            loadCfg();
        }catch (Exception e){
            l.info("Error Loading Config!");
            e.printStackTrace();
        }
        heading = cfg.getString("heading").replaceAll("&", "§");
        motd = cfg.getStringList("motdlist");

        getProxy().getConsole().sendMessage(new TextComponent("Setting MOTD Heading to: " + heading));

        l.info("Loaded!");
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent e){
        Random r = new Random();
        String randString = motd.get(r.nextInt(motd.size())).replaceAll("&", "§");
        e.getResponse().setDescriptionComponent(new TextComponent(heading + "\n" + randString));
    }

    public void mkDefaultConf(){
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                loadCfg();
                cfg.set("heading", "&cSuperMOTD Default Heading");
                cfg.set("motdlist", Arrays.asList("&aMOTD Random Line1", "&aMOTD Random Line2", "&aMOTD Random Line3"));
                saveCfg();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCfg() throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, new File(getDataFolder(), "config.yml"));
    }

    public void loadCfg() throws IOException {
        cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
    }

}
