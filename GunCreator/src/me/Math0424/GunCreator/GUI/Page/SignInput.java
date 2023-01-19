package me.Math0424.GunCreator.GUI.Page;

import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.GunCreator.GunCreator;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.level.block.BlockSign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class SignInput {

    public static Map<Player, SignInput> editing = new HashMap<>();

    private Player player;
    private ActionSignEventPredicate action;
    private Location loc;

    public SignInput(Player p, ActionSignEventPredicate action) {
        this.player = p;
        this.loc = p.getLocation();
        loc.setY(p.getLocation().getY() - 10);
        this.action = action;
        p.closeInventory();
        try {
            Constructor<PacketPlayOutOpenSignEditor> packetCon = PacketPlayOutOpenSignEditor.class.getConstructor(BlockPosition.class);
            Object packet = packetCon.newInstance(NMSUtil.NMS().ToBlockPosition(loc));

            player.sendBlockChange(loc, Material.OAK_SIGN.createBlockData());
            GunCreator.getProtocol().sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editing.put(p, this);
    }

    public static void signChangeEvent(Player p, String[] text) {
        if (editing.containsKey(p)) {
            SignInput in = editing.get(p);
            p.sendBlockChange(in.loc, in.loc.getBlock().getBlockData());
            in.action.run(p, text);
            editing.remove(p);
        }
    }

    public static boolean containsPlayer(Player p) {
        return editing.containsKey(p);
    }


}
