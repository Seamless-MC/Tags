package io.github.droppinganvil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagMenu implements Menu {
    private Inventory in;
    private HashMap<Integer, Tag> imap;
    private APlayer a;
    private int size = Core.getInstance().getConfig().getInt("TagGUI.Size");
    //private int index;
    private List<Tag> tags;
    private Boolean glow = Core.getInstance().getConfig().getBoolean("TagGUI.TagItem.Glow", true);
    public TagMenu(APlayer aP) {
        imap = new HashMap<Integer, Tag>();
        a = aP;
        //index = 1;
        tags = aP.getTags();
    }
    public void onClick(int i, ClickType clickType) {
        if (!imap.keySet().contains(i)) {return;}
        a.setActiveTag(imap.get(i));
        a.getOfflinePlayer().getPlayer().openInventory(getInventory());
    }

    public void build() {
        if (size >= tags.size()) {
                String guiName = Util.getInstance().parseString(Core.getInstance().getConfig().getString("TagGUI.Name"), a);
                in = Bukkit.createInventory(this, size, guiName);
            int iIndex = 0;
            for (Tag t : tags) {
                ItemStack tagItem = new ItemStack(Material.valueOf(Core.getInstance().getConfig().getString("TagGUI.TagItem.Material", "NAME_TAG")), 1);
                ItemMeta tagMeta = tagItem.getItemMeta();
                String name = Util.getInstance().parseString(Core.getInstance().getConfig().getString("TagGUI.TagItem.Name", "{TAGNAME} &bTag"), a);
                if (name.contains("{TAGNAME}")) {name = name.replace("{TAGNAME}", t.getName());}
                tagMeta.setDisplayName(name);
                    List<String> lore = new ArrayList<String>();
                    for (String s : Core.getInstance().getConfig().getStringList("TagGUI.TagItem.Lore")) {
                        String ss = Util.getInstance().parseString(s, a);
                        if (ss.contains("{TAGNAME}")) {ss = ss.replace("{TAGNAME}", t.getName());}
                        if (ss.contains("{PlayerDisplayName}")) {ss = ss.replace("{PlayerDisplayName}", a.getOfflinePlayer().getPlayer().getDisplayName());}
                        if (ss.contains("{ParsedTag}")) {ss = ss.replace("{ParsedTag}", t.getParsedPlaceholder(a));}
                        lore.add(ss);
                    }
                    tagMeta.setLore(lore);
                    if (glow && a.getActiveTag() == t) {
                        tagMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                        tagMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    tagItem.setItemMeta(tagMeta);
                    in.addItem(tagItem);
                    imap.put(iIndex, t);
                    iIndex++;
                }
            }
        }

    public Inventory getInventory() {
        build();
        return in;
    }
}
