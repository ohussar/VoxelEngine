package com.ohussar.VoxelEngine;
import com.ohussar.VoxelEngine.Entities.Entity;
import com.ohussar.VoxelEngine.Models.ModelRenderer;
import com.ohussar.VoxelEngine.Models.RawModel;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

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
