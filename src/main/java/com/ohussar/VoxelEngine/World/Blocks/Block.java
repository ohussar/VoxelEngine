package com.ohussar.VoxelEngine.World.Blocks;

import org.lwjgl.util.vector.Vector3f;

public class Block {
    public BlockType blockType;
    public Vector3f pos;
    public Block(BlockType blockType, Vector3f pos){
        this.blockType = blockType;
        this.pos = pos;
    }
}
