package net.haxjakt.demo.pmatcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.haxjakt.demo.pmatcher.discordcmd.CombatFormat;
import net.haxjakt.demo.pmatcher.matchers.BattleReportMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger sLogger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        if (args.length < 1) return;
        JDA jda = JDABuilder.createDefault(args[0],
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS).build();

        setUpSlashCommand(jda);
        jda.addEventListener(new CombatFormat());
    }
    private static void setUpSlashCommand(final JDA jda) {
        sLogger.info("Adding slash-commands for guild");
        jda.updateCommands().addCommands(
                Commands.slash("format", "Formateaza raportul de lupta")
                        .addOption(OptionType.STRING, "raport", "Textul raportului copiat din joc", true)
        ).queue();
    }
}