package com.ohussar.VoxelEngine.World.Blocks;

import com.ohussar.VoxelEngine.Models.TexturedBlockModel;

public class Blocks {

    public static final BlockType DIRT = BlockTypes.createBlockType("dirt", new TexturedBlockModel.DirtUVS());
    public static final BlockType GRASS = BlockTypes.createBlockType("grass", new TexturedBlockModel.GrassUVS());
    public static final BlockType STONE = BlockTypes.createBlockType("stone", new TexturedBlockModel.StoneUVS());

}
