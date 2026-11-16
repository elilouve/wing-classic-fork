package com.elilouve.wingsclassic.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Configuration côté joueur (fichier config/wingsclassic-client.toml). */
public final class ClientConfig {
    private ClientConfig() {}

    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.BooleanValue RENDER_WINGS;
    private static final ForgeConfigSpec.BooleanValue FLIGHT_POSE;
    private static final ForgeConfigSpec.BooleanValue CAMERA_ROLL;
    private static final ForgeConfigSpec.DoubleValue SOUND_VOLUME;
    private static final ForgeConfigSpec.DoubleValue WING_PHYSICS;
    private static final ForgeConfigSpec.BooleanValue FIRE_GLOW;
    private static final ForgeConfigSpec.BooleanValue FIRE_PARTICLES;

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        RENDER_WINGS = b.comment("Afficher les ailes dans le dos des joueurs et des mobs").define("renderWings", true);
        FLIGHT_POSE = b.comment("Mettre le corps à l'horizontale (et bras/jambes/tête) pendant le vol").define("flightPose", true);
        CAMERA_ROLL = b.comment("Légère inclinaison de la caméra pendant les virages en vol").define("cameraRoll", true);
        SOUND_VOLUME = b.comment("Volume du bruit de vent des ailes (0 = muet, 1 = normal)").defineInRange("windSoundVolume", 1.0D, 0.0D, 2.0D);
        WING_PHYSICS = b.comment("Intensité du balancement des ailes à la marche, la course et les sauts (0 = ailes figées, 1 = normal)").defineInRange("wingPhysics", 1.0D, 0.0D, 3.0D);
        FIRE_GLOW = b.comment("Les ailes de feu brillent quand elles se déploient").define("fireWingsGlow", true);
        FIRE_PARTICLES = b.comment("Les ailes de feu dégagent des particules de flammes en vol").define("fireWingsParticles", true);
        SPEC = b.build();
    }

    public static boolean renderWings() { return !SPEC.isLoaded() || RENDER_WINGS.get(); }
    public static boolean flightPose() { return !SPEC.isLoaded() || FLIGHT_POSE.get(); }
    public static boolean cameraRoll() { return !SPEC.isLoaded() || CAMERA_ROLL.get(); }
    public static float wingPhysics() { return SPEC.isLoaded() ? WING_PHYSICS.get().floatValue() : 1.0F; }
    public static boolean fireGlow() { return !SPEC.isLoaded() || FIRE_GLOW.get(); }
    public static boolean fireParticles() { return !SPEC.isLoaded() || FIRE_PARTICLES.get(); }
    public static float soundVolume() { return SPEC.isLoaded() ? SOUND_VOLUME.get().floatValue() : 1.0F; }
}
