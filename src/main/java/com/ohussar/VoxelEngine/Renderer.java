package com.ohussar.VoxelEngine;
import com.ohussar.VoxelEngine.Blocks.Block;
import com.ohussar.VoxelEngine.Blocks.BlockTypes;
import com.ohussar.VoxelEngine.Entities.Entity;
import com.ohussar.VoxelEngine.Models.ModelRenderer;
import com.ohussar.VoxelEngine.Models.RawModel;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import com.ohussar.VoxelEngine.Util.Maths;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import com.ohussar.VoxelEngine.Blocks.Chunk;
import org.lwjgl.util.vector.Vector3f;

public class Renderer {

    Matrix4f projectionMatrix;
    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 10000f;


    public Renderer(StaticShader shader){
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0.4f, 0.7f, 1.0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Entity entity, StaticShader shader) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        ModelRenderer.render(entity, shader);
    }

    public void renderChunk(Chunk chunk, StaticShader shader, TexturedModel model){
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL30.glBindVertexArray(chunk.VAO);
        GL20.glEnableVertexAttribArray(0); //position
        GL20.glEnableVertexAttribArray(1); //uv

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());

        Matrix4f transformMatrix = Maths.createTransformationMatrix(new Vector3f(5, 0, 5), new Vector3f(0, 0, 0), 1);
        shader.loadTransformationMatrix(transformMatrix);
        // <- use this without indices ( for chunk mesh )

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, chunk.getVertices().size() * 3);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void createProjectionMatrix(){

        projectionMatrix = new Matrix4f();

        float ratio = (float)Display.getWidth() / (float)Display.getHeight();
        float yscale = 1f / (float)Math.tan(Math.toRadians(FOV) / 2f);
        float xscale = yscale / ratio;
        float zp = FAR_PLANE + NEAR_PLANE;
        float zm = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = xscale;
        projectionMatrix.m11 = yscale;
        projectionMatrix.m22 = -zp/zm;
        projectionMatrix.m32 = -(2*FAR_PLANE*NEAR_PLANE)/zm;
        projectionMatrix.m23 = -1;
        projectionMatrix.m33 = 0;
    }
    /* projection matrix
        x scale   0         0         0
        0         y scale   0         0
        0         0         -zp/zm    -(2*ZFar*ZNear)/zm
        0         0         -1        0
        aspeactratio = width / height
        yscale = 1/tan(FOV/2)
        xscale = yscale / aspectRatio
        zfar = far plane dist
        znear = near plane dist
        zp = zfar + znear;
        zm = zfar - znear;
     */

}
