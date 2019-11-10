package com.github.nnnnusui.minecraft.customtitle;

import java.util.Arrays;
import java.util.List;

public class Source {
    public static final Source instance = new Source();
    private Source(){}

    public final String className   = "TitleCustomizer";
    public final String packageName = "customtitle";
    private final String[] imports = new String[]{
            "import java.text.DateFormat;"
           ,"import java.text.SimpleDateFormat;"
           ,"import java.util.Date;"
           ,"import net.minecraft.client.Minecraft;"
           ,"import net.minecraft.client.renderer.chunk.ChunkRender;"
           ,"import net.minecraftforge.fml.loading.FMLLoader;"
    };
    private final String[] fields = new String[]{
            "private final DateFormat dateFormat = new SimpleDateFormat(\"yyyy/MM/dd\");"
    };
    private final String[] methods = new String[]{
            "private String  getVersion()    { return Minecraft.getInstance().getVersion(); }"
           ,"private String  getGameDirName(){ return Minecraft.getInstance().gameDir.toPath().getFileName().toString(); }"
           ,"private int     getDebugFPS()   { return Minecraft.getDebugFPS(); }"
           ,"private int     getModsCount()  { return FMLLoader.getLoadingModList().getMods().size(); }"
           ,"private Date    getDate()       { return new Date(); }"
    };
    private final String[] code = new String[]{
            "return String.join(\" | \", new String[]{"
           ,"            String.format(\"%s (%s mods, %s)\", getGameDirName(), getModsCount(), getVersion())"
           ,"           ,dateFormat.format(getDate())"
           ,"           ,String.format(\"fps: %d (%d chunk updates)\", getDebugFPS(), ChunkRender.renderChunksUpdated)"
           ,"        });"
    };
    public List<String> toSourceCode()             { return buildSourceCode(code); }
    public List<String> toSourceCode(String[] code){ return buildSourceCode(code); }
    private List<String> buildSourceCode(String[] code){
        //@formatter:off
        return Arrays.asList(
            "package "+ packageName +";"
           ,""
           ,String.join("\n", imports)
           ,""
           ,"public class "+ className +" {"
           ,"\t"+ String.join("\n\t", new String[]{
                String.join("\n\t", fields)
               ,""
               ,"@Override"
               ,"public String toString(){"
               ,"\t"+ String.join("\n\t\t", code)
               ,"}"
               ,""
               ,String.join("\n\t", methods)
            })
           ,"}"
        );
    }
}
