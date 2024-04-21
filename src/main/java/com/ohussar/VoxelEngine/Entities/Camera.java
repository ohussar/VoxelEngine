package com.ohussar.VoxelEngine.Entities;

import com.ohussar.VoxelEngine.Main;
import com.ohussar.VoxelEngine.Util.Vec3i;
import com.ohussar.VoxelEngine.World.Blocks.Block;
import com.ohussar.VoxelEngine.World.Blocks.BlockTypes;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.stream.ImageOutputStream;
import java.security.Key;

public class Camera {
    Vector3f position;
    Vector3f rotation;
    Vector3f momentum;
    float speed = 0.1f;
    private boolean space = false;
    private boolean mouse0 = false;
    private boolean mouse1 = false;
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.momentum = new Vector3f(0, 0, 0);
    }


    public void tick() {
        move();

       // this.momentum.translate(0, -0.01f, 0);
        Vector3f future = new Vector3f(this.position.x, this.position.y + this.momentum.y - 1, this.position.x);
        Block toMeet = Main.world.getBlock(future);
        if(toMeet != null){
            System.out.println("mmmm");
            this.momentum.y *= -0.7f;
        }

        if (Mouse.isButtonDown(1) && !mouse1) {
            mouse1 = true;
            placeBlock();
        }
        if (!Mouse.isButtonDown(1)) {
            mouse1 = false;
        }
        if(!Mouse.isButtonDown(0)){
            mouse0 = false;
        }
        if (Mouse.isButtonDown(0) && !mouse0) {
            mouse0 = true;
            Block block = raycastFromCameraRotation();
            if(block != null){
                Main.world.removeBlock(new Vec3i(block.pos));
            }
        }
        this.position.translate(momentum.x, momentum.y, momentum.z);
    }

    public void placeBlock(){
        float stepsize = 0.005f;
        float maxSteps = 1000;
        float maxBlockDist = stepsize * maxSteps;
        float dy = -(float) Math.sin(Math.toRadians(rotation.getX())) * stepsize;
        int i = 0;
        float xx = this.position.x;
        float yy = this.position.y;
        float zz = this.position.z;

        float angy = (float) Math.toRadians(rotation.getX());
        float dx = -(1 / (float) Math.tan(angy)) * dy * (float) Math.sin(Math.toRadians(rotation.getY()));
        float dz = (1 / (float) Math.tan(angy)) * dy * (float) Math.cos(Math.toRadians(rotation.getY()));

        Vec3i previousPos = new Vec3i(0, 0, 0);

        float delta = 1 - (float) Math.sqrt((Math.abs(Math.sin(Math.toRadians(rotation.getX())))));
        while (i < maxSteps) {
            i++;
            float beforeX = xx;
            float beforeY = yy;
            float beforeZ = zz;
            xx += dx;
            yy += dy;
            zz += dz;
            Vec3i pos = new Vec3i(new Vector3f((int) Math.round(xx), (int) Math.round(yy), (int) Math.round(zz)));
            if (!previousPos.equals(pos)) {
                previousPos = pos;
                Block block = Main.world.getBlock(pos.toVec3f());
                if (block != null) {
                    if (block.blockType != -1) {
                        Vec3i ray1 = new Vec3i(new Vector3f((int) Math.round(xx), (int) Math.round(beforeY), (int) Math.round(beforeZ)));
                        Block block1 = Main.world.getBlock(ray1.toVec3f());
                        if(block1 != null && block1.blockType != -1){
                            Vec3i newPos = ray1.translate(-(int)Math.signum(dx), 0,0);
                            Main.world.placeBlock(new Block(BlockTypes.DIRT, newPos.toVec3f()), newPos);
                            break;
                        }
                        Vec3i ray2 = new Vec3i(new Vector3f((int) Math.round(beforeX), (int) Math.round(yy), (int) Math.round(beforeZ)));
                        Block block2 = Main.world.getBlock(ray2.toVec3f());
                        if(block2 != null && block2.blockType != -1){
                            Vec3i newPos = ray2.translate(0, -(int)Math.signum(dy),0);
                            Main.world.placeBlock(new Block(BlockTypes.DIRT, newPos.toVec3f()), newPos);
                            break;
                        }
                        Vec3i ray3 = new Vec3i(new Vector3f((int) Math.round(beforeX), (int) Math.round(beforeY), (int) Math.round(zz)));
                        Block block3 = Main.world.getBlock(ray3.toVec3f());
                        if(block3 != null && block3.blockType != -1){
                            Vec3i newPos = ray3.translate(0, 0,-(int)Math.signum(dz));
                            Main.world.placeBlock(new Block(BlockTypes.DIRT, newPos.toVec3f()), newPos);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }



    public Block raycastFromCameraRotation() {
        float stepsize = 0.005f;
        float maxSteps = 1000;
        float maxBlockDist = stepsize * maxSteps;
        float dy = -(float) Math.sin(Math.toRadians(rotation.getX())) * stepsize;
        int i = 0;
        float xx = this.position.x;
        float yy = this.position.y;
        float zz = this.position.z;

        float angy = (float) Math.toRadians(rotation.getX());
        float dx = -(1 / (float) Math.tan(angy)) * dy * (float) Math.sin(Math.toRadians(rotation.getY()));
        float dz = (1 / (float) Math.tan(angy)) * dy * (float) Math.cos(Math.toRadians(rotation.getY()));

        Vec3i previousPos = new Vec3i(0, 0, 0);

        float delta = 1 - (float) Math.sqrt((Math.abs(Math.sin(Math.toRadians(rotation.getX())))));
        Block found = null;
        while (i < maxSteps) {
            i++;
            xx += dx;
            yy += dy;
            zz += dz;
            Vec3i pos = new Vec3i(new Vector3f((int) Math.round(xx), (int) Math.round(yy), (int) Math.round(zz)));
            if (!previousPos.equals(pos)) {
                previousPos = pos;
                Block block = Main.world.getBlock(pos.toVec3f());
                if (block != null) {
                    if (block.blockType != -1) {
                        found = block;
                        break;
                    }
                }
            }
        }
        return found;
    }

    public void move(){
        float mat = 0f;
        float hor = 0;
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            mat = -1;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            mat = 1;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            hor = 1;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            hor = -1;
        }

        rotation.x += 0.1f*-Mouse.getDY();
        rotation.y += 0.1f*Mouse.getDX();

        float dx = (float) Math.sin(Math.toRadians(rotation.getY())) * -mat;
        float dy = 0;
        float dz = (float) Math.cos(Math.toRadians(rotation.getY())) * mat;
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !space){
            this.position.y += 0.1f;
            space = true;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
            this.position.y -= 0.1f;
        }
        if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            space = false;
        }

        position.translate(dx, dy, dz);
        dx = (float) Math.cos(Math.toRadians(rotation.getY())) * mat;
        dz = (float) Math.sin(Math.toRadians(rotation.getY())) * hor;
    }


    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getRotation() {
        return rotation;
    }
}
