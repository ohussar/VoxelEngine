package com.ohussar.VoxelEngine;

import com.ohussar.VoxelEngine.World.Blocks.BlockTypes;
import com.ohussar.VoxelEngine.World.Chunk;
import com.ohussar.VoxelEngine.Entities.Camera;
import com.ohussar.VoxelEngine.Entities.Entity;
import com.ohussar.VoxelEngine.Shaders.StaticShader;
import com.ohussar.VoxelEngine.Util.Vec3i;
import com.ohussar.VoxelEngine.World.World;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import com.ohussar.VoxelEngine.World.Blocks.Block;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int FRAMERATE = 60;

    public static MemoryLoader StaticLoader = null;
    public static StaticShader StaticShader = null;

    static List<Entity> entities = new ArrayList<>();
    static Map<Vec3i, Chunk> chunks = new HashMap<Vec3i, Chunk>();

    public static World world;

    public static void main(String[] args) {
        createDiplay();

        StaticLoader = new MemoryLoader();
        StaticShader = new StaticShader();
        BlockTypes.registerBlocks();
        BlockTypes.registerUvGetters();
        Renderer renderer = new Renderer(StaticShader);
        Camera camera = new Camera(new Vector3f(0, 80, 0), new Vector3f(0, 0, 0));
        boolean pressedq = false;
        boolean pressedr = false;

        world = new World();

        while(!Display.isCloseRequested()){
            renderer.prepare();
            camera.tick();
            StaticShader.start();
            StaticShader.loadViewMatrix(camera);
            world.tick(camera);

            for(Chunk chunk : world.getLoadedChunks()){
                renderer.renderChunk(chunk, StaticShader);
            }

            StaticShader.stop();
//            if(Keyboard.isKeyDown(Keyboard.KEY_R) && !pressedr){
//                pressedr = true;
//                Vector3f pos = new Vector3f((int)Math.floor(camera.getPosition().x), (int)Math.floor(camera.getPosition().y), (int)Math.floor(camera.getPosition().z));
//                int chunkx = (int) Math.floor(pos.x/16);
//                int chunkz = (int) Math.floor(pos.z/16);
//
//                int relx = (int) pos.x - chunkx * 16;
//                int rely = (int) pos.y;
//                int relz = (int) pos.z - chunkz * 16;
//                Vec3i chunkpos = new Vec3i(chunkx, 0, chunkz);
//                if(!chunks.containsKey(chunkpos)){
//                    Chunk ch = new Chunk(chunkpos.toVec3f());
//                    chunks.put(chunkpos, ch);
//                }
//                Chunk cc = chunks.get(chunkpos);
//                cc.removeBlockFromChunk(relx, rely, relz);
//                cc.buildMesh();
//            }
            if(Keyboard.isKeyDown(Keyboard.KEY_Q) && !pressedq){
                pressedq = true;
                Vector3f pos = new Vector3f((int)Math.floor(camera.getPosition().x), (int)Math.floor(camera.getPosition().y), (int)Math.floor(camera.getPosition().z));
                world.placeBlock(new Block(BlockTypes.DIRT, pos), new Vec3i(pos));
            }
            if(!Keyboard.isKeyDown(Keyboard.KEY_R)){
                pressedr = false;
            }
            if(!Keyboard.isKeyDown(Keyboard.KEY_Q)){
                pressedq = false;
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