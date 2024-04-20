package com.ohussar.VoxelEngine.Blocks;

import com.ohussar.VoxelEngine.Entities.Cube;
import com.ohussar.VoxelEngine.MemoryLoader;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Textures.ModelTexture;

public class BlockTypes {

    public static final byte DIRT = 1;

    public static final ModelTexture[] blockTextures = {
            new ModelTexture(-1),
            new ModelTexture(MemoryLoader.loadTexture("dirtTex")) // dirt;
    };


}
