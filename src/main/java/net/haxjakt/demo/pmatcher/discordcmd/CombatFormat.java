package net.haxjakt.demo.pmatcher.discordcmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.haxjakt.demo.pmatcher.matchers.BattleReportMatcher;
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

//         acknowledge - if it takes more than 3 seconds
        event.deferReply().queue();

        // get the non-formatted rapport
        String payload = Objects.requireNonNull(event.getOption("raport")).getAsString();
        sLogger.info("Slash-command payload:" + payload.substring(0, Integer.min(payload.length(), 10)));

        event.getHook().sendMessage(payload).queue();
    }

    private MessageEmbed createEmbedded() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Raport de Lupta");
        eb.setColor(0x0099FF);

        eb.addField("`Player 1        vs        Player 2`", "`                             `", false);

        // this doesn't seem to work on mobile
//        eb.addField("Unitati", "1.\n2.\n3.\n4.", true);
//        eb.addField("Player1", "-\n-\n-\n4(-4)", true);
//        eb.addField("Player2", "10(-0)\n10(-0)\n10(-0)\n-", true);

        final String tab = String.valueOf((char)9).repeat(10);

        eb.addField("`         Player1     Player2`",
                "<:kappa:744989726242767062>...............`0`............`0`\n" +
                      "<:kappa:744989726242767062>.................`0`............`0`\n", false);

        return eb.build();
    }

}
