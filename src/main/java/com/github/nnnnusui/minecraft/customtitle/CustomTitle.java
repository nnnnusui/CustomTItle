package com.github.nnnnusui.minecraft.customtitle;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod("customtitle")
public class CustomTitle {
    // Directly reference a log4j logger.
    static final Logger LOGGER = LogManager.getLogger();

    public CustomTitle() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
//        MinecraftForge.EVENT_BUS.register(this);
        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("customtitle.toml"));
    }

    private void doClientStuff(final FMLClientSetupEvent event) { Loader.load(); }

    public static void main(String[] args) throws Exception{
        System.out.println(System.getProperty("java.class.path"));
//        Path path = Paths.get("G:\\game\\Minecraft\\.minecraft\\libraries");
//        Files.walk(path)
//                .filter(it-> !Files.isDirectory(it))
//                .forEach(it-> System.out.println(it));
    }
}

