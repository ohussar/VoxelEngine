package com.ohussar.VoxelEngine.Models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.GLUtils;

public class ModelRenderer {

    public static void render(TexturedModel model) {
        GL30.glBindVertexArray(model.getModel().vaoID);
        GL20.glEnableVertexAttribArray(0); //position
        GL20.glEnableVertexAttribArray(1); //uv
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());

        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().vertexCount, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
