package com.braydenl.RPiModMail;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Launcher  {

    static JDA jda;
    static Bot bot = new Bot();

    public static void main(String[] args) {
        System.out.println("Hello, world!");
        try {
            String TOKEN = args[0];
            GatewayIntent[] intents = {
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MESSAGES,
            };
            jda = JDABuilder.createLight(TOKEN, Arrays.asList(intents))
                    .addEventListeners(bot)
                    .setActivity(Activity.watching("Direct Messages and you!"))
                    .build()
                    .awaitReady();
            bot.configure(jda);
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Failed to login.");
            System.exit(1);
        }
    }
}

