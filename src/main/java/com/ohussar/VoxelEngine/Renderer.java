package com.ohussar.VoxelEngine;
import com.ohussar.VoxelEngine.Entities.Player;
import com.ohussar.VoxelEngine.World.Blocks.BlockTypes;
import com.ohussar.VoxelEngine.Entities.Entity;
import com.ohussar.VoxelEngine.Models.ModelRenderer;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import com.ohussar.VoxelEngine.Util.Maths;
import com.ohussar.VoxelEngine.Util.Util;
import com.ohussar.VoxelEngine.World.ChunkMeshData;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import com.ohussar.VoxelEngine.World.Chunk;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;

public class Renderer {

    Matrix4f projectionMatrix;
    public static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 10000f;
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
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
    }

    public void render(Entity entity, StaticShader shader) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        ModelRenderer.render(entity, shader);
    }

    public void renderPlayer(Player player, StaticShader shader){

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        GL30.glBindVertexArray(player.getVAO());
        GL20.glEnableVertexAttribArray(0); //position
        GL20.glEnableVertexAttribArray(1); //uv
        Matrix4f transformMatrix = Maths.createTransformationMatrix(player.position, new Vector3f(0, 45, 0), 1);
        shader.loadTransformationMatrix(transformMatrix);
        //GL11.glDrawArrays(); <- use this without indices ( for chunk mesh )
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, BlockTypes.atlas.getTextureID());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, player.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void renderChunk(Chunk chunk, StaticShader shader, Matrix4f toShadowMapSpace){
        //shader.loadToShadowMapSpaceMatrix(toShadowMapSpace);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, BlockTypes.atlas.getTextureID());
        Vector3f p = new Vector3f(chunk.getPosition().x*16, chunk.getPosition().y*16, chunk.getPosition().z*16);
        Matrix4f transformMatrix = Maths.createTransformationMatrix(p, Util.EmptyVec3(), 1);
        shader.loadTransformationMatrix(transformMatrix);

        for(Map.Entry<Byte, ChunkMeshData> data : chunk.getChunkMeshes().entrySet()){

            GL30.glBindVertexArray(data.getValue().VAO);
            GL20.glEnableVertexAttribArray(0); //position
            GL20.glEnableVertexAttribArray(1); //uv
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0,data.getValue().vertices.size() * 3);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
        }
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
