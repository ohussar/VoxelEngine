package com.ohussar.VoxelEngine.Models;

import org.lwjgl.util.vector.Vector2f;

public class TexturedBlockModel {


    private static Vector2f[] uv(float minX, float minY, float maxX, float maxY){
        return new Vector2f[]{
                new Vector2f(minX / 256f, minY/256f),
                new Vector2f(minX / 256f, maxY / 256f),
                new Vector2f(maxX / 256f, maxY / 256f),
                new Vector2f(maxX / 256f, maxY / 256f),
                new Vector2f(maxX / 256f, minY / 256f),
                new Vector2f(minX / 256f, minY/256f),
        };
    }

    public static interface uvGetter{
        public Vector2f[] getUvForSide(int side);
    }

    public static class DirtUVS implements uvGetter {
        public static Vector2f[] UV = uv(19f, 33f, 34f, 48f);

        @Override
        public Vector2f[] getUvForSide(int side) {
            return UV;
        }
    }

    public static class GrassUVS implements uvGetter {
        public static Vector2f[] SIDE = uv(36f, 33f, 51f, 48f);
        public static Vector2f[] TOP = uv(1f, 33f, 16f, 48f);
        public static Vector2f[] BOTTOM = DirtUVS.UV;
        public GrassUVS(){}
        @Override
        public Vector2f[] getUvForSide(int side) {
            if(side == 0){
                return TOP;
            }
            if(side == 1){
                return BOTTOM;
            }
            return SIDE;
        }
    }

}
