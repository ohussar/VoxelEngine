package com.ohussar.VoxelEngine.Blocks;

import com.ohussar.VoxelEngine.Entities.Cube;
import com.ohussar.VoxelEngine.MemoryLoader;
import com.ohussar.VoxelEngine.Models.TexturedModel;
import com.ohussar.VoxelEngine.Textures.ModelTexture;

public class BlockTypes {

    public static final int DIRT = 0;

    public static final ModelTexture[] blockTextures = {
            new ModelTexture(MemoryLoader.loadTexture("dirtTex")) // dirt;
    };


}
