package com.philderbeast.prisonpicks;

import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import com.philderbeast.prisonpicks.PrisonPicks;


public class Pickoplenty extends Pick{


    public static boolean isPick(ItemStack item)
    {
        if(Pick.isPick(item) && item.getItemMeta().getLore().contains((Object)ChatColor.LIGHT_PURPLE + "Pick o'Plenty"))
        {
            return true;
        }else {
            return false;
        }

    }
    public void breakBlock(BlockBreakEvent event)
    {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (!block.hasMetadata("blockBreaker")) {

            Location center = event.getBlock().getLocation();
            boolean hollow = false;
            int radius = 2;
            int bX = center.getBlockX();
            int bY = center.getBlockY();
            int bZ = center.getBlockZ();
            Material mat = block.getType();
            int level = 0;
            int x = bX - radius;
            while (x < bX + radius) {
                int y = bY - radius;
                while (y < bY + radius) {
                    int z = bZ - radius;
                    while (z < bZ + radius) {
                        Block b;
                        Location loc;
                        double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                        if (!(distance >= (double)(radius * radius) 
                                || hollow && distance < (double)((radius - 1) * (radius - 1)) 
                                || (b = (loc = new Location(center.getWorld(), (double)x, (double)y, (double)z)).getWorld().getBlockAt(loc)).hasMetadata("blockBreaker") 
                                || Priority.getPriority((Material)b.getType()).level <= level)) {
                            mat = b.getType();
                            level = Priority.getPriority((Material)b.getType()).level;
                        }
                        ++z;
                    }
                    ++y;
                }
                ++x;
            }
            if (mat != block.getType() && level > 0) {
                event.setCancelled(true);
                boolean fortune = false;
                boolean silk = false;
                for (Map.Entry entry : item.getEnchantments().entrySet()) {
                    if (((Enchantment)entry.getKey()).getName().equalsIgnoreCase(Enchantment.LOOT_BONUS_BLOCKS.getName())) {
                        fortune = true;
                        continue;
                    }
                    if (!((Enchantment)entry.getKey()).getName().equalsIgnoreCase(Enchantment.SILK_TOUCH.getName())) continue;
                    silk = true;
                }
                Priority p = Priority.getPriority(mat);
                ItemStack drop = p.drop;
                if (silk) {
                    drop = new ItemStack(p.mat);
                } else {
                    drop.setAmount(Util.randInt(p.amtMin, p.amtMax));
                }
                if (!silk && fortune) {
                    drop.setAmount(Util.randInt(p.amtFMin, p.amtFMax));
                }
                player.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.0, 0.5), drop);
                block.setType(Material.AIR);
            }
        }
        if (block.hasMetadata("blockBreaker")) {
            block.removeMetadata("blockBreaker", (Plugin)PrisonPicks.getInstance());
        }
    }
}