package com.ohussar.VoxelEngine.Blocks;

import com.ohussar.VoxelEngine.Entities.Cube;
import com.ohussar.VoxelEngine.Main;
import com.ohussar.VoxelEngine.MemoryLoader;
import com.ohussar.VoxelEngine.Util.Util;
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

    private static int CHUNK_SIZE_X = 16;
    private static int CHUNK_SIZE_Y = 32;
    private static int CHUNK_SIZE_Z = 16;

    private Block[] CHUNK_BLOCKS = new Block[CHUNK_SIZE_X * CHUNK_SIZE_Y * CHUNK_SIZE_Z];

    private final Vector3f position;

    public Chunk(Vector3f position){
        this.position = position;
    }

    public void setBlockList(List<Block> blocks){
        this.blocks = new ArrayList<>(blocks);

        for(Block block : blocks){
            int x = (int) block.pos.x;
            int y = (int) block.pos.y;
            int z = (int) block.pos.z;
            addBlockToChunkInternal(block, x, y, z);
        }

    }

    public List<Vertex> getVertices(){
        return vertices;
    }

    public Vector3f getPosition(){
        return position;
    }

    public void addBlockToChunk(Block block){
        int x = (int) block.pos.x;
        int y = (int) block.pos.y;
        int z = (int) block.pos.z;

        int relx = x - (int)position.x*16;
        int rely = y - (int)position.y*16;
        int relz = z - (int)position.z*16;
        block.pos.set(relx, rely, relz);
        if(getBlockAtPos(relx, rely, relz).blockType > 0){
            int index = this.blocks.indexOf(getBlockAtPos(relx, rely, relz));
            this.blocks.set(index, block);
        }else{
            this.blocks.add(block);
        }
        addBlockToChunkInternal(block, relx, rely, relz);
    }

    public void removeBlockFromChunk(Block block){

    }

    public Block getBlockAtPos(int x, int y, int z){
        return CHUNK_BLOCKS[x * CHUNK_SIZE_X + y + z * (CHUNK_SIZE_X * CHUNK_SIZE_Y)] == null ? new Block((byte) -1, Util.EmptyVec3()) : CHUNK_BLOCKS[x * CHUNK_SIZE_X + y + z * (CHUNK_SIZE_X * CHUNK_SIZE_Y)];
    }
    private void addBlockToChunkInternal(Block block, int x, int y, int z){
        CHUNK_BLOCKS[x * CHUNK_SIZE_X + y + z * (CHUNK_SIZE_X * CHUNK_SIZE_Y)] = block;
    }
    public void buildMesh(){
        List<Float> positionslist = new ArrayList<Float>();
        List<Float> uvlist = new ArrayList<Float>();
        vertices.clear();
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
                        positionslist.add(pos.x);
                        positionslist.add(pos.y);
                        positionslist.add(pos.z);
                        uvlist.add(UV[f].x);
                        uvlist.add(UV[f].y);
                        vertices.add(new Vertex(pos, UV[f]));
                    }
                }
            }

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
