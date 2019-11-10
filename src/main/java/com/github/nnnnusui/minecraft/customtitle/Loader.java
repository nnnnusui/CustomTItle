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
import java.util.regex.Pattern;

public class Loader {
    private static final String logPrefix = "mod.CustomTitle: ";
    private static final MainWindow window = Minecraft.getInstance().mainWindow;
    private static Loader instance = null;
    static void load() {
        try {
            if(instance == null)
                instance = new Loader();
        } catch (Exception exception) {
            final String message = "initialize error | " + exception.toString();
            CustomTitle.LOGGER.debug(message);
            GLFW.glfwSetWindowTitle(window.getHandle(), logPrefix + message);
        }
    }
    private final Source source    = Source.instance;
    private final String className = source.packageName +"."+ source.className;
    private final Path rootPath    = Paths.get("config");
    private final Path packagePath = Paths.get(rootPath.toString(), source.packageName.split(Pattern.quote(".")));
    private final Path sourcePath = Paths.get(packagePath.toString(), source.className+".java");
    private final Path classPath  = Paths.get(packagePath.toString(), source.className+".class");
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final String[] options
        = new String[]{
            "-classpath"
           ,System.getProperty("java.class.path")
           ,sourcePath.toString()
        };
    private final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    private final URL[]       urls               = new URL[]{ rootPath.toUri().toURL() };
    private Object   customTitle = "";
    private FileTime lastModified;

    private Loader() throws Exception {
        MinecraftForge.EVENT_BUS.register(this);
        generateFile(sourcePath);
        reloadInstance();
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
        final Class clazz = Class.forName(className, true, classLoader);
        customTitle = clazz.newInstance();
    }

    private void generateFile(Path path){
        try {
            if(Files.notExists(path.getParent()))
                Files.createDirectories(path.getParent());
            if(Files.notExists(path))
                Files.createFile(path);
            Files.write(path, source.toSourceCode()/*, StandardCharsets.UTF_8*/);
        } catch(IOException exception) {
            errorLog("file generate error. | "+ exception.toString());
        }
    }
    private void errorLog(String message){
        CustomTitle.LOGGER.debug(message);
        customTitle = logPrefix + message;
    }
}
