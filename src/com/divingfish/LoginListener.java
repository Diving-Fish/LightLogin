package com.divingfish;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.io.File;

public class LoginListener implements Listener {
    public boolean checkPlayer(Player player) {
        return (StaticData.playerNameList.contains(player.getName().toLowerCase()));
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!checkPlayer(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!checkPlayer(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!checkPlayer(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!checkPlayer(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (!checkPlayer(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!checkPlayer(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void causeDamage(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (!checkPlayer(player)) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!checkPlayer(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName().toLowerCase();
        StaticData.playerNameList.remove(playerName);
        File file = new File("plugins\\LightLogin\\playerdata\\" + playerName + ".ll");
        if (file.exists()) {
            player.sendRawMessage(StaticData.loginMessage);
        } else {
            player.sendRawMessage(StaticData.registerMessage);
        }
    }
    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event){
        if (!checkPlayer(event.getPlayer())){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onCommandUseEvent(PlayerCommandPreprocessEvent event){
        String message = event.getMessage();
        String[] messages = message.split(" ");
        if (!checkPlayer(event.getPlayer())){
            if (!(messages[0].equals("/login")||messages[0].equals("/register")||messages[0].equals("/l")||messages[0].equals("/reg"))){
                event.setCancelled(true);
            }
        }
    }
}
