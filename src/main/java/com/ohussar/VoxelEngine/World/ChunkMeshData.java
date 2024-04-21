package com.ohussar.VoxelEngine.World;

import com.ohussar.VoxelEngine.Main;
import com.ohussar.VoxelEngine.Models.Vertex;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ChunkMeshData {
    public List<Vertex> vertices;
    protected List<Float> positionList;
    protected List<Float> uvsList;
    public int VAO = -1;

    public ChunkMeshData(){
        vertices = new ArrayList<>();
        positionList = new ArrayList<>();
        uvsList = new ArrayList<>();

        VAO = Main.StaticLoader.updateVAO(VAO, new float[]{}, new float[]{});
    }



}
