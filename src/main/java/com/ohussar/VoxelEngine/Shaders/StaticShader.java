package com.ohussar.VoxelEngine.Shaders;

public class StaticShader extends ShaderProgram{
    private static final String vertexFile = "src/main/java/com/ohussar/VoxelEngine/Shaders/vertexShader.txt";
    private static final String fragmentFile = "src/main/java/com/ohussar/VoxelEngine/Shaders/fragmentShader.txt";
    public StaticShader() {
        super(vertexFile, fragmentFile);
    }
    @Override
    protected void bindAttributes() { // bind attributes from model to the shader: like vertex positions to in vec3 pos;
        super.bindAttribute("position", 0);
        super.bindAttribute("textureCoords", 1);
    }
}
