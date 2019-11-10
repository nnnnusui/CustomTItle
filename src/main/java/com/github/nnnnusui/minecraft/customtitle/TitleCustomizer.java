package com.github.nnnnusui.minecraft.customtitle;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.userdev.ClasspathLocator;
import org.lwjgl.glfw.GLFW;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TitleCustomizer{
    private MainWindow window = Minecraft.getInstance().mainWindow;
    private Object instance;

    public TitleCustomizer(){
        MinecraftForge.EVENT_BUS.register(this);
        try{
            reloadInstance();
            System.out.println("newClazz: " + instance);
        } catch (Exception exception){}
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        GLFW.glfwSetWindowTitle(window.getHandle(), instance.toString());
    }

    private void reloadInstance() throws Exception{
        Path root = Paths.get("config");
        Path path = Paths.get(root.toString(), "customtitle", "Test.java");
//        genFile(path);

        List<String> options = new ArrayList<String>();
        options.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));
        options.add(path.toString());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, options.toArray(new String[0]));

        URL[] urls = new URL[]{ root.toUri().toURL() };
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader classLoader = new URLClassLoader(urls, contextClassLoader);

        Class clazz = Class.forName("customtitle.Test", true, classLoader);
        instance = clazz.newInstance();
    }

    private void genFile(Path path){
//        String source = "package customtitle; public class Test { public String toString() { return \"Test#test()\"; } }";
        try {
            if(!Files.exists(path.getParent()))
                Files.createDirectories(path.getParent());
            if(!Files.exists(path))
                Files.createFile(path);
//            Files.write(path, source.getBytes());
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
}