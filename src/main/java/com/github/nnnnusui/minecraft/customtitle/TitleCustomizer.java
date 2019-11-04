package com.github.nnnnusui.minecraft.customtitle;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class TitleCustomizer{
    private ResourceGetter getter = new ResourceGetter();
    private MainWindow window = Minecraft.getInstance().mainWindow;

    public TitleCustomizer(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        GLFW.glfwSetWindowTitle(window.getHandle()
                ,String.join(" | ", new String[]{
                    String.format("%s (%s mods, %s)", getter.getGameDirName(), getter.getModsCount(), getter.getVersion())
                   ,String.format("fps: %d (%d chunk updates)", getter.getDebugFPS(), ChunkRender.renderChunksUpdated)
                }));
    }
}
