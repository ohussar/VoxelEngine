package com.ohussar.VoxelEngine.Entities;

import com.ohussar.VoxelEngine.Main;
import com.ohussar.VoxelEngine.Util.Util;
import com.ohussar.VoxelEngine.World.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int VAO = -1;
    private int vertexCount = 0;
    public Vector3f position;
    public float speed = 0.075f;
    public Vector3f velocity;
    public Vector3f[] boundingBox = {
            new Vector3f(0.6f, 0, 0.6f),
            new Vector3f(0.6f, 0, 0.3f),//
            new Vector3f(0.3f, 0, 0.6f),
            new Vector3f(0.3f, 0, 0.3f),//
    };
    private boolean isGrounded = false;

    public Player(Vector3f position){
        this.position = position;
        this.velocity = new Vector3f(0, 0, 0);
//        Vector3f[] vert = Cube.NX_POS.clone();
//        Vector2f[] uvs = Cube.UV.clone();
//        List<Float> vertC = new ArrayList<>();
//        List<Float> uvC = new ArrayList<>();
//        for(int i = 0; i < vert.length; i++){
//            vertC.add(vert[i].x);
//            vertC.add(vert[i].y);
//            vertC.add(vert[i].z);
//        }
//        for(int i = 0; i < uvs.length; i++) {
//            uvC.add(uvs[i].x);
//            uvC.add(uvs[i].y);
//        }
//        float[] v = new float[vertC.size()];
//        float[] u = new float[uvC.size()];
//        for(int j = 0; j < vertC.size(); j++){
//            v[j] = vertC.get(j);
//            vertexCount++;
//        }
//        for(int j = 0; j < uvC.size(); j++){
//            u[j] = uvC.get(j);
//        }

        //this.VAO = Main.StaticLoader.updateVAO(VAO, v, u);
    }

    public void tick(World world, Camera camera){
        this.velocity.translate(0, -0.025f, 0);
        this.move(camera);

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && isGrounded){
            this.velocity.y += 0.4f;
        }
        isGrounded = false;
        collision(world);
        this.position.translate(this.velocity.x, this.velocity.y, this.velocity.z);
        camera.position = new Vector3f(this.position.x, this.position.y + 1, this.position.z);
    }

    public void collision(World world){
        for (Vector3f point : boundingBox){
            Vector3f futurePosX = new Vector3f(this.position.x + point.x + this.velocity.x, this.position.y + point.y, this.position.z + point.z);
            futurePosX = Util.floorVector(futurePosX);

            if(world.getBlock(futurePosX) != null){
                this.velocity.x = 0;
            }
            Vector3f futurePosZ = new Vector3f(this.position.x + point.x , this.position.y + point.y, this.position.z + point.z + this.velocity.z);
            futurePosZ = Util.floorVector(futurePosZ);
            if(world.getBlock(futurePosZ) != null){
                this.velocity.z = 0;
            }
            Vector3f futurePosY = new Vector3f(this.position.x +point.x, this.position.y + point.y + this.velocity.y, this.position.z + point.z);
            futurePosY = Util.floorVector(futurePosY);
            if(world.getBlock(futurePosY) != null){
                this.velocity.y = 0;
                isGrounded = true;
            }
        }
    }


    public void move(Camera camera){
        float mat = 0f;
        float hor = 0;
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            mat = -speed;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            mat = speed;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            hor = speed;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            hor = -speed;
        }

        float dx = (float) Math.sin(Math.toRadians(camera.rotation.getY())) * -mat;
        float dy = 0;
        float dz = (float) Math.cos(Math.toRadians(camera.rotation.getY())) * mat;
//        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
//            dy += 0.1f;
//        }
//        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
//            dy -= 0.1f;
//        }
//        if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
//            space = false;
//        }

        this.velocity.x = dx;
        this.velocity.z = dz;
        this.velocity.x += (float) Math.cos(Math.toRadians(camera.rotation.getY())) * -hor;
        this.velocity.z += (float) Math.sin(Math.toRadians(camera.rotation.getY())) * -hor;
    }
    public int getVAO(){
        return this.VAO;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
