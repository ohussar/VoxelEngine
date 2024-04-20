package com.ohussar.VoxelEngine.Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.stream.ImageOutputStream;
import java.security.Key;

public class Camera {
    Vector3f position;
    Vector3f rotation;

    float speed = 0.1f;

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void move(){
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



        rotation.x += 0.1f*-Mouse.getDY();
        rotation.y += 0.1f*Mouse.getDX();

        float dx = (float) Math.sin(Math.toRadians(rotation.getY())) * -mat;
        float dy = 0;
        float dz = (float) Math.cos(Math.toRadians(rotation.getY())) * mat;
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            dy += 0.1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
            dy-=0.1f;
        }
        position.translate(dx, dy, dz);
        dx = (float) Math.cos(Math.toRadians(rotation.getY())) * -hor;
        dz = (float) Math.sin(Math.toRadians(rotation.getY())) * -hor;
        position.translate(dx, 0, dz);
    }


    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getRotation() {
        return rotation;
    }
}
