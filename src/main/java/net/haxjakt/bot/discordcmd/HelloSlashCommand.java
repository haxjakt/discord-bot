package net.haxjakt.bot.discordcmd;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.haxjakt.bot.annotations.JDAListener;
import net.haxjakt.bot.discordcmd.template.SlashCommandOptionsWrapper;
import net.haxjakt.bot.discordcmd.template.TemplateSlashCommand;

@JDAListener
@SuppressWarnings("unused")
public class HelloSlashCommand extends ListenerAdapter implements TemplateSlashCommand {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("hello")) return;

        event.reply("Hello. SpartanBot@v0.0.4").queue();
    }

    @Override
    public boolean isExperimental() {
        return true;
    }

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getDescription() {
        return "Basic command";
    }

    @Override
    public SlashCommandOptionsWrapper[] getOptions() {
        return new SlashCommandOptionsWrapper[0];
    }
}
