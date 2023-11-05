package net.haxjakt.demo.pmatcher.discordcmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CombatFormat extends ListenerAdapter {

    private static final String COMMAND_NAME = "format";
    private static final Logger sLogger = LoggerFactory.getLogger(CombatFormat.class);
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(COMMAND_NAME)) return;
        sLogger.info("Received format slash-command");
        event.deferReply().queue();

        event.getChannel().sendMessage(Objects.requireNonNull(event.getOption("raport")).getAsString()).queue();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("raport").addField("",
                        """
                                ```
                                012345678912345    012345678912345
                                [12345]        -vs-        [12345]
                                012345678912345    012345678912345
                                ==================================
                                0(-7)..........zid...............x
                                x.............gigant........18(-0)
                                x.............berbec.........9(-0)
                                ----------------------------------
                                ```
                                """, false);
        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

}
