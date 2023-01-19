package me.Math0424.WitheredControl;

import me.Math0424.WitheredControl.Arenas.Arena;
import me.Math0424.WitheredControl.Arenas.ArenaPOINT;
import me.Math0424.WitheredControl.Arenas.ArenaTEAM;
import me.Math0424.WitheredControl.Data.PlayerData;
import me.Math0424.WitheredControl.Data.SaveFiles;
import me.Math0424.WitheredControl.Guns.GunListeners;
import me.Math0424.WitheredControl.SignSpawner.SignData;
import me.Math0424.WitheredControl.SignSpawner.SignListener;
import me.Math0424.WitheredControl.Util.WitheredUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;

public class MainListeners implements Listener {
	
	GunListeners gunL = new GunListeners();
	SignListener sigL = new SignListener();
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {	
		boolean createData = true;
		for (PlayerData data : WitheredControl.getPlugin().playerData) {
			if (data.getName().equals(e.getPlayer().getName())) {
				createData = false;
				break;
			}
		}
		if (createData) {
			WitheredControl.getPlugin().playerData.add(new PlayerData(e.getPlayer().getName()));
			WitheredUtil.debug("Created new PlayerDataEntry for player " + e.getPlayer().getName());
			SaveFiles.savePlayerData();
		}
		
		byte[] hash = DatatypeConverter.parseHexBinary(Config.RESOURCEPACKHASH.getStrVal().toLowerCase());
		System.out.println(Config.RESOURCEPACKHASH.getStrVal().toLowerCase());
		e.getPlayer().setResourcePack(Config.RESOURCEPACK.getStrVal(), hash);
		
	}
	
	@EventHandler
	public void playerResourcePackStatusEvent(PlayerResourcePackStatusEvent e) {
		if (Bukkit.getServer().getPluginManager().getPlugin("Withered") != null) {
			return;
		}
		switch (e.getStatus()) {
		case ACCEPTED:
			break;
		case DECLINED:
			if (Config.RESOURCEPACKFORCE.getBoolVal()) {
				e.getPlayer().kickPlayer(ChatColor.RED + "You must use the ResourcePack!");
			}
			break;
		case FAILED_DOWNLOAD:
			e.getPlayer().setResourcePack(Config.RESOURCEPACK.getStrVal());
			break;
		case SUCCESSFULLY_LOADED:
			break;
		}

	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e) {		
		Arena a = Arena.getArena(e.getPlayer());
		if (a != null) {
			a.removePlayer(e.getPlayer(), true);
		}
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		sigL.playerInteractEvent(e);
		Arena a = Arena.getArena(e.getPlayer());
		if (e.getItem() != null && e.getItem().getType() == Material.BLUE_WOOL) {
			if (a instanceof ArenaTEAM) {
				ArenaTEAM at = (ArenaTEAM)a;
				at.setBlueTeam(e.getPlayer(), false);
			} else if(a instanceof ArenaPOINT) {
				ArenaPOINT at = (ArenaPOINT)a;
				at.setBlueTeam(e.getPlayer(), false);
			}
		} else if (e.getItem() != null && e.getItem().getType() == Material.RED_WOOL) {
			if (a instanceof ArenaTEAM) {
				ArenaTEAM at = (ArenaTEAM)a;
				at.setRedTeam(e.getPlayer(), false);
			} else if(a instanceof ArenaPOINT) {
				ArenaPOINT at = (ArenaPOINT)a;
				at.setRedTeam(e.getPlayer(), false);
			}
		}
	}
	
	@EventHandler
	public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void foodLevelChangeEvent(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player && Arena.getArena((Player) e.getEntity()) != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerBedEnterEvent(PlayerBedEnterEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerDropItemEvent(PlayerDropItemEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e) {
		if (Arena.getArena((Player) e.getWhoClicked()) != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerChatEvent(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		Arena a = Arena.getArena(e.getPlayer());
		if (a != null) {
			for (Player p : e.getPlayer().getWorld().getPlayers()) {
				if (Arena.getArena(p) != null && Arena.getArena(p) == a) {
					p.sendMessage(String.format(e.getFormat(), e.getPlayer().getDisplayName(), e.getMessage()));
				}
			}
			WitheredUtil.info(a.getName() + " <" + e.getPlayer().getDisplayName() + "> :" + e.getMessage());
		} else {
			for (Player p : e.getPlayer().getWorld().getPlayers()) {
				if (Arena.getArena(p) == null) {
					p.sendMessage(String.format(e.getFormat(), e.getPlayer().getDisplayName(), e.getMessage()));
				}
			}
			WitheredUtil.info("<" + e.getPlayer().getDisplayName() + "> :" + e.getMessage());
		}
	}
	
	@EventHandler
	public void blockBreakEvent(BlockBreakEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}

		for (SignData s : SignData.signData) {
			if (WitheredUtil.isSameLocation(e.getBlock().getLocation(), s.getSignLoc())) {
				if (e.getPlayer().isOp() && e.getPlayer().isSneaking()) {
					SignData.signData.remove(s);
					SaveFiles.saveSignData();
					e.getPlayer().sendMessage(ChatColor.GREEN + "Removed sign");
					return;
				} else {
					e.setCancelled(true);
				}
			}
		}	
	}
	
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}
	}

	private ArrayList<Player> shot = new ArrayList<>();

	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Arena a = Arena.getArena(p);
			if (a != null && !a.isGameRunning()) {
				e.setCancelled(true);
			} else if (a != null && (int)(p.getHealth() - e.getDamage()) <= 0) {
				e.setCancelled(true);
				if (!shot.contains(p)) {
					shot.add(p);
					p.setLastDamageCause(e);
					a.playerDeath(p);
					//one tick delay for shotguns
					new BukkitRunnable() {
						@Override
						public void run() {
							shot.remove(p);
						}
					}.runTaskLater(WitheredControl.getPlugin(), 1);
				}
			}
		}
	}
	
	
	@EventHandler
	public void signChangeEvent(SignChangeEvent e) {
		sigL.signChangeEvent(e);
	}
	
}
