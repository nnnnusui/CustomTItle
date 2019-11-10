package customtitle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRender;
import net.minecraftforge.fml.loading.FMLLoader;

/*
 * ロード用のclass
 * config/customtitle/Test.class として配置する。
 */
public class Test {
    @Override
    public String toString(){
        return String.join(" | ", new String[]{
                    String.format("%s (%s mods, %s)", getGameDirName(), getModsCount(), getVersion())
                   ,String.format("fps: %d (%d chunk updates)", getDebugFPS(), ChunkRender.renderChunksUpdated)
//                "test1"
//                ,getVersion()
                });
    }
    private String  getVersion()    { return Minecraft.getInstance().getVersion(); }
    private String  getGameDirName(){ return Minecraft.getInstance().gameDir.toPath().getFileName().toString(); }
    private int     getDebugFPS()   { return Minecraft.getDebugFPS(); }
    private int     getModsCount()  { return FMLLoader.getLoadingModList().getMods().size(); }
}
