package net.haxjakt.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.haxjakt.bot.annotations.JDAListener;
import net.haxjakt.bot.discordcmd.template.TemplateSlashCommand;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;


public class BotApplication {

    private static final Logger sLogger = LoggerFactory.getLogger(BotApplication.class);
    private static JDA sJDA;

    public static void main(String[] args) {
        if (args.length < 1) {
            sLogger.error("This application needs the discord token as an argument");
            return;
        }
        String discordToken = args[0];

        initJDA(discordToken);
        setUpSlashCommand();
        registerListeners();
    }

    private static void initJDA(final String token) {
        try {
            sJDA = JDABuilder
                    .createDefault(token,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MEMBERS)
                    .disableCache(
                            CacheFlag.VOICE_STATE,
                            CacheFlag.EMOJI,
                            CacheFlag.STICKER,
                            CacheFlag.SCHEDULED_EVENTS)
                    .build().awaitReady();
        } catch (InterruptedException e) {
            sLogger.error("JDA initlialization interrupted. Exiting...");
            throw new RuntimeException(e);
        }
    }

    private static void registerListeners() {
        var listeners = new Reflections("net.haxjakt.bot.discordcmd").getTypesAnnotatedWith(JDAListener.class);
        Object[] instances = listeners.stream()
                .map(BotApplication::instantiate)
                .filter(Objects::nonNull)
                .peek(instance -> sLogger.debug("Registering: " + instance.getClass().getName()))
                .toArray(Object[]::new);
        sJDA.addEventListener(instances);
        sLogger.info("Registered " + instances.length + " JDA listeners");
    }

    private static Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Throwable t) {
            sLogger.error("Could not instantiate JDA Listener: " + clazz.getCanonicalName());
        }
        return null;
    }

    private static void setUpSlashCommand() {
        sLogger.info("Adding slash-commands for guild");
        sJDA.updateCommands().addCommands(
                Commands.slash("hello", "Comanda basic"),
                Commands.slash("time", "Calculeaza timpul necesar pentru a transporta trupe")
                        .addOption(OptionType.STRING, "troops", "Tipul de unitate", true)
                        .addOption(OptionType.STRING, "coord1", "Coordonatele primei locatii", false)
                        .addOption(OptionType.STRING, "coord2", "Coordonatele celei de-a 2-a locatii", false)
                        .addOption(OptionType.INTEGER, "port", "Nivelul combinat al portului", false)
        ).queue();
        sLogger.info("Added slash commands for guild");

        Guild test = sJDA.getGuilds().stream().filter(guild -> guild.getName().equals("Test Server For JDA Bot")).findFirst().orElse(null);
        if (test == null) return;

        test.updateCommands().addCommands(
                Commands.slash("format", "Formateaza raportul de lupta")
                        .addOption(OptionType.STRING, "raport", "Textul raportului copiat din joc", true)
        ).queue();
    }

    private static void updateSlashCommands() {
        Set<Class<? extends TemplateSlashCommand>> classList = new Reflections("net.haxjakt.bot.discordcmd")
                .getSubTypesOf(TemplateSlashCommand.class);

        // experimental commands
        var experimentalCommands = classList.stream()
                .map(BotApplication::instantiate)
                .filter(Objects::nonNull)
                .map(object -> (TemplateSlashCommand) object)
                .filter(TemplateSlashCommand::isExperimental)
//                .peek(instance -> System.out.println("name=" + instance.getName() + " desc=" + instance.getDescription()))
                .toList();

        Guild test = sJDA.getGuilds().stream()
                .filter(guild -> guild.getName().equals("Test Server For JDA Bot")).findFirst().orElse(null);
        if (test == null) {
            sLogger.error("No test guild found");
            return;
        }

        var updateAction = test.updateCommands();
        sLogger.info("Experimental implementations of templateSlashCommand: " + experimentalCommands.size());
        for (var command : experimentalCommands) {
            sLogger.info("Updating experimental command:" + command.getName() + " (" + command.getClass() + ")");
            updateAction = updateAction.addCommands(Commands.slash(command.getName(), command.getDescription()));
        }
        updateAction.queue();
    }
}