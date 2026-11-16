package com.elilouve.wingsclassic.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration (type SERVEUR : synchronisée automatiquement vers les clients).
 * Fichier : <monde>/serverconfig/wingsclassic-server.toml
 */
public final class WingsConfig {
    private WingsConfig() {}

    public static final int EASIEST = 960;
    public static final int MODERATE = 1920;
    public static final int HARDEST = 2880;

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> WEAR_OBSTRUCTIONS;
    private static final ForgeConfigSpec.BooleanValue FLIGHT_ENABLED;
    private static final ForgeConfigSpec.BooleanValue LANDING_ASSIST;
    private static final ForgeConfigSpec.BooleanValue BAT_BLOOD_ENABLED;
    private static final ForgeConfigSpec.BooleanValue FAIRY_DUST_ORE;
    private static final ForgeConfigSpec.BooleanValue AMETHYST_ORE;
    private static final ForgeConfigSpec.DoubleValue MIN_SPEED;
    private static final ForgeConfigSpec.DoubleValue MAX_SPEED;
    private static final ForgeConfigSpec.DoubleValue SPRINT_MAX_SPEED;
    private static final ForgeConfigSpec.BooleanValue DEBUG_SPEED;
    private static final ForgeConfigSpec.DoubleValue LIFT;
    private static final ForgeConfigSpec.DoubleValue FALL_REDUCTION;
    private static final ForgeConfigSpec.IntValue DAMAGE_INTERVAL;
    private static final Map<String, Entry> WINGS = new LinkedHashMap<>();

    public static final class Entry {
        final ForgeConfigSpec.BooleanValue enabled;
        final ForgeConfigSpec.IntValue requiredFlightSatiation;
        final ForgeConfigSpec.DoubleValue flyingExertion;
        final ForgeConfigSpec.IntValue requiredLandSatiation;
        final ForgeConfigSpec.DoubleValue landingExertion;
        final ForgeConfigSpec.IntValue durability;
        final int defaultDurability;

        Entry(final ForgeConfigSpec.Builder b, final String name, final int defaultDurability) {
            this.defaultDurability = defaultDurability;
            b.push(name);
            this.enabled = b.comment("false = ces ailes existent mais ne permettent plus de voler")
                .define("enabled", true);
            this.requiredFlightSatiation = b.comment("Niveau de faim minimum pour voler (0-20)")
                .defineInRange("requiredFlightSatiation", 7, 0, 20);
            this.flyingExertion = b.comment("Fatigue ajoutée par déplacement en vol")
                .defineInRange("flyingExertion", 0.001D, 0.0D, 10.0D);
            this.requiredLandSatiation = b.comment("Niveau de faim minimum pour amortir la chute (0-20)")
                .defineInRange("requiredLandSatiation", 2, 0, 20);
            this.landingExertion = b.comment("Fatigue ajoutée à l'atterrissage")
                .defineInRange("landingExertion", 0.08D, 0.0D, 10.0D);
            this.durability = b.comment("Durabilité des ailes")
                .defineInRange("durability", defaultDurability, 0, Short.MAX_VALUE);
            b.pop();
        }
    }

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("Réglages généraux").push("general");
        FLIGHT_ENABLED = b.comment("false = plus aucune aile ne permet de voler").define("flightEnabled", true);
        LANDING_ASSIST = b.comment("Les ailes amortissent la chute (annule les dégâts de chute et ralentit la descente)").define("landingAssist", true);
        BAT_BLOOD_ENABLED = b.comment("Clic droit sur une chauve-souris avec une fiole vide pour obtenir du sang de chauve-souris").define("batBloodEnabled", true);
        b.pop();
        b.comment("Génération des minerais (s'applique aux nouveaux chunks générés)").push("worldgen");
        FAIRY_DUST_ORE = b.define("generateFairyDustOre", true);
        AMETHYST_ORE = b.define("generateAmethystOre", true);
        b.pop();
        b.comment("Physique du vol (valeurs d'origine du mod)").push("flightPhysics");
        MIN_SPEED = b.comment("Vitesse minimale ajoutée à chaque tick").defineInRange("minSpeed", 0.03D, 0.0D, 1.0D);
        MAX_SPEED = b.comment("Vitesse maximale ajoutée à chaque tick (quand on avance)").defineInRange("maxSpeed", 0.0715D, 0.0D, 1.0D);
        SPRINT_MAX_SPEED = b.comment("Vitesse maximale en vol quand on sprinte (en blocs par tick ; 1.5 = élytre à pleine vitesse avec feux d'artifice, soit ~30 blocs/s). 0 = le sprint n'accélère pas plus.").defineInRange("sprintMaxSpeed", 1.5D, 0.0D, 5.0D);
        DEBUG_SPEED = b.comment("Diagnostic : affiche au-dessus de la barre d'objets la vitesse réelle en vol et l'état du sprint").define("debugSpeed", false);
        LIFT = b.comment("Poussée vers le haut").defineInRange("lift", 0.05D, 0.0D, 1.0D);
        FALL_REDUCTION = b.comment("Facteur appliqué à la vitesse de chute quand les ailes amortissent (1 = aucun effet)").defineInRange("fallReduction", 0.9D, 0.0D, 1.0D);
        DAMAGE_INTERVAL = b.comment("Toutes les N ticks de vol, les ailes perdent 1 de durabilité (20 ticks = 1 seconde)").defineInRange("damageInterval", 20, 1, 24000);
        b.pop();
        WEAR_OBSTRUCTIONS = b.comment(
            "Objets qui empêchent de porter des ailes (identifiants), vérifiés dans les emplacements de plastron et Curios « dos ».",
            "Par défaut l'élytre : on ne peut pas équiper des ailes tant qu'on en porte une."
        ).defineListAllowEmpty("wearObstructions", List.of("minecraft:elytra"), o -> o instanceof String s && ResourceLocation.tryParse(s) != null);
        b.push("wings");
        add(b, "angel_wings", EASIEST);
        add(b, "slime_wings", EASIEST);
        add(b, "blue_butterfly_wings", EASIEST);
        add(b, "monarch_butterfly_wings", EASIEST);
        add(b, "fire_wings", MODERATE);
        add(b, "bat_wings", MODERATE);
        add(b, "fairy_wings", EASIEST);
        add(b, "evil_wings", MODERATE);
        add(b, "dragon_wings", HARDEST);
        b.pop();
        SPEC = b.build();
    }

    private static void add(final ForgeConfigSpec.Builder b, final String name, final int durability) {
        WINGS.put(name, new Entry(b, name, durability));
    }

    // --- Accès sûrs : la config serveur n'est lisible qu'une fois un monde chargé ---

    private static boolean on(final ForgeConfigSpec.BooleanValue v) {
        return !SPEC.isLoaded() || v.get();
    }

    private static double num(final ForgeConfigSpec.DoubleValue v, final double fallback) {
        return SPEC.isLoaded() ? v.get() : fallback;
    }

    public static boolean flightEnabled(final String name) {
        final Entry e = WINGS.get(name);
        return on(FLIGHT_ENABLED) && (!SPEC.isLoaded() || e == null || e.enabled.get());
    }

    public static boolean landingAssist() { return on(LANDING_ASSIST); }
    public static boolean batBloodEnabled() { return on(BAT_BLOOD_ENABLED); }
    public static boolean fairyDustOre() { return on(FAIRY_DUST_ORE); }
    public static boolean amethystOre() { return on(AMETHYST_ORE); }
    public static float minSpeed() { return (float) num(MIN_SPEED, 0.03D); }
    public static float maxSpeed() { return (float) num(MAX_SPEED, 0.0715D); }
    public static float sprintMaxSpeed() { return (float) num(SPRINT_MAX_SPEED, 1.5D); }
    public static boolean debugSpeed() { return SPEC.isLoaded() && DEBUG_SPEED.get(); }
    public static float lift() { return (float) num(LIFT, 0.05D); }
    public static float fallReduction() { return (float) num(FALL_REDUCTION, 0.9D); }
    public static int damageInterval() { return SPEC.isLoaded() ? DAMAGE_INTERVAL.get() : 20; }

    public static int requiredFlightSatiation(final String name) {
        final Entry e = WINGS.get(name);
        return SPEC.isLoaded() && e != null ? e.requiredFlightSatiation.get() : 7;
    }

    public static float flyingExertion(final String name) {
        final Entry e = WINGS.get(name);
        return SPEC.isLoaded() && e != null ? e.flyingExertion.get().floatValue() : 0.001F;
    }

    public static int requiredLandSatiation(final String name) {
        final Entry e = WINGS.get(name);
        return SPEC.isLoaded() && e != null ? e.requiredLandSatiation.get() : 2;
    }

    public static float landingExertion(final String name) {
        final Entry e = WINGS.get(name);
        return SPEC.isLoaded() && e != null ? e.landingExertion.get().floatValue() : 0.08F;
    }

    public static int durability(final String name, final int fallback) {
        final Entry e = WINGS.get(name);
        return SPEC.isLoaded() && e != null ? e.durability.get() : fallback;
    }

    public static List<String> wearObstructions() {
        if (!SPEC.isLoaded()) {
            return List.of("minecraft:elytra");
        }
        final List<String> out = new ArrayList<>();
        for (final String s : WEAR_OBSTRUCTIONS.get()) {
            out.add(s);
        }
        return out;
    }
}
