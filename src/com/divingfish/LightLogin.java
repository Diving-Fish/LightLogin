package com.divingfish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import static com.divingfish.StaticData.*;

public class LightLogin extends JavaPlugin {
    public void onEnable(){
        saveDefaultConfig();
        if (!new File("plugins\\LightLogin\\message.lang").exists()) {
            new FileManager().writeDefaultMessageFile();
        }
        if (!new File("plugins\\LightLogin\\playerdata").exists()) {
            new File("plugins\\LightLogin\\playerdata").mkdirs();
        }
        new FileManager().readMessageFile();
        setStringMessage();
        banSameIP = this.getConfig().getBoolean("banSameIP");
        minPasswordLength = this.getConfig().getInt("minPasswordLength");
        maxPasswordLength = this.getConfig().getInt("maxPasswordLength");
        ipList = new FileManager().getIPs();
        getLogger().info("LightLogin已加载完毕！");
        getServer().getPluginManager().registerEvents(new LoginListener(),this);
    }
    public void onDisable(){
        getLogger().info("LightLogin已停用！");
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return new Commander().onCommand(sender, command, label, args);
    }
    public void setStringMessage(){
        argumentWrongMessage = "§b[LightLogin]§r " + messages.get("argumentWrongMessage");
        consoleUseMessage = "[LightLogin] " + messages.get("consoleUseMessage");
        fileProblemMessage = "§b[LightLogin]§r " + messages.get("fileProblemMessage");
        loginMessage = "§b[LightLogin]§r " + messages.get("loginMessage");
        loginSuccessMessage = "§b[LightLogin]§r " + messages.get("loginSuccessMessage");
        passwordChangedMessage = "§b[LightLogin]§r " + messages.get("passwordChangedMessage");
        passwordNotMatchMessage = "§b[LightLogin]§r " + messages.get("passwordNotMatchMessage");
        passwordTooLongMessage = "§b[LightLogin]§r " + messages.get("passwordTooLongMessage");
        passwordTooShortMessage = "§b[LightLogin]§r " + messages.get("passwordTooShortMessage");
        passwordWrongMessage = "§b[LightLogin]§r " + messages.get("passwordWrongMessage");
        playerAlreadyLoginMessage = "§b[LightLogin]§r " + messages.get("playerAlreadyLoginMessage");
        playerAlreadyRegisterMessage = "§b[LightLogin]§r " + messages.get("playerAlreadyRegisterMessage");
        playerNotFoundMessage = "§b[LightLogin]§r " + messages.get("playerNotFoundMessage");
        playerNotLoginYetMessage = "§b[LightLogin]§r " + messages.get("playerNotLoginYetMessage");
        playerNotRegisterYetMessage = "§b[LightLogin]§r " + messages.get("playerNotRegisterYetMessage");
        registerMessage = "§b[LightLogin]§r " + messages.get("registerMessage");
        registerSuccessMessage = "§b[LightLogin]§r " + messages.get("registerSuccessMessage");
        sameIpMessage = "§b[LightLogin]§r " + messages.get("sameIpMessage");
    }
}
