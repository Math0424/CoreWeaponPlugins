package me.Math0424.Withered.Teams;

import me.Math0424.Withered.Chat.ChatManager;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Squad {

    private static final List<String> squadNames = Arrays.asList("Alpha", "Beta", "Math", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Omega", "Stigma", "Lambda", "Iota", "Rho", "Omicron", "Upsilon", "Kappa", "Sigma", "Ligma", "Charlie");
    private static final ArrayList<Squad> squads = new ArrayList<>();

    private final ArrayList<Player> invitedPlayers = new ArrayList<>();

    private final Color color;
    private final Team team;
    private final String name;
    private Player owner;
    private final ArrayList<Player> members = new ArrayList<>();

    public Squad(Player owner) {
        this.owner = owner;
        this.color = Color.fromBGR(MyUtil.random(255), MyUtil.random(255), MyUtil.random(255));
        this.name = generateSquadName();

        team = ScoreboardManager.getMainScoreboard().registerNewTeam(name);

        this.team.setCanSeeFriendlyInvisibles(false);
        if (!Config.NAMETAGVISIBILITY.getBoolVal()) {
            this.team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
        }
        this.team.setAllowFriendlyFire(Config.SQUADFRIENDLYFIRE.getBoolVal());
        squads.add(this);

        team.addEntry(owner.getName());
        members.add(owner);
        ScoreboardManager.updateHelmet(owner);
        owner.sendMessage(Lang.SQUADCREATE.convert(owner).replace("{squad}", this.getName()));
    }

    public void addMember(Player p) {
        team.addEntry(p.getName());
        members.add(p);
        ScoreboardManager.updateHelmet(p);
        p.sendMessage(Lang.SQUADJOINED.convert(p).replace("{squad}", name));
        sendSquadMessage(Lang.SQUADJOIN, new String[]{"{player}", p.getName()});
    }

    public void removeMember(Player p) {
        team.removeEntry(p.getName());
        members.remove(p);
        ScoreboardManager.updateHelmet(p);
        ScoreboardManager.setMainTeam(p);
        p.sendMessage(Lang.SQUADLEAVE.convert(p));
        if (members.size() == 0) {
            p.sendMessage(Lang.SQUADDISBAND.convert(p).replace("{squad}", this.getName()));
            team.unregister();
            squads.remove(this);
        } else if (p == owner) {
            sendSquadMessage(Lang.SQUADLEFT, new String[]{"{player}", p.getName()});
            promoteRandomMember();
        }
        ChatManager.rotateChannelFromSquadKick(p);
    }

    private void promoteRandomMember() {
        owner = members.get(MyUtil.random(members.size()));
        owner.sendMessage(Lang.SQUADPROMOTED.convert(owner).replace("{squad}", this.getName()));
    }

    private String generateSquadName() {
        if (owner.getName().equals("Math0424") && !exists("Math-0424")) {
            return "Math-0424";
        }
        for (int i = 0; i < 50; i++) {
            int number = MyUtil.random(99);
            String name = squadNames.get(MyUtil.random(squadNames.size()));
            String finalName = name + "-" + number;
            if (!exists(finalName)) {
                return finalName;
            }
        }
        return String.valueOf(MyUtil.random(999999999));
    }

    public void sendSquadMessage(Lang lang, String[]... replace) {
        for (Player p : members) {
            String finalMessage = lang.convert(p);
            for (String[] s : replace) {
                finalMessage = finalMessage.replace(s[0], s[1]);
            }
            p.sendMessage(finalMessage);
        }
    }

    public ArrayList<Player> getInvitedPlayers() {
        return invitedPlayers;
    }

    //Static

    public static void removeFromAllSquads(Player p) {
        if (isInSquad(p)) {
            Squad s = getPlayerSquad(p);
            s.removeMember(p);
        }
    }

    public static void removeFromAllInvites(Player p) {
        for (Squad s : squads) {
            s.getInvitedPlayers().remove(p);
        }
    }

    public static ArrayList<Squad> getAllActiveInvites(Player p) {
        ArrayList<Squad> contains = new ArrayList<>();
        for (Squad s : squads) {
            if (s.getInvitedPlayers().contains(p)) {
                contains.add(s);
            }
        }
        return contains;
    }

    public static boolean isInSquad(Player p) {
        return getPlayerSquad(p) != null;
    }

    public static boolean isInSameSquad(Player p, Player p1) {
        if (!isInSquad(p) || !isInSquad(p1)) {
            return false;
        }
        return getPlayerSquad(p) == getPlayerSquad(p1);
    }

    public static boolean isInSameSquad(Player p, String p1) {
        if (!isInSquad(p) || getPlayerSquad(p1) == null) {
            return false;
        }
        return getPlayerSquad(p) == getPlayerSquad(p1);
    }

    public static Squad getPlayerSquad(Player p) {
        for (Squad s : squads) {
            for (Player p1 : s.members) {
                if (p1 == p) {
                    return s;
                }
            }
        }
        return null;
    }

    public static Squad getPlayerSquad(String p) {
        for (Squad s : squads) {
            for (Player p1 : s.members) {
                if (p1.getName().equals(p)) {
                    return s;
                }
            }
        }
        return null;
    }

    public static Squad getSquad(String p) {
        for (Squad s : squads) {
            if (s.getName().equals(p)) {
                return s;
            }
        }
        return null;
    }


    public static boolean exists(String name) {
        for (Squad s : squads) {
            if (s.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public ArrayList<Player> getMembers() {
        return members;
    }
}
