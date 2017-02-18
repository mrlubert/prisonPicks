package com.philderbeast.prisonpicks;

import java.util.ArrayList;

import org.junit.Test;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class XpickTest {

    @Test
    public void isXPickTest()
    {
        //make a mock Xpick
        ItemMeta i = mock(ItemMeta.class);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "Explosive I");
        doReturn(lore).when(i).getLore();
        doReturn(true).when(i).hasLore();

        ItemStack xpick = mock(ItemStack.class);
        doReturn(true).when(xpick).hasItemMeta();
        doReturn(i).when(xpick).getItemMeta();

        //this is wrong
        lore.add(ChatColor.GREEN + "Explosive I");
        assertFalse(Xpick.isPick(xpick));
        //this should work
        lore.add(ChatColor.GOLD + "Explosive I");
        assertTrue(Xpick.isPick(xpick));

        

    }

}