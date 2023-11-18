package net.haxjakt.bot.discordcmd.template;

public interface TemplateSlashCommand {

    boolean isExperimental();
    String getName();
    String getDescription();
    SlashCommandOptionsWrapper[] getOptions();

}
