package com.ohussar.VoxelEngine.Blocks;

import com.ohussar.VoxelEngine.Entities.Cube;
import com.ohussar.VoxelEngine.Main;
import com.ohussar.VoxelEngine.MemoryLoader;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.ninjacave.jarsplice.gui.ShellScriptPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ohussar.VoxelEngine.Entities.Cube.*;

public class Chunk {
    public int VAO = -1;
    private List<Block> blocks = new ArrayList<>();
    private List<Vertex> vertices = new ArrayList<>();
    private List<Vector2f> uvs = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();

    public void setBlockList(List<Block> blocks){
        this.blocks = new ArrayList<>(blocks);
    }

    public List<Vertex> getVertices(){
        return vertices;
    }


    public void buildMesh(){
        for(int i = 0; i < this.blocks.size(); i++){


            Vector3f[] directions = {
                    new Vector3f(0, 1, 0),
                    new Vector3f(0, -1, 0),
                    new Vector3f(1, 0, 0),
                    new Vector3f(-1 , 0, 0),
                    new Vector3f(0, 0, 1),
                    new Vector3f(0, 0, -1)
            };


            Vector3f[][] vertexes = {PY_POS, NY_POS, PX_POS, NX_POS, PZ_POS, NZ_POS};
            Boolean[] isOccluded = {false, false, false, false, false, false};
            Block initial = blocks.get(i);

            for (Block block : this.blocks) {
                for (int k = 0; k < directions.length; k++) {
                    Vector3f p = new Vector3f(initial.pos.x + directions[k].x, initial.pos.y + directions[k].y, initial.pos.z + directions[k].z);
                    if (p.equals(block.pos)) {
                        isOccluded[k] = true;
                    }
                }
            }

            for(int k = 0; k < directions.length; k++){
                if(!isOccluded[k]){
                    for(int f = 0; f < 6; f++){
                        Vector3f start = vertexes[k][f];
                        Vector3f pos = new Vector3f(start.x + initial.pos.x, start.y + initial.pos.y, start.z + initial.pos.z);
                        vertices.add(new Vertex(pos, UV[f]));
                    }
                }
            }

        }

        List<Float> positionslist = new ArrayList<>();
        List<Float> uvlist = new ArrayList<>();

        for(int i = 0; i < vertices.size(); i++){
            positionslist.add(vertices.get(i).pos.x);
            positionslist.add(vertices.get(i).pos.y);
            positionslist.add(vertices.get(i).pos.z);
            uvlist.add(vertices.get(i).uv.x);
            uvlist.add(vertices.get(i).uv.y);
        }


        float[] vert = new float[positionslist.size()];
        float[] uv = new float[uvlist.size()];

        for(int i = 0; i < positionslist.size(); i++){
            vert[i] = positionslist.get(i);
        }
        for(int k = 0; k < uvlist.size(); k++){
            uv[k] = uvlist.get(k);
        }


        VAO = Main.StaticLoader.updateVAO(VAO, vert, uv);
        positionslist.clear();;
        uvlist.clear();
    }

}
