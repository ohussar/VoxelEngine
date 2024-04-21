package com.ohussar.VoxelEngine.World;

import com.ohussar.VoxelEngine.Entities.Camera;
import com.ohussar.VoxelEngine.Util.Vec3i;
import com.ohussar.VoxelEngine.World.Blocks.Block;
import com.ohussar.VoxelEngine.World.Blocks.BlockTypes;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {

    private Map<Vec3i, Chunk> chunks = new HashMap<Vec3i, Chunk>();
    private List<Chunk> loadedChunks = new ArrayList<>();

    private Vec3i previousCameraPos = new Vec3i(5, 5, 5);

    public void tick(Camera camera){

        Vector3f cPos = camera.getPosition();
        Vec3i chunkCameraPos = new Vec3i((int)Math.floor(cPos.x/16), 0, (int)Math.floor(cPos.z/16));

        if(!chunkCameraPos.equals(previousCameraPos)){
            previousCameraPos = chunkCameraPos;
            loadedChunks.clear();
            for(int i = -5; i < 5; i++){
                for(int j = -5; j < 5; j++){
                    Vec3i copy = chunkCameraPos.copy();
                    Vec3i chunkPos = copy.translate(i, 0, j);
                    if(!isChunkGenerated(chunkPos)){
                        generateChunk(chunkPos);
                    }
                    loadedChunks.add(chunks.get(chunkPos));
                }
            }
        }

    }

    public List<Chunk> getLoadedChunks(){
        return loadedChunks;
    }

    public boolean isChunkGenerated(int x, int z){
        return isChunkGenerated(new Vec3i(x, 0, z));
    }

    public boolean isChunkGenerated(Vec3i chunkpos){
        return chunks.containsKey(chunkpos);
    }
    public void generateChunk(int chunkx, int chunkz){
        generateChunk(new Vec3i(chunkx, 0, chunkz));
    }
    public Block getBlock(Vector3f pos){
        int xx = (int) pos.x;
        int yy = (int) pos.y;
        int zz = (int) pos.z;

        int chunkx = (int) Math.floor((double)xx/(double)16);
        int chunkz = (int) Math.floor((double)zz/(double)16);
        Vec3i chunkpos = new Vec3i(chunkx, 0, chunkz);
        if(!isChunkGenerated(chunkpos)){
            generateChunk(chunkpos);
        }
        Chunk chunk = chunks.get(chunkpos);

        int relx = xx - chunkx * 16;
        int rely = yy;
        int relz = zz - chunkz * 16;
        Block ret = chunk.getBlockAtPos(relx, rely, relz);
        if(ret!=null){
            ret.pos.translate(chunkx*16, 0, chunkz*16);
            return ret;
        }
        return null;
    }
    public void placeBlock(Block block, Vec3i blockPos){

        int xx = blockPos.getX();
        int yy = blockPos.getY();
        int zz = blockPos.getZ();

        int chunkx = (int) Math.floor((double)xx/(double)16);
        int chunkz = (int) Math.floor((double)zz/(double)16);
        Vec3i chunkpos = new Vec3i(chunkx, 0, chunkz);
        if(!isChunkGenerated(chunkpos)){
            generateChunk(chunkpos);
        }

        Chunk chunk = chunks.get(chunkpos);
        chunk.addBlockToChunk(block);
        chunk.buildMesh();
    }

    public void removeBlock(Vec3i pos){
        int xx = (int) pos.getX();
        int yy = (int) pos.getY();
        int zz = (int) pos.getZ();

        int chunkx = (int) Math.floor((double)xx/(double)16);
        int chunkz = (int) Math.floor((double)zz/(double)16);
        Vec3i chunkpos = new Vec3i(chunkx, 0, chunkz);
        Chunk chunk = chunks.get(chunkpos);
        if(chunk != null){
            chunk.removeBlockFromChunk(xx-chunkx*16, yy, zz-chunkz*16);
            chunk.buildMesh();
        }
    }

    public void generateChunk(Vec3i chunkpos){
        if(!chunks.containsKey(chunkpos)){
            Chunk generatedChunk = new Chunk(chunkpos.toVec3f());

            List<Block> blocks = new ArrayList<>();

            for(int x = 0; x < 16; x++){
                for(int z = 0; z < 16; z++){
                    Block block = new Block(BlockTypes.DIRT, new Vector3f(x, 0, z));
                    blocks.add(block);
                }
            }

            generatedChunk.setBlockList(blocks);
            generatedChunk.buildMesh();
            chunks.put(chunkpos, generatedChunk);
        }
    }

}
