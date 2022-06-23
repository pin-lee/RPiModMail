package com.braydenl.RPiModMail;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Bot extends ListenerAdapter {

    public Bot() {
        System.out.println("lol, lmao");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        User author = msg.getAuthor();
        if (author.isBot()) return;
        String content = msg.getContentRaw();


        if (content.equals("ticket") || content.equals("modmail")) {
            if (msg.isFromGuild()) {
                author.openPrivateChannel().flatMap(channel -> channel.sendMessage("Use `ticket` to create a new ticket!")).queue();
                return;
            }
            msg.reply("What up, dawg?").queue();
            System.out.println(event.getAuthor() + " said Trans rights");
        }

    }

}
