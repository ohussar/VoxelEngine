package com.ohussar.VoxelEngine.World.Blocks;

import com.ohussar.VoxelEngine.Entities.Cube;
import com.ohussar.VoxelEngine.MemoryLoader;
import com.ohussar.VoxelEngine.Models.TexturedBlockModel;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Textures.ModelTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockTypes {

    public static final byte DIRT = 1;
    public static final byte GRASS = 2;
    private static byte pointer = 0;
    public static ModelTexture atlas = new ModelTexture(MemoryLoader.loadTexture("pack"));
    public static List<BlockType> blocks = new ArrayList<>();


    public static BlockType createBlockType(String name, TexturedBlockModel.uvGetter getter){
         BlockType type = new BlockType(pointer, name, getter);
         pointer += 1;
         blocks.add(type);
        return type;
    }


}
