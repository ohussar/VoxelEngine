package com.ohussar.VoxelEngine;
import com.ohussar.VoxelEngine.Models.ModelRenderer;
import com.ohussar.VoxelEngine.Models.RawModel;
import org.lwjgl.opengl.*;

public class Renderer {
    public void prepare() {
        GL11.glClearColor(0.4f, 0.7f, 1.0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(RawModel model) {
        ModelRenderer.render(model);
    }

}
