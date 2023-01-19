package me.Math0424.Withered.Files.Changeable;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Files.Languages;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum Lang {

    ERRORS("messages.errors", Arrays.asList("&cYou cannot do that here!", "&cPlease wait &c{time}&c more seconds until preforming that command!")),
    BASICWORDS("messages.words", Arrays.asList("&7Drop", "in bank")),
    COMBATLOGINFO("messages.combatlog", Arrays.asList("&7You are now in &ccombat&7, you may not log out!", "&7You are no longer in &ccombat&7, you may log out!")),
    CHANGELANGUAGE("messages.language", Arrays.asList("&7Current language is: &2", "&7Changed language to: &2")),

    //currency
    CURRENCYBASICS("currency.basics", Arrays.asList("&2Picked up &a{amount}&2 {currency}", "&2Dropped &a{amount}&2 {currency}")),

    //BANKER
    BANKERINV("banker.inv", Arrays.asList("&2Money in bank", "&2Deposit", "&2Withdrawal")),
    BANKERDEPOSIT("banker.deposit", Arrays.asList("&2Depositing... please wait 5 seconds", "&cDeposit failed! you moved", "&2Deposit successful!")),

    //gunsmith
    GUNSMITHINV("gunsmith.inv", Arrays.asList("&7Attachments")),

    //EVENTS
    ASSASSINATIONEVENTSTART("events.assassination.start", Arrays.asList("&4Assassination!", "&cKill your target for a monetary reward!, &cfollow your compass to the target")),
    ASSASSINATIONEVENTINFO("events.assassination.info", Arrays.asList("&fTime to kill your target", "&fCompass pointing towards your target, &6{player}", "&cYour target left the game!")),
    ASSASSINATIONEVENTEND("events.assassination.end", Arrays.asList("&2You have successfully killed your target", "&cYour target has died you're done here.", "&cYou ran out of time!")),

    POINTCAPTUREEVENTSTART("events.pointcapture.start", Arrays.asList("&eCapture the point!", "&eBe the last one at the point by the end, follow your compass to the location")),
    POINTCAPTUREEVENTINFO("events.pointcapture.info", Arrays.asList("&eTime util the closest player wins", "&fCompass pointing towards the point", "&2You are the closest player!", "&cYou've lost the lead for closest player!")),
    POINTCAPTUREVENTEND("events.pointcapture.end", Arrays.asList("&2You were the closest player!", "&cYou failed, another player was closer!")),

    DEFENDEVENTSTART("events.defend.start", Arrays.asList("&bDefend!", "&3Fortify your location, and keep people out for monetary reward!", "&9Attack!", "&9Capture and hold the point from the enemy, kill everyone on site")),
    DEFENDEVENTINFO("events.defend.info", Arrays.asList("&fTime till the end", "&fCompass pointing towards the point", "&cPoint is under attack, {time} seconds till capture!")),
    DEFENDEVENTEND("events.defend.end", Arrays.asList("&cYou failed! the point was captured by the enemies", "&cYou failed! the point was held by the enemies", "&2You win, you have successfully held the point!", "You win, you have successfully captured the point!")),

    AIRDROPEVENTSTART("events.airdrop.start", Arrays.asList("&dAirDrop!", "&5A chest was dropped fom the sky, be the first to get to it", "&5A level {level} chest has spawned!")),
    AIRDROPEVENTINFO("events.airdrop.info", Arrays.asList("&fTime util the AirDrop is gone", "&fCompass pointing towards the loot")),
    AIRDROPEVENTEND("events.airdrop.end", Arrays.asList("&cThe AirDrop has been secured")),

    WEAPONSCACHEEVENTSTART("events.weaponscache.start", Arrays.asList("&dWeaponsCache!", "&5A weapons cache has been spotted! be the first to get to it")),
    WEAPONSCACHEEVENTINFO("events.weaponscache.info", Arrays.asList("&fTime util the cache is gone", "&fCompass pointing towards the loot")),
    WEAPONSCACHEEVENTEND("events.weaponscache.end", Arrays.asList("&2The WeaponsCache has been secured.")),

    MECHSUITEVENTSTART("events.mechsuit.start", Arrays.asList("&9Mechsuit!", "&9A mechsuit has been spotted")),
    MECHSUITEVENTINFO("events.mechsuit.info", Arrays.asList("&fTime util the mech has despawned", "&fCompass pointing towards the mech")),
    MECHSUITEVENTEND("events.mechsuit.end", Arrays.asList("&2The MechSuit has been retrieved.")),

    DROPCRATEEVENTSTART("events.dropcrate.start", Arrays.asList("&bDropCrate!", "&3A will fall at the coordinates in your compass")),
    DROPCRATEEVENTINFO("events.dropcrate.info", Arrays.asList("&fTime util the drop crate lands", "&fCompass pointing towards the drop crate", "&aSquad &6{squad}&a is in control of the DropCrate", "&6{player}&a is in control of the DropCrate", "&cThe DropCrate is contested!")),
    DROPCRATEEVENTEND("events.dropcrate.end", Arrays.asList("&cThe DropCrate has been secured.")),

    KILLCOUNTEREVENTSTART("events.killcounter.start", Arrays.asList("&cKillCount!", "&4Kill the most players by the end!")),
    KILLCOUNTEREVENTINFO("events.killcounter.info", Arrays.asList("&fTime until event over", "&fCompass pointing towards {player}")),
    KILLCOUNTEREVENTEND("events.killcounter.end", Arrays.asList("&cKillCounter has ended and no one wins", "&6{player}&e has won with the most kills!")),

    DIAMONDEVENTSTART("events.diamond.start", Arrays.asList("&9Endgame Diamond!", "&3Be the last with the diamond for a &blarge&r reward!", "&3Compass pointing towards player with the diamond")),
    DIAMONDEVENTDIAMOND("event.diamond.diamond", Arrays.asList("&7Endgame Diamond", "&8All compasses point to you", "&8hold this till the time runs out", "&cDont die!")),
    DIAMONDEVENTINFO("events.diamond.info", Arrays.asList("&fTime until end game", "&fCompass pointing towards {player} kill them!", "&2You have the diamond! stay on high alert!")),
    DIAMONDEVENTEND("events.diamond.end", Arrays.asList("&6{player}&e holds the diamond! they win!... &crestarting game t-10 seconds")),

    //DEATHS
    KILLCOUNTER("deaths.killcounter", Arrays.asList("&2{player}&6 is on a &2{amount}&6 killstreak!", "&2{player}&6 has a &2{amount}&6 killstream!", "Dominating! &2{player}&6 has a &2{amount}&6 killstreak!")),

    //items
    INFO("items.info", Arrays.asList("&fInfo", "&5You can only hold", "&4{primary} primaries", "&5and &4{secondary} secondaries")),
    RADIO("items.tracker", Arrays.asList("&7Radio", "&5Changed talk type to &a{talktype}")),
    COMPASS("items.compass", Arrays.asList("&7Compass", "&fNo events on going", "&fTime until next global event")),
    WATCH("items.watch", Arrays.asList("&7Watch", "&fHow long till next event")),

    //hacking
    STRUCTUREHACKING("structure.hacking", Arrays.asList("&7Hacking in progress {time} seconds remaining", "&2Hack succeeded! the structure is now yours", "&cHacking failed! you moved.")),
    STRUCTURETOOLS("structure.tools", Arrays.asList("Structure", "&cCannot pickup structure because inventory is full")),
    STRUCTUREINFO("structure.info", Arrays.asList("&cStructure cannot be found! is schematic missing?", "&cStructure is floating!", "&cStructure is too deep in ground!", "&cStructure cannot fit here!")),

    //mechsuit
    MECHINFO("mechsuit.info", Arrays.asList("&7Entering Mech", "&7Exiting Mech", "Exit", "&cWarning mech low health")),
    MECHENTERING("mechsuit.entering", Arrays.asList("&7Entering mech t-5 seconds...", "&cFailed to enter mech!", "&cCannot enter mech with diamond!")),

    //witheredAPI
    ARMORFAILREASON("armor.fail", Arrays.asList("&cYour {armor} are running low on fuel!", "&cYour {armor} are out of fuel!")),

    DEPLOYABLEPLACEFAIL("deployable.place.fail", Arrays.asList("&cEncroaching another deployables clearance!")),
    DEPLOYABLEPICKUPFAIL("deployable.pickup.fail", Arrays.asList("&cInventory full!", "&cDeployable is disabled!", "&cDeployable is under attack!")),
    DEPLOYABLEHACKING("deployable.hacking", Arrays.asList("&7Hacking in progress {time} seconds remaining", "&2Hack succeeded! the deployable is now yours", "&cHacking failed! you moved.")),

    //squad
    SQUADFRIENDLYFIRE("squad.messages.friendlyfire", Arrays.asList("&cDO NOT SHOOT YOUR OWN TEAMMATES")),
    SQUADPROMOTED("squad.messages.promoted", Arrays.asList("&2You have been promoted to squad leader of &6{squad}")),
    SQUADLEAVE("squad.messages.leave", Arrays.asList("&cYou have left the squad")),
    SQUADLEFT("squad.messages.left", Arrays.asList("&6{player}&2 have left the squad")),
    SQUADJOIN("squad.messages.join", Arrays.asList("&6{player}&2 has joined your squad")),
    SQUADJOINED("squad.messages.joined", Arrays.asList("&2You have joined squad &6{squad}")),
    SQUADCREATE("squad.messages.create", Arrays.asList("&2Squad &6{squad}&2 created")),
    SQUADDISBAND("squad.messages.disband", Arrays.asList("&cSquad &6{squad}&c has been disbanded")),
    //squad commands
    SQUADINVITE("squad.messages.invite", Arrays.asList("&6{player}&2 has invited you to join squad &6{squad}", "&cYou have already invited this player!")),

    SQUADINVITES("squad.messages.invites", Arrays.asList("&2Allowing all invites", "&2Denying all invites", "&2Cleared all invites")),
    SQUADINVITED("squad.messages.invited", Arrays.asList("&2Invited &6{player} &2to join squad &6{squad}")),

    SQUADCOMMAND("squad.messages.command", Arrays.asList("&cYou are already in a squad!", "&cYou are not in a squad!", "&cYou are not squad leader!"));


    private final String location;
    private final List<String> value;

    Lang(String location, List<String> value) {
        this.location = location;
        this.value = value;
    }

    public String convert(Player p) {
        return convert(p, 0);
    }

    public String convert(Player p, int i) {
        PlayerData d = PlayerData.getPlayerData(p);
        try {
            List<String> str = d.getLang().getFile().getStringList(this.getLocation());
            return ChatColor.translateAlternateColorCodes('&', str.get(i));
        } catch (Exception e) {
            return this.getValue(i);
        }
    }

    public String convertRand(Player p) {
        PlayerData d = PlayerData.getPlayerData(p);
        try {
            List<String> str = d.getLang().getFile().getStringList(this.getLocation());
            if (str.size() == 1) {
                return ChatColor.translateAlternateColorCodes('&', str.get(0));
            }
            return ChatColor.translateAlternateColorCodes('&', str.get(MyUtil.random(str.size() - 1)));
        } catch (Exception e) {
            if (value.size() == 1) {
                return ChatColor.translateAlternateColorCodes('&', value.get(0));
            }
            return this.getValue(MyUtil.random(value.size() - 1));
        }
    }

    public String getValue(int i) {
        Languages lang = Languages.valueOf(Config.SERVERLANG.getStrVal().toUpperCase());
        List<String> langString = lang.getFile().getStringList(this.getLocation());
        if (langString.size() >= i + 1) {
            return ChatColor.translateAlternateColorCodes('&', lang.getFile().getStringList(this.getLocation()).get(i));
        }
        WitheredUtil.debug("Failed to find lang value " + this.getLocation() + " using default value");
        return ChatColor.translateAlternateColorCodes('&', value.get(i));
    }

    public static String getRandomValue(Player p, String toSearch) {
        PlayerData d = PlayerData.getPlayerData(p);
        try {
            List<String> str = d.getLang().getFile().getStringList(toSearch);
            if (str.size() == 1) {
                return ChatColor.translateAlternateColorCodes('&', str.get(0));
            }
            return ChatColor.translateAlternateColorCodes('&', str.get(MyUtil.random(str.size() - 1)));
        } catch (Exception e) {
            return "Lang desc not found '" + toSearch + "'";
        }
    }

    public String getLocation() {
        return this.location;
    }

}
