package com.ohussar.VoxelEngine;

import com.ohussar.VoxelEngine.Entities.Camera;
import com.ohussar.VoxelEngine.Entities.Entity;
import com.ohussar.VoxelEngine.Models.RawModel;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import com.ohussar.VoxelEngine.Textures.ModelTexture;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

public class Main {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FRAMERATE = 60;

    public static MemoryLoader StaticLoader = null;
    public static StaticShader StaticShader = null;
    public static void main(String[] args) {
        createDiplay();

        MemoryLoader loader = new MemoryLoader();
        StaticLoader = loader;
        StaticShader shader = new StaticShader();
        StaticShader = shader;
        Renderer renderer = new Renderer(shader);
        float[] vertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f
        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22
        };
        float[] uv = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };
        RawModel model = loader.loadToVAO(vertices, indices, uv);
        ModelTexture texture = new ModelTexture(loader.loadTexture("dirtTex"));
        TexturedModel m = new TexturedModel(model, texture);
        Entity entity = new Entity(m, new Vector3f(0, 0,-1), new Vector3f(0, 0, 0), 1);
        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
        while(!Display.isCloseRequested()){
            renderer.prepare();
            camera.move();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);
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
            //Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attributes);
            Display.setTitle("Voxel Engine");
            Display.setResizable(false);
            Display.setFullscreen(true);
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
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
                Mouse.setGrabbed(!Keyboard.isKeyDown(Keyboard.KEY_E));

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