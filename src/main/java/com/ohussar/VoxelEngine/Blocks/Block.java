package com.ohussar.VoxelEngine.Blocks;

import org.lwjgl.util.vector.Vector3f;

public class Block {
    public int blockType;
    public Vector3f pos;
    public Block(int blockType, Vector3f pos){
        this.blockType = blockType;
        this.pos = pos;
    }
}
