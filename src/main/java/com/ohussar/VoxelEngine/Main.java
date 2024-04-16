package com.ohussar.VoxelEngine;

import com.ohussar.VoxelEngine.Models.RawModel;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import com.ohussar.VoxelEngine.Textures.ModelTexture;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

public class Main {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FRAMERATE = 60;

    public static MemoryLoader StaticLoader = null;
    public static StaticShader StaticShader = null;
    public static void main(String[] args) {
        createDiplay();
        Renderer renderer = new Renderer();
        MemoryLoader loader = new MemoryLoader();
        StaticLoader = loader;
        StaticShader shader = new StaticShader();
        StaticShader = shader;
        float[] vertices = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
             0.5f, -0.5f, 0,
             0.5f, 0.5f, 0,
        };

        int[] indices = {
            0, 1, 2,
            2, 3, 0
        };
        float[] uv = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };
        RawModel model = loader.loadToVAO(vertices, indices, uv);
        ModelTexture texture = new ModelTexture(loader.loadTexture("dirtTex"));
        TexturedModel m = new TexturedModel(model, texture);

        while(!Display.isCloseRequested()){
            renderer.prepare();
            shader.start();
            renderer.render(m);
            shader.stop();
            updateDisplay();
        }
        closeDisplay();
    }

    public static void createDiplay(){
        ContextAttribs attributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attributes);
            Display.setTitle("Voxel Engine");
            Display.setResizable(false);
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateDisplay(){
        Display.sync(FRAMERATE);
        Display.update();

        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()){
                if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                    closeDisplay();
                }
            }
        }

    }
    public static void closeDisplay(){
        StaticLoader.cleanUp();
        StaticShader.clean();
        Display.destroy();
        System.exit(0);
    }

}