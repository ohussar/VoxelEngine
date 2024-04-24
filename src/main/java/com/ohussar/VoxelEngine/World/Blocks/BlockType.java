package com.ohussar.VoxelEngine.World.Blocks;

import com.ohussar.VoxelEngine.Models.TexturedBlockModel;

public class BlockType {

    byte id;

    public byte getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public TexturedBlockModel.uvGetter getUvGetter(){
        return uvGetter;
    }

    public void setUvGetter(TexturedBlockModel.uvGetter getter){
        this.uvGetter = getter;
    }

    String name;
    TexturedBlockModel.uvGetter uvGetter;

    public BlockType(byte id, String name, TexturedBlockModel.uvGetter getter){
        this.id = id;
        this.name = name;
        this.uvGetter = getter;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass() != this.getClass()) return false;
        return this.id == ((BlockType) obj).getId();
    }
}
