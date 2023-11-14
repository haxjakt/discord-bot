package net.haxjakt.demo.pmatcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.haxjakt.demo.pmatcher.discordcmd.CombatFormat;
import net.haxjakt.demo.pmatcher.discordcmd.HelloSlashCommand;
import net.haxjakt.demo.pmatcher.discordcmd.TravelTimeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger sLogger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        System.out.println("=== DISCORD BOT MAIN ===");
        if (args.length < 1) return;
        JDA jda = JDABuilder.createDefault(args[0],
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS).build();

        setUpSlashCommand(jda);
        jda.addEventListener(new CombatFormat());
        jda.addEventListener(new HelloSlashCommand());
        jda.addEventListener(new TravelTimeCommand());
    }
    private static void setUpSlashCommand(final JDA jda) {
        sLogger.info("Adding slash-commands for guild");
        jda.updateCommands().addCommands(
                Commands.slash("hello", "Comanda basic"),
                Commands.slash("time", "Calculeaza timpul necesar pentru a transporta trupe")
                        .addOption(OptionType.STRING, "coord1", "Coordonatele primei locatii", true)
                        .addOption(OptionType.STRING, "coord2", "Coordonatele celei de-a 2-a locatii", true)
                        .addOption(OptionType.STRING, "troops", "Tipul de unitate", true)
                        .addOption(OptionType.INTEGER, "port", "Nivelul combinat al portului", false)
        ).queue();

        Guild test = jda.getGuilds().stream().filter(guild -> guild.getName().equals("Test Server For JDA Bot")).findFirst().orElse(null);
        if (test == null) return;

        test.updateCommands().addCommands(
                Commands.slash("format", "Formateaza raportul de lupta")
                        .addOption(OptionType.STRING, "raport", "Textul raportului copiat din joc", true)
        ).queue();
    }
}