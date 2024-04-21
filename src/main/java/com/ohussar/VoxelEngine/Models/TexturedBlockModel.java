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
        public static Vector2f[] UV = uv(17f, 33f, 32f, 48f);

        @Override
        public Vector2f[] getUvForSide(int side) {
            return UV;
        }
    }

    public static class GrassUVS implements uvGetter {
        public static Vector2f[] SIDE = {
                new Vector2f(1f /16f, 0f),
                new Vector2f(1f /16f, 1f / 16f),
                new Vector2f(2f /16f, 1/ 16f),
                new Vector2f(2f /16f, 1/ 16f),
                new Vector2f(2f /16f, 0f),
                new Vector2f(1f /16f, 0f / 16f),
        };
        public static Vector2f[] TOP = uv(0f, 33f, 15f, 48f);
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
