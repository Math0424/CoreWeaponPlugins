package me.Math0424.GunCreator;

import io.netty.channel.Channel;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.NMS.Packets.Reflection;
import me.Math0424.CoreWeapons.NMS.Packets.TinyProtocol;
import me.Math0424.GunCreator.Commands.GunCreatorCommands;
import me.Math0424.GunCreator.GUI.MainListener;
import me.Math0424.GunCreator.GUI.Page.SignInput;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GunCreator extends JavaPlugin {

    private static Plugin plugin;
    private static TinyProtocol protocol;
    private static final Reflection.FieldAccessor<String[]> text = Reflection.getField(PacketPlayInUpdateSign.class, String[].class, 0);


    public void onLoad() {
        plugin = this;
        CoreWeaponsAPI.AddPluginToUpdateChecker("84246", this);
    }

    public void onEnable() {
        MainListener mainl = new MainListener();
        Bukkit.getPluginManager().registerEvents(mainl, this);
        GunCreatorCommands cmds = new GunCreatorCommands();

        this.getCommand("guncreator").setExecutor(cmds);
        this.getCommand("guncreator").setTabCompleter(cmds);

        setupProtocol();
    }

    public void setupProtocol() {
        if (protocol != null) {
            return;
        }
        protocol = new TinyProtocol(this) {
            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                if (packet instanceof PacketPlayInUpdateSign && SignInput.containsPlayer(sender)) {
                    String[] b = text.get(packet);
                    Bukkit.getScheduler().runTask(plugin, () -> SignInput.signChangeEvent(sender, b));
                }
                return packet;
            }
        };
    }


    public static TinyProtocol getProtocol() {
        return protocol;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
