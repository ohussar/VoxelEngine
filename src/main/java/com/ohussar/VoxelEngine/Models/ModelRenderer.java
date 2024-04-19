package com.ohussar.VoxelEngine.Models;

import com.ohussar.VoxelEngine.Entities.Entity;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import com.ohussar.VoxelEngine.Util.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

public class ModelRenderer {

    public static void render(Entity entity, StaticShader shader) {
        GL30.glBindVertexArray(entity.getModel().getModel().vaoID);
        GL20.glEnableVertexAttribArray(0); //position
        GL20.glEnableVertexAttribArray(1); //uv

        Matrix4f transformMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());

        shader.loadTransformationMatrix(transformMatrix);
        //GL11.glDrawArrays(); <- use this without indices ( for chunk mesh )
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());

        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().vertexCount, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }


}
