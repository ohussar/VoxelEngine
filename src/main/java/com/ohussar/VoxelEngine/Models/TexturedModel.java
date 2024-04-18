package com.ohussar.VoxelEngine.Models;

import com.ohussar.VoxelEngine.Textures.ModelTexture;

public class TexturedModel {
    private final RawModel model;
    private final ModelTexture texture;

    public TexturedModel(RawModel model, ModelTexture texture){
        this.model = model;
        this.texture = texture;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
