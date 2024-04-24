package com.ohussar.VoxelEngine;

import com.ohussar.VoxelEngine.Entities.Player;
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
    public static Camera camera = new Camera(new Vector3f(0, 80, 0), new Vector3f(0, 0, 0));;
    public static World world;

    public static void main(String[] args) {
        createDiplay();

        StaticLoader = new MemoryLoader();
        StaticShader = new StaticShader();
        Renderer renderer = new Renderer(StaticShader);
        Player player = new Player(new Vector3f(0, 80, 0));
        world = new World();

        while(!Display.isCloseRequested()){
            renderer.prepare();
            camera.tick();
            StaticShader.start();
            StaticShader.connectToTextures();
            StaticShader.loadViewMatrix(camera);
            world.tick(camera);
            player.tick(world, camera);
            for(Chunk chunk : world.getLoadedChunks()){
                renderer.renderChunk(chunk, StaticShader, null);
            }
            //renderer.renderPlayer(player, StaticShader);

            StaticShader.stop();

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