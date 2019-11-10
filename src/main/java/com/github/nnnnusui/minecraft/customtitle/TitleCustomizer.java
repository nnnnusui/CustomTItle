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
import java.util.Arrays;
import java.util.stream.Collectors;

public class TitleCustomizer{
    private ResourceGetter getter = new ResourceGetter();
    private MainWindow window = Minecraft.getInstance().mainWindow;
    private Object instance;
//    static {
//        try {
//            new Test().test();
//        } catch (Exception e) {}
//    }

    public TitleCustomizer(){
        MinecraftForge.EVENT_BUS.register(this);
        try{
            instance = getInstance();
            System.out.println("newClazz: " + instance);
        } catch (Exception exception){}
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        GLFW.glfwSetWindowTitle(window.getHandle()
//                ,String.join(" | ", new String[]{
//                    String.format("%s (%s mods, %s)", getter.getGameDirName(), getter.getModsCount(), getter.getVersion())
//                   ,String.format("fps: %d (%d chunk updates)", getter.getDebugFPS(), ChunkRender.renderChunksUpdated)
//                }));
                ,instance.toString());
    }

    private Object getInstance() throws Exception{
        Path root = Paths.get("config");
        Path path = Paths.get(root.toString(), "customtitle", "Test.java");
//        genFile(path);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

//        compiler.run(null, null, null
////                ,"-classpath", "G:\\game\\Minecraft\\.minecraft\\libraries\\net\\minecraftforge\\forge\\1.14.4-28.1.0\\forge-1.14.4-28.1.0-client.jar;G:\\game\\Minecraft\\.minecraft\\libraries\\net\\minecraftforge\\forgespi\\1.3.0\\forgespi-1.3.0.jar"
//                , path.toString());
        URL[] urls = Arrays.stream(new String[]{
                root.toString()
//                ,"G:\\game\\Minecraft\\.minecraft\\libraries\\net\\minecraftforge\\forge\\1.14.4-28.1.0\\forge-1.14.4-28.1.0-client.jar"
//                ,"G:\\game\\Minecraft\\.minecraft\\libraries\\net\\minecraftforge\\forgespi\\1.3.0\\forgespi-1.3.0.jar"
            }).map(it -> Paths.get(it).toUri())
            .map(this::getURL)
            .toArray(URL[]::new);
        URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
//        ClassLoader classLoader = TitleCustomizer.class.getClassLoader();
//        classLoader.loadClass()
        Class clazz = Class.forName("customtitle.Test", true, classLoader);
        return clazz.newInstance();
    }
    private URL getURL(URI path){
        try {
            return path.toURL();
        } catch (MalformedURLException exception){
            throw new RuntimeException(exception);
        }
    }
    private void genFile(Path path){
//        String source = "package com.github.nnnnusui.minecraft.customtitle; public class Test { public String toString() { return \"Test#test()\"; } }";
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