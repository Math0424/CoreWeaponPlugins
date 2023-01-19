package me.Math0424.GunCreator.GUI.Page.Pages;

import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import me.Math0424.GunCreator.GUI.Page.Page;
import me.Math0424.GunCreator.GUI.Page.SignInput;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class MainPage extends Page {

    public Material material = Material.DIAMOND_PICKAXE;
    public int modelID = 0;

    public MainPage(MyGUI gui, String name, ItemStack background) {
        super(gui, name, background);
        setup();
    }

    private void setup() {
        setPredicate(0, ItemStackUtil.createItemStack(Material.NAME_TAG, ChatColor.WHITE + "Name",
                Arrays.asList(ChatColor.RED + "Enter a string", getGui().getGun().getName())),
                this::clickedName);

        setSignPredicate(1, ItemStackUtil.createItemStack(Material.ARROW,ChatColor.WHITE + "AmmoID",
                Arrays.asList(ChatColor.RED + "Enter a string", ChatColor.YELLOW + "Current: " + getGui().getGun().getAmmoID())),
                this::clickedAmmoId);

        setPredicate(2, ItemStackUtil.createItemStack(Material.DIAMOND, ChatColor.GREEN + "Set Quality",
                Collections.singletonList(ChatColor.WHITE + "Current: " + MyUtil.capitalize(getGui().getGun().getQualityEnum().toString()))),
                (s, p) -> { getGui().getPages()[5].open(p); });

        setPredicate(3, ItemStackUtil.createItemStack(Material.RED_STAINED_GLASS,
                ChatColor.RED + "Set BulletType",
                Arrays.asList(ChatColor.WHITE + "Current", ChatColor.RED + MyUtil.capitalize(getGui().getGun().getBulletType().toString()))),
                (s, p) -> getGui().getPages()[1].open(p));

        setPredicate(4, ItemStackUtil.createItemStack(Material.YELLOW_STAINED_GLASS,
                ChatColor.YELLOW + "Set GunType",
                Arrays.asList(ChatColor.WHITE + "Current", ChatColor.YELLOW + MyUtil.capitalize(getGui().getGun().getGunType().toString()))),
                (s, p) -> { getGui().getPages()[2].open(p); });

        setPredicate(6, ItemStackUtil.createItemStack(Material.GREEN_STAINED_GLASS,
                ChatColor.GREEN + "Set FireSound",
                Arrays.asList(ChatColor.WHITE + "Current", ChatColor.GREEN + MyUtil.capitalize(getGui().getGun().getFireSoundID()))),
                (s, p) -> { getGui().getPages()[3].open(p); });

        setSignPredicate(7, ItemStackUtil.createItemStack(Material.BELL,
                ChatColor.WHITE + "Set FireSoundRange",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getFireSoundRange())),
                (p, l) -> { getGui().getGun().setFireSoundRange(integerParse(p, l[0])); open(p); });

        setSignPredicate(8, ItemStackUtil.createItemStack(Material.NOTE_BLOCK,
                ChatColor.WHITE + "Set FirePitch",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getFirePitch())),
                (p, l) -> { getGui().getGun().setFirePitch((float)doubleParse(p, l[0])); open(p); });


        setPredicate(18, ItemStackUtil.createItemStack(Material.PAPER, ChatColor.WHITE + "BulletOptions ->",
                Arrays.asList(ChatColor.RED + "Unless specified, value", ChatColor.RED + "MUST be a number! ex '1.0' or '1'")),
                null);

        setSignPredicate(19, ItemStackUtil.createItemStack(Material.SNOWBALL,ChatColor.WHITE + "BulletsPerShot",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletCountPerShot()), getGui().getGun().getBulletCountPerShot()),
                (p, l) -> { getGui().getGun().setBulletCountPerShot(integerParse(p, l[0])); open(p); });

        setSignPredicate(20, ItemStackUtil.createItemStack(Material.SNOWBALL, ChatColor.WHITE + "BulletSpread",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletSpread()),
                (int) getGui().getGun().getBulletSpread()),
                (p, l) -> { getGui().getGun().setBulletSpread(doubleParse(p, l[0])); open(p); });

        setSignPredicate(21, ItemStackUtil.createItemStack(Material.ANVIL, ChatColor.WHITE + "BulletDrop",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletDrop())),
                (p, l) -> { getGui().getGun().setBulletDrop(doubleParse(p, l[0])); open(p); });

        setSignPredicate(22, ItemStackUtil.createItemStack(Material.FEATHER, ChatColor.WHITE + "BulletSpeed",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletSpeed())),
                (p, l) -> { getGui().getGun().setBulletSpeed(doubleParse(p, l[0])); open(p); });

        setSignPredicate(23, ItemStackUtil.createItemStack(Material.SKELETON_SKULL, ChatColor.WHITE + "BulletDamage",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletDamage())),
                (p, l) -> { getGui().getGun().setBulletDamage(doubleParse(p, l[0])); open(p); });

        setSignPredicate(24, ItemStackUtil.createItemStack(Material.WITHER_SKELETON_SKULL, ChatColor.WHITE + "HeadshotMultiplier",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getHeadshotMultiplier())),
                (p, l) -> { getGui().getGun().setHeadshotMultiplier(doubleParse(p, l[0])); open(p); });

        setSignPredicate(25, ItemStackUtil.createItemStack(Material.BLAZE_POWDER, ChatColor.WHITE + "BulletFalloff",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletFalloff())),
                (p, l) -> { getGui().getGun().setBulletFalloff(doubleParse(p, l[0])); open(p); });;

        setSignPredicate(26, ItemStackUtil.createItemStack(Material.TNT, ChatColor.WHITE + "BulletPower",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletPower())),
                (p, l) -> { getGui().getGun().setBulletPower((float)doubleParse(p, l[0])); open(p); });


        setPredicate(27, ItemStackUtil.createItemStack(Material.PAPER, ChatColor.WHITE + "GunOptions ->",
                Arrays.asList(ChatColor.RED + "Unless specified, value", ChatColor.RED + "MUST be a number! ex '1.0' or '1'")),
                null);

        setSignPredicate(28, ItemStackUtil.createItemStack(Material.SNOWBALL, ChatColor.WHITE + "BulletsPerReload",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getBulletsDrawnPerReloadCycle()), getGui().getGun().getBulletsDrawnPerReloadCycle()),
                (p, l) -> { getGui().getGun().setBulletsDrawnPerReloadCycle(integerParse(p, l[0])); open(p); });

        setSignPredicate(29, ItemStackUtil.createItemStack(Material.SNOWBALL, ChatColor.WHITE + "ShotsPerReload",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getShotsAddedPerReloadCycle()), getGui().getGun().getShotsAddedPerReloadCycle()),
                (p, l) -> { getGui().getGun().setShotsAddedPerReloadCycle(integerParse(p, l[0])); open(p); });

        setPredicate(30, ItemStackUtil.createItemStack(Material.DIAMOND_SWORD, ChatColor.WHITE + "IsPrimary",
                Arrays.asList(ChatColor.YELLOW + "Click to change primary status", ChatColor.YELLOW + "Currently set to " + getGui().getGun().isPrimaryGun())),
                (i, p) -> { getGui().getGun().setPrimaryGun(!getGui().getGun().isPrimaryGun()); setup(); open(p); });

        setSignPredicate(31, ItemStackUtil.createItemStack(Material.BOW, ChatColor.WHITE + "FireSpeed",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getFireSpeed()), getGui().getGun().getFireSpeed()),
                (p, l) -> { getGui().getGun().setFireSpeed(integerParse(p, l[0])); open(p); });

        setSignPredicate(32, ItemStackUtil.createItemStack(Material.CROSSBOW, ChatColor.WHITE + "ReloadSpeed",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getReloadSpeed()), getGui().getGun().getReloadSpeed()),
                (p, l) -> { getGui().getGun().setReloadSpeed(integerParse(p, l[0])); open(p); });

        setSignPredicate(33, ItemStackUtil.createItemStack(Material.SNOWBALL,ChatColor.WHITE + "MaxAmmo",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getMaxAmmoCount()), getGui().getGun().getMaxAmmoCount()),
                (p, l) -> { getGui().getGun().setMaxAmmoCount(integerParse(p, l[0])); open(p); });

        setSignPredicate(34, ItemStackUtil.createItemStack(Material.FIREWORK_STAR, ChatColor.WHITE + "BurstSize",
                Collections.singletonList(ChatColor.WHITE + "Current: " + getGui().getGun().getMaxBurstCount()), getGui().getGun().getMaxBurstCount()),
                (p, l) -> { getGui().getGun().setMaxBurstCount(integerParse(p, l[0])); open(p); });

        setPredicate(35, ItemStackUtil.createItemStack(Material.OAK_SIGN, ChatColor.WHITE + "Illegal attachments",
                Math.max(1, getGui().getGun().getIncompatibleAttachments().size())),
                (s, p) -> getGui().getPages()[6].open(p));

        setPredicate(45, ItemStackUtil.createItemStack(Material.SPECTRAL_ARROW,
                ChatColor.RED + "Go to ammo creator"),
                (s, p) -> getGui().getPages()[4].open(p));

        for (int i = 46; i < 54; i++) {
            setPredicate(i, ItemStackUtil.createItemStack(Material.GREEN_STAINED_GLASS_PANE, " "), null);
        }

        setPredicate(47, ItemStackUtil.createItemStack(material, ChatColor.WHITE + "Material",
                Arrays.asList(ChatColor.YELLOW + "Drop item onto this slot", ChatColor.YELLOW + "to set the material")),
                this::droppedItem);

        ItemStack stack = ItemStackUtil.createItemStack(material, getGui().getGun().getName(), 1, (short)modelID);
        getGui().getGun().ApplyToItemStack(stack);
        setPredicate(49, stack, this::giveClickedItem);

        setSignPredicate(50, ItemStackUtil.createItemStack(Material.DIAMOND_PICKAXE, ChatColor.WHITE + "SetModelID",
                Collections.singletonList(ChatColor.WHITE + "Current: " + modelID), modelID),
                (p, l) -> { modelID = integerParse(p, l[0]); open(p);});

        setPredicate(53, ItemStackUtil.createItemStack(Material.BARRIER,
                ChatColor.RED + "Reset"),
                (s, p) -> { MyGUI.resetGUI(p); MyGUI.openGUI(p); });
    }

    private void clickedName(int slot, Player player) {
        new SignInput(player, ((p, l) -> {
            if (!l[0].isEmpty()) {
                String name = ChatColor.translateAlternateColorCodes('&', l[0]);
                this.getGui().getGun().setName(name);
            }
            this.open(player);
        }));
    }

    private void droppedItem(int slot, Player player) {
        if (player.getItemOnCursor().getType() != Material.AIR) {
            material = player.getItemOnCursor().getType();
            player.setItemOnCursor(null);
            open(player);
        }
    }

    private void clickedAmmoId(Player p, String[] l) {
        ((AmmoCreationPage)this.getGui().getPages()[4]).setName(l[0]);
        this.getGui().getGun().setAmmoID(l[0]);
        open(p);
    }

    private void giveClickedItem(int slot, Player player) {
        player.setItemOnCursor(getInventory().getItem(slot));
    }


    @Override
    public void open(Player p) {
        setup();
        super.open(p);
    }
}
