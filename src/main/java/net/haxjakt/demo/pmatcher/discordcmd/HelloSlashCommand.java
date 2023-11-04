package net.haxjakt.demo.pmatcher.discordcmd;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloSlashCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("hello")) return;

        event.reply("Hello. SpartanBot@v0.0.1").queue();
    }
}
