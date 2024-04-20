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
import com.ohussar.VoxelEngine.Util.Vec3i;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import com.ohussar.VoxelEngine.Blocks.Block;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FRAMERATE = 60;

    public static MemoryLoader StaticLoader = null;
    public static StaticShader StaticShader = null;

    static List<Entity> entities = new ArrayList<>();
    static Map<Vec3i, Chunk> chunks = new HashMap<Vec3i, Chunk>();
    public static void main(String[] args) {
        createDiplay();

        StaticLoader = new MemoryLoader();
        StaticShader = new StaticShader();
        Renderer renderer = new Renderer(StaticShader);
        Chunk chunk = new Chunk(new Vector3f(0, 0, 0));
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
        chunks.put(new Vec3i(chunk.getPosition()), chunk);
        boolean pressed = false;
        while(!Display.isCloseRequested()){
            renderer.prepare();
            camera.move();
            StaticShader.start();
            StaticShader.loadViewMatrix(camera);
            for(Chunk c : chunks.values()){
                renderer.renderChunk(c, StaticShader);
            }

            StaticShader.stop();
            if(Keyboard.isKeyDown(Keyboard.KEY_Q) && !pressed){
                pressed = true;
                Vector3f pos = new Vector3f((int)Math.floor(camera.getPosition().x), (int)Math.floor(camera.getPosition().y), (int)Math.floor(camera.getPosition().z));

                int chunkx = (int) Math.floor(pos.x/16);
                int chunkz = (int) Math.floor(pos.z/16);
                Vec3i p = new Vec3i(chunkx, 0, chunkz);
                if(!chunks.containsKey(p)){
                    Chunk c = new Chunk(p.toVec3f());
                    chunks.put(p, c);
                }
                Chunk chunk1 = chunks.get(p);

                chunk1.addBlockToChunk(new Block(BlockTypes.DIRT, pos));
                chunk1.buildMesh();
            }
            if(!Keyboard.isKeyDown(Keyboard.KEY_Q)){
                pressed = false;
            }
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