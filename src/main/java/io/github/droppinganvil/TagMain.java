package io.github.droppinganvil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TagMain extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        getCommand("tag").setExecutor(this);
        getCommand("tags").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Util.getInstance().sendOnlyPlayers(sender);
                return true;
            }
            TagMenu t = new TagMenu(Core.getInstance().getFromOfflinePlayer(Bukkit.getOfflinePlayer(((Player) sender).getUniqueId())));
            ((Player) sender).openInventory(t.getInventory());
            return true;
        } else {
            if (args.length == 3 && sender.hasPermission("anviltags.give")) {
                if (args[0].equalsIgnoreCase("give")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null) {
                        APlayer a = Core.getInstance().getFromOfflinePlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
                        Tag t = Core.getInstance().getFromName(args[2]);
                        if (t.getName().equals("Error")) {
                            sender.sendMessage(ChatColor.RED + "Error: Cannot find tag named " + args[2]);
                            return true;
                        }
                        Util.getInstance().awardTag(a, t, false);
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: Cannot find player named " + args[1]);
                        return true;
                    }
                }
            }
            if (args.length == 4 && sender.hasPermission("anviltags.give.persist")) {
                if (args[0].equalsIgnoreCase("give") && args[3].equalsIgnoreCase("persist")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null) {
                        APlayer a = Core.getInstance().getFromOfflinePlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
                        Tag t = Core.getInstance().getFromName(args[2]);
                        if (t.getName().equals("Error")) {
                            sender.sendMessage(ChatColor.RED + "Error: Cannot find tag named " + args[2]);
                            return true;
                        }
                        Util.getInstance().awardTag(a, t, false);
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: Cannot find player named " + args[1]);
                        return true;
                    }
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Try /tags or /tags give");
        return false;
    }
}
