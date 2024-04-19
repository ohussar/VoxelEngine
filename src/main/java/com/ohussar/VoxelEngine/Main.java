package com.ohussar.VoxelEngine;

import com.ohussar.VoxelEngine.Blocks.BlockTypes;
import com.ohussar.VoxelEngine.Blocks.Chunk;
import com.ohussar.VoxelEngine.Entities.Camera;
import com.ohussar.VoxelEngine.Entities.Cube;
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
import com.ohussar.VoxelEngine.Blocks.Block;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FRAMERATE = 60;

    public static MemoryLoader StaticLoader = null;
    public static StaticShader StaticShader = null;

    static List<Entity> entities = new ArrayList<>();

    public static void main(String[] args) {
        createDiplay();

        MemoryLoader loader = new MemoryLoader();
        StaticLoader = loader;
        StaticShader shader = new StaticShader();
        StaticShader = shader;
        Renderer renderer = new Renderer(shader);

        RawModel model = loader.loadToVAO(Cube.vertices, Cube.indices, Cube.uv);
        ModelTexture texture = new ModelTexture(MemoryLoader.loadTexture("dirtTex"));
        TexturedModel texModel = new TexturedModel(model, texture);
        Entity entity = new Entity(texModel, new Vector3f(0, 2, 0), new Vector3f(0, 0,0), 1);
        entities.add(entity);
        Chunk chunk = new Chunk();
        List<Block> blocks = new ArrayList<>();
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                for(int z = 0; z < 8; z++){
                    blocks.add(new Block(BlockTypes.DIRT, new Vector3f(x, y, z)));
                }
            }
        }
        chunk.setBlockList(blocks);
        chunk.buildMesh();
        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
        System.out.println(chunk.getVertices().size() * 3);
        while(!Display.isCloseRequested()){
            renderer.prepare();
            camera.move();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.renderChunk(chunk, shader, texModel);
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