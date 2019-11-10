package com.github.nnnnusui.minecraft.customtitle;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class Loader {
    private static final String logPrefix = "mod.CustomTitle: ";
    private static final MainWindow window = Minecraft.getInstance().mainWindow;
    static void load() {
        try {
            new Loader();
        } catch (Exception exception) {
            final String message = "initialize error | " + exception.toString();
            CustomTitle.LOGGER.debug(message);
            GLFW.glfwSetWindowTitle(window.getHandle(), logPrefix + message);
        }
    }

    private final Path rootPath    = Paths.get("config");
    private final Path packagePath = Paths.get(rootPath.toString(), "customtitle");
    private final Path sourcePath = Paths.get(packagePath.toString(), "Test.java");
    private final Path classPath  = Paths.get(packagePath.toString(), "Test.class");
    private Object   customTitle = "";
    private FileTime lastModified;

    private final URL[]       urls               = new URL[]{ rootPath.toUri().toURL() };
    private final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

    private final String[] options
        = new String[]{
            "-classpath"
           ,System.getProperty("java.class.path")
           ,sourcePath.toString()
        };
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private Loader() throws Exception {
        MinecraftForge.EVENT_BUS.register(this);
        reloadInstance();
//        genFile(path);
    }

    // TODO: Change to an appropriate event.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        try {
            if (lastModified.compareTo(Files.getLastModifiedTime(sourcePath)) < 0)
                reloadInstance();
            GLFW.glfwSetWindowTitle(window.getHandle(), customTitle.toString());
        } catch (Exception exception){
            errorLog("reload error | " + exception.toString());
        }
    }

    private void reloadInstance() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, CompileTimeException {
        if (Files.exists(classPath))
            Files.delete(classPath);
        lastModified = Files.getLastModifiedTime(sourcePath);
        compile();
        loadClass();
        CustomTitle.LOGGER.info("reload.");
    }
    private void compile() throws CompileTimeException {
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        compiler.run(null, null, err, options);
        if(err.size() > 0)
            throw new CompileTimeException(err);
    }
    private void loadClass() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        /*mandatory-reload*/URLClassLoader classLoader = new URLClassLoader(urls, contextClassLoader);
        final Class clazz = Class.forName("customtitle.Test", true, classLoader);
        customTitle = clazz.newInstance();
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
    private void errorLog(String message){
        CustomTitle.LOGGER.debug(message);
        customTitle = logPrefix + message;
    }
}
class CompileTimeException extends Exception {
    public CompileTimeException(String message){
        super(message);
    }
    public CompileTimeException(ByteArrayOutputStream stream){
        this(new String(stream.toByteArray()));
    }
}