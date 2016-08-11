package com.divingfish;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;

import static com.divingfish.StaticData.*;

public class Commander {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "login":
                login(sender, args);
                break;
            case "register":
                register(sender, args);
                break;
            case "pwc":
                changePassword(sender, args);
                break;
            case "fpwc":
                forceToChangePassword(sender, args);
                break;
            case "llhelp":
                String[] info;
                if (sender.hasPermission("ll.fpwc")) {
                    info = new String[]{"§e[LightLogin命令列表]",
                            "§a/l 或 /login <密码>                                    §6登录",
                            "§a/reg 或 /register <密码> <重复密码>          §6注册一个新账户",
                            "§a/pwc <旧密码> <新密码> <重复新密码>            §6更改你的密码",
                            "§a/fpwc <玩家> <密码> <重复密码>       §6强制更改一名玩家的密码"};
                } else {
                    info = new String[]{"§e[LightLogin命令列表]",
                            "§a/l 或 /login <密码>                                    §6登录",
                            "§a/reg 或 /register <密码> <重复密码>          §6注册一个新账户",
                            "§a/pwc <旧密码> <新密码> <重复新密码>            §6更改你的密码"};
                }
                sender.sendMessage(info);
                break;
        }
        return true;
    }
    public void login(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(consoleUseMessage);
            return;
        }
        Player player = (Player) sender;
        String playerName = player.getName().toLowerCase();
        if (StaticData.playerNameList.contains(playerName)) {
            player.sendMessage(playerAlreadyLoginMessage);
            return;
        }
        if (args.length != 1) {
            player.sendMessage(argumentWrongMessage);
            player.sendMessage(loginMessage);
            return;
        }
        String[] playerInfo = new FileManager().readPlayerInfo(playerName);
        switch (playerInfo[3]) {
            case "1":
                player.sendMessage(playerNotRegisterYetMessage);
                player.sendMessage(registerMessage);
                break;
            case "2":
                player.sendMessage(fileProblemMessage);
                break;
            case "0":
                if (!playerInfo[1].equals(new FileManager().getEncryptionPassword(args[0]))) {
                    player.sendMessage(passwordWrongMessage);
                    player.sendMessage(loginMessage);
                } else {
                    player.sendMessage(loginSuccessMessage);
                    StaticData.playerNameList.add(playerName);
                }
                break;
        }
    }
    public void register(CommandSender sender, String[] args){
        if (!(sender instanceof Player)) {
            sender.sendMessage(consoleUseMessage);
            return;
        }
        Player player = (Player)sender;
        String playerName = player.getName().toLowerCase();
        if (StaticData.playerNameList.contains(playerName)){
            player.sendMessage(playerAlreadyLoginMessage);
            return;
        }
        if (args.length != 2) {
            player.sendMessage(argumentWrongMessage);
            player.sendMessage(registerMessage);
            return;
        }
        if (!args[1].equals(args[0])) {
            player.sendMessage(passwordNotMatchMessage);
            player.sendMessage(registerMessage);
            return;
        }
        if (banSameIP) {
            for (String s : ipList) {
                if (player.getAddress().getAddress().getHostAddress().equals(s)) {
                    player.sendMessage(sameIpMessage);
                    return;
                }
            }
        }
        int errorCode = new FileManager().createPlayerInfo(playerName,args[0],player.getAddress().getAddress().getHostAddress());
        switch (errorCode) {
            case 0:
                player.sendMessage(registerSuccessMessage);
                StaticData.playerNameList.add(player.getName().toLowerCase());
                break;
            case 1:
                player.sendMessage(playerAlreadyRegisterMessage);
                Bukkit.getPluginManager().getPlugin("LightLogin").getLogger().info(playerAlreadyRegisterMessage);
                player.sendMessage(loginMessage);
                break;
            case 2:
                player.sendMessage(fileProblemMessage);
                break;
            case 3:
                player.sendMessage(passwordTooLongMessage);
                break;
            case 4:
                player.sendMessage(passwordTooShortMessage);
                break;
            default:
                player.sendMessage("不可预知的错误发生了。");
        }
    }
    public void changePassword(CommandSender sender,String[] args){
        if (!(sender instanceof Player)){
            sender.sendMessage(consoleUseMessage);
            return;
        }
        Player player = (Player)sender;
        String playerName = player.getName().toLowerCase();
        if (!StaticData.playerNameList.contains(playerName)){
            player.sendMessage(playerNotLoginYetMessage);
            return;
        }
        if (args.length != 3) {
            player.sendMessage(argumentWrongMessage);
            return;
        }
        String[] playerInfo = new FileManager().readPlayerInfo(playerName);
        if (!playerInfo[1].equals(new FileManager().getEncryptionPassword(args[0]))) {
            player.sendMessage(passwordWrongMessage);
            return;
        }
        if (!args[1].equals(args[2])) {
            player.sendMessage(passwordNotMatchMessage);
            return;
        }
        int errorCode = new FileManager().writePlayerInfo(playerName,args[1],player.getAddress().getAddress().getHostAddress());
        switch (errorCode) {
            case 0:
                player.sendMessage(passwordChangedMessage);
                break;
            case 2:
                player.sendMessage(fileProblemMessage);
                break;
            case 3:
                player.sendMessage(passwordTooLongMessage);
                break;
            case 4:
                player.sendMessage(passwordTooShortMessage);
                break;
            default:
                player.sendMessage("不可预知的错误发生了。");
        }
    }
    public void forceToChangePassword(CommandSender sender,String[] args){
        if (args.length != 3) {
            sender.sendMessage(argumentWrongMessage);
            return;
        }
        if (!playerExist(args[0])){
            sender.sendMessage(playerNotFoundMessage);
            return;
        }
        if (!args[1].equals(args[2])) {
            sender.sendMessage(passwordNotMatchMessage);
            return;
        }
        int errorCode = new FileManager().writePlayerInfo(args[0].toLowerCase(),args[1],new FileManager().readPlayerInfo(args[0])[2]);
        switch (errorCode) {
            case 0:
                sender.sendMessage(passwordChangedMessage);
                break;
            case 2:
                sender.sendMessage(fileProblemMessage);
                break;
            case 3:
                sender.sendMessage(passwordTooLongMessage);
                break;
            case 4:
                sender.sendMessage(passwordTooShortMessage);
                break;
            default:
                sender.sendMessage("不可预知的错误发生了。");
        }
    }
    public boolean playerExist(String playerName){
        String[] fileNames = new File("plugins\\LightLogin\\playerdata").list();
        boolean b = false;
        for (String s:fileNames){
            if (playerName.toLowerCase().equals(s.replace(".ll",""))){
                b = true;
                break;
            }
        }
        return b;
    }
}
