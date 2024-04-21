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

    public static final List<Byte> blocks = new ArrayList<>();
    public static ModelTexture atlas = new ModelTexture(MemoryLoader.loadTexture("pack"));
    public static final ModelTexture[] blockTextures = {
            new ModelTexture(-1),
            atlas,
            atlas,
    };

    public static void registerBlocks(){
        blocks.add(DIRT);
        blocks.add(GRASS);
    }


    public static void registerUvGetters(){
        blockUvs.put(DIRT, new TexturedBlockModel.DirtUVS());
        blockUvs.put(GRASS, new TexturedBlockModel.GrassUVS());
    }

    public static final Map<Byte, TexturedBlockModel.uvGetter> blockUvs = new HashMap<>();


}
