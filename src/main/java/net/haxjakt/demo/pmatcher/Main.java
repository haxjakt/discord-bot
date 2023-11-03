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

    private static final String BOT_TOKEN = "MTE2Nzc5OTk4NjYwMDI5NjUzOA.GsDNA9.4SYQ5mK-kllYA2NTiBUb1IJnxtTUGhWCOeP40M";
    private static final Logger sLogger = LoggerFactory.getLogger(Main.class);
    private static void initTest() {
        BattleReportMatcher brm = new BattleReportMatcher();
        var result = brm.toMap("Batalie pentru W-P-1 (26.10.2023 15:50:00) Unknown[V] din Vin 1 vs haxjakt[SPA] din W-P-1  --------------------------------------------------------------------------------------------- Phalanx...............................304(-5) - Phalanx................................0(-30) ............................................. - Zid.....................................1(-4) Spadasin...............................90(-0) - ............................................. Berbec.................................12(-0) - ............................................. Catapulta..............................12(-0) - ............................................. --------------------------------------------------------------------------------------------- Generali...................................-7 - Generali..................................-42 Puncte de atac............................105 - Puncte de aparare........................17.5 Daune Primite.............................350 - Daune Primite............................2100 Procent Daune.............................14% - Procent Daune.............................86% ---------------------------------------------------------------------------------------------  Castigatori: Unknown[V]  Pierzatori: haxjakt[SPA]  Atacatorul a castigat pentru ca a reusit sa sparga zidul iar aparatorul nu a avut unitati pentru a-l apara.");
        result.entrySet().forEach(System.out::println);
    }
    public static void main(String[] args) {
        initTest();
//        JDA jda = JDABuilder.createDefault(BOT_TOKEN,
//                GatewayIntent.GUILD_MESSAGES,
//                GatewayIntent.MESSAGE_CONTENT,
//                GatewayIntent.GUILD_MEMBERS).build();
//
//        setUpSlashCommand(jda);
//        jda.addEventListener(new CombatFormat());
    }

    private static void somethign() {
        Button.success("s", "s");
    }
    private static void setUpSlashCommand(final JDA jda) {
        sLogger.info("Adding slash-commands for guild");
        jda.updateCommands().addCommands(
                Commands.slash("format", "Formateaza raportul de lupta")
                        .addOption(OptionType.STRING, "raport", "Textul raportului copiat din joc", true)
        ).queue();
    }
}