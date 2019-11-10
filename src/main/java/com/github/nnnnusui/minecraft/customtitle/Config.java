package com.github.nnnnusui.minecraft.customtitle;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config{
    public static final String Category_option = "option";

    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue compile;
//    public static ForgeConfigSpec.BooleanValue LimitedImport;

    static {
        setupConfig();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupConfig() {
        CLIENT_BUILDER.push(Category_option);

        compile = CLIENT_BUILDER.comment("")
                .define("compile", false);
//        LimitedImport = CLIENT_BUILDER.comment("WARNING")
//                .define("limited import", true);

        CLIENT_BUILDER.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }


}
//class GuiConfigFactory implements IModGuiFactory{
//    @Override
//    public void initialize(Minecraft minecraftInstance) {
//
//    }
//
//    @Override
//    public boolean hasConfigGui() {
//        return false;
//    }
//
//    @Override
//    public Screen createConfigGui(Screen parentScreen) {
//        return null;
//    }
//
//    @Override
//    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
//        return null;
//    }
//}
