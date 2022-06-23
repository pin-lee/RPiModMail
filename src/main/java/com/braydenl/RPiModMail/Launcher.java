package com.braydenl.RPiModMail;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Launcher  {

    static JDA jda;

    public static void main(String[] args) {
        System.out.println("Hello, world!");
        try {
            String TOKEN = "";
            GatewayIntent[] intents = {
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MESSAGES,
            };
            jda = JDABuilder.createLight(TOKEN, Arrays.asList(intents))
                    .addEventListeners(new Bot())
                    .setActivity(Activity.watching("#modmail and Direct Messages!"))
                    .build()
                    .awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Failed to login.");
            System.exit(1);
        }
    }
}

