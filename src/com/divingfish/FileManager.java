package com.divingfish;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FileManager {
    public String getEncryptionPassword(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            }
            else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString().substring(0,15);
    }
    public int writePlayerInfo(String playerName,String password,String address){
        if (password.length()<=StaticData.maxPasswordLength&&password.length()>=StaticData.minPasswordLength){
            File file = new File("plugins\\LightLogin\\playerdata\\"+playerName+".ll");
            String firstLine = "name: " + playerName;
            String secondLine = "password: " + getEncryptionPassword(password);
            String thirdLine = "registerIP: " + address;
            try {
                file.createNewFile();
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
                fileWriter.write(firstLine + "\n" + secondLine +"\n" + thirdLine);
                fileWriter.flush();
                fileWriter.close();
                getIPs();
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        }
        else if (password.length()>StaticData.maxPasswordLength) {
            return 3;
        } else {
            return 4;
        }
    }
    public int createPlayerInfo(String playerName,String password,String address){
        if (!new File("plugins\\LightLogin\\playerdata").exists()){
            new File("plugins\\LightLogin\\playerdata").mkdirs();
        }
        File file = new File("plugins\\LightLogin\\playerdata\\"+playerName+".ll");
        if (file.exists()){
            return 1;
            //玩家信息存在时返回1
        }
        else {
            return writePlayerInfo(playerName,password,address);
        }
    }
    public String[] readPlayerInfo(String playerName){
        File file = new File("plugins\\LightLogin\\playerdata\\"+playerName+".ll");
        String[] playerInfo = new String[4];
        if (file.exists()) {
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String firstLine = fileReader.readLine();
                String secondLine = fileReader.readLine();
                String thirdLine = fileReader.readLine();
                String[] firstElements = firstLine.split(":");
                String[] secondElements = secondLine.split(":");
                String[] thirdElements = thirdLine.split(":");
                if (firstElements[0].equals("name") && secondElements[0].equals("password") && thirdElements[0].equals("registerIP")) {
                    playerInfo[0] = firstElements[1].trim();
                    playerInfo[1] = secondElements[1].trim();
                    playerInfo[2] = thirdElements[1].trim();
                    playerInfo[3] = "0";
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                    playerInfo[0] = playerInfo[1] = playerInfo[2] = "";
                    playerInfo[3] = "2";
            } catch (Exception e) {
                playerInfo[0] = playerInfo[1] = playerInfo[2] = "";
                playerInfo[3] = "2";
            }
        }
        else {
            playerInfo[0] = playerInfo[1] = playerInfo[2] = "";
            playerInfo[3] = "1";
        }
        return playerInfo;
    }
    public ArrayList<String> getIPs(){
        ArrayList<String> ipList = new ArrayList<>();
        File directory = new File("plugins\\LightLogin\\playerdata");
        for (String s : directory.list()){
            ipList.add(readPlayerInfo(s.replace(".ll",""))[2]);
        }
        return ipList;
    }
    public void writeDefaultMessageFile() {
        File file = new File("plugins\\LightLogin\\message.lang");
        try {
            file.createNewFile();
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            fileWriter.write("argumentWrongMessage: §c参数不正确！\n" +
                    "consoleUseMessage: 控制台无法使用此命令\n" +
                    "fileProblemMessage: §c文件出现错误，请咨询管理员\n" +
                    "loginMessage: §c请使用/login <密码> 进行登录\n" +
                    "loginSuccessMessage: §a登录成功！\n" +
                    "passwordChangedMessage: §a密码更改成功！\n" +
                    "passwordNotMatchMessage: §c两次输入的密码不一致！\n" +
                    "passwordTooLongMessage: §c密码过长！\n" +
                    "passwordTooShortMessage: §c密码过短！\n" +
                    "passwordWrongMessage: §c密码错误\n" +
                    "playerAlreadyLoginMessage: §c您已经登录了！\n" +
                    "playerAlreadyRegisterMessage: §c您已经注册过了！\n" +
                    "playerNotFoundMessage: §c玩家未找到！\n" +
                    "playerNotLoginYetMessage: §c您还未登录，请登录后再进行此操作\n" +
                    "playerNotRegisterYetMessage: §c您还没有注册！\n" +
                    "registerMessage: §c您是新用户，请使用/register <密码> <重复密码> 进行注册\n" +
                    "registerSuccessMessage: §a注册成功！\n" +
                    "sameIpMessage: §c本IP已经注册过一次，请勿创建小号");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readMessageFile() {
        File file = new File("plugins\\LightLogin\\message.lang");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (true) {
                String s = reader.readLine();
                if (s == null) break;
                else StaticData.messages.put(s.split(":")[0].trim(), s.split(":")[1].trim());
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
