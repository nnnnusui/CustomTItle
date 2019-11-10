package com.github.nnnnusui.minecraft.customtitle;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLLoader;

import java.nio.file.Path;

public class ResourceGetter {
    public ResourceGetter(){}
    public String getVersion(){
        return Minecraft.getInstance().getVersion();
    }
    public String getGameDirName(){
        Path path = Minecraft.getInstance().gameDir.getAbsoluteFile().toPath();
        return path.getFileName().toString();
    }
    public int getDebugFPS(){
        return Minecraft.getDebugFPS();
    }
    public int getModsCount() { return FMLLoader.getLoadingModList().getMods().size(); }
}