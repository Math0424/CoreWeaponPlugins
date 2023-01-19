package me.Math0424.WitheredGunGame;

import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.WitheredGunGame.Arenas.Arena;
import me.Math0424.WitheredGunGame.Arenas.ArenaPOINT;
import me.Math0424.WitheredGunGame.Arenas.ArenaTEAM;
import me.Math0424.WitheredGunGame.Data.SaveFiles;
import me.Math0424.WitheredGunGame.SignSpawner.SignData;
import me.Math0424.WitheredGunGame.SignSpawner.SignListener;
import me.Math0424.WitheredGunGame.Util.WitheredUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.logging.Level;

public class MainListeners implements Listener {

	SignListener sigL = new SignListener();

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
			WitheredUtil.log(Level.INFO, a.getName() + " <" + e.getPlayer().getDisplayName() + "> :" + e.getMessage());
		} else {
			for (Player p : e.getPlayer().getWorld().getPlayers()) {
				if (Arena.getArena(p) == null) {
					p.sendMessage(String.format(e.getFormat(), e.getPlayer().getDisplayName(), e.getMessage()));
				}
			}
			WitheredUtil.log(Level.INFO, "<" + e.getPlayer().getDisplayName() + "> :" + e.getMessage());
		}
	}
	
	@EventHandler
	public void blockBreakEvent(BlockBreakEvent e) {
		if (Arena.getArena(e.getPlayer()) != null) {
			e.setCancelled(true);
		}

		for (SignData s : SignData.signData) {
			if (MyUtil.isSameBlockLocation(e.getBlock().getLocation(), s.getSignLoc())) {
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

	private final ArrayList<Player> shot = new ArrayList<>();

	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			Arena a = Arena.getArena(attacker);
			if (a != null && a.getDead().containsKey(attacker)) {
				e.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Arena a = Arena.getArena(p);
			if (a != null) {
				if ((a.getPhase() != Arena.Phase.RUNNING) || a.getDead().containsKey(p)) {
					e.setCancelled(true);
				} else if ((int)(p.getHealth() - e.getDamage()) <= 0) {
					e.setCancelled(true);
					if (!shot.contains(p)) {
						shot.add(p);
						p.setLastDamageCause(e);
						a.playerDeath(p);
						//one tick delay for shotguns or multiple bullets hitting on one tick
						new BukkitRunnable() {
							@Override
							public void run() {
								shot.remove(p);
							}
						}.runTaskLater(WitheredGunGame.getPlugin(), 1);
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void signChangeEvent(SignChangeEvent e) {
		sigL.signChangeEvent(e);
	}
	
}
