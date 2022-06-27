package com.braydenl.RPiModMail;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;

public class Bot extends ListenerAdapter {

    HashMap<User, String> usersAndTickets = new HashMap<>();

    JDA jda;
    TextChannel modMail;
    int ticketNumber = 1;

    public Bot() {
        System.out.println("lol, lmao");
    }

    public void configure(JDA jda) {
        this.jda = jda;
        this.modMail = jda.getTextChannelById("989330012689813585");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        User author = msg.getAuthor();
        if (author.isBot()) return;
        String content = msg.getContentRaw();

        if (msg.getChannel().equals(modMail) && content.equals("!maintenance")) {
            // send a message to all users in usersAndTickets
                for (User user : usersAndTickets.keySet()) {
                        user.openPrivateChannel().queue(channel -> channel.sendMessage("The bot is going down for maintenance. " +
                                "All currently cached tickets will be lost. Please reopen your ticket once the bot is back up. " +
                                "If the issue is urgent, please ping and/or direct-message an online moderator.").queue());
                }
                System.out.println("Sent maintenance message to all users. Shutting down.");
                return;
        }

        if (content.equals("ticket") || content.equals("modmail")) {
            if (msg.isFromGuild()) {
                author.openPrivateChannel().flatMap(channel -> channel.sendMessage("Use `ticket` to create a new ticket!")).queue();
                return;
            }
            msg.reply("What's up? Post text, screenshots, etc concerning this ticket's subject matter. **To finalize the ticket and " +
                    "send it off, tell me** `ticket complete`**!**").queue();
            if (!usersAndTickets.containsKey(author)) {
                usersAndTickets.put(author, "");
                return;
            }
        }

        if (!msg.isFromGuild() && content.equals("ticket complete") && usersAndTickets.containsKey(author) && !usersAndTickets.get(author).isEmpty()) {
            modMail.sendMessage("**NEW MOD MAIL -> [ticket " + ticketNumber + "]**\nUser: <@" + author.getId() + ">\n" + usersAndTickets.get(author)).queue();
            msg.reply("Ticket sent!").queue();
            usersAndTickets.remove(author);
            ticketNumber++;
            return;
        }

        if (!msg.isFromGuild() && content.equals("ticket cancel")) {
            usersAndTickets.remove(author);
            msg.reply("Cancelled ticket creation.").queue();
            return;
        }

        if (!msg.isFromGuild() && content.equals(("help"))) {
                author.openPrivateChannel().flatMap(channel -> channel.sendMessage("""
                        Use `ticket` to create a new ticket.
                        Use `ticket complete` to close the ticket and send it off to the mod team.
                        Use `ticket cancel` to cancel the ticket.
                        *P.S. This help dialogue does not show up in the ticket, so don't worry! *"""
                        )).queue();
                return;
        }

        if (!msg.isFromGuild() && usersAndTickets.containsKey(author)) {
            List<Message.Attachment> attachments;
            usersAndTickets.put(author, usersAndTickets.get(author) + "\n" + msg.getContentRaw());
            try {
                attachments = msg.getAttachments();
                for (Message.Attachment attachment : attachments) {
                    usersAndTickets.put(author, usersAndTickets.get(author) + "\n" + attachment.getUrl());
                }
            } catch (Exception ignored) {}
        }
    }
}
