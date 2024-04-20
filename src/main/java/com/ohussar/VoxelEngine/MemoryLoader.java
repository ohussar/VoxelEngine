package com.ohussar.VoxelEngine;

import com.ohussar.VoxelEngine.Models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class MemoryLoader {

    static List<Integer> vaos = new ArrayList<>();
    static List<Integer> vbos = new ArrayList<>();
    static List<Integer> textures = new ArrayList<>();




    public RawModel loadToVAO(float[] vertices, int[] indices, float[] uv) {
        int vaoID = createVAO();
        storeDataInAttributeList(vertices, 0, 3);
        storeDataInAttributeList(uv, 1, 2);
        bindIndicesBuffer(indices);
        GL30.glBindVertexArray(0);
        return new RawModel(vaoID, indices.length);
    }

    public int updateVAO(int VAO, float[] vertices, float[] uv){
        if(VAO == -1){
            VAO = createVAO();
        }else{
            GL30.glDeleteVertexArrays(VAO);
            VAO = createVAO();
        }
        storeDataInAttributeList(vertices, 0, 3);
        storeDataInAttributeList(uv, 1, 2);
        GL30.glBindVertexArray(0);
        return VAO;
    }


    public static int loadTexture(String filename){
        Texture texture;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("src/main/resources/" + filename + ".PNG"));
            int textureID = texture.getTextureID();
            textures.add(textureID);
            return textureID;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    };

    private int createVAO() {

        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(float[] data, int attributeNumber, int dimentions) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = this.toFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // binding the buffer
        GL20.glVertexAttribPointer(attributeNumber, dimentions, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbinding the buffer
    }

    private void bindIndicesBuffer(int[] indices){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = toIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer toIntBuffer(int data[]){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer toFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {
        for(int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int tex : textures){
            GL11.glDeleteTextures(tex);
        }
    }
}
