package com.example.sphere;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sphere {
    // Buffer for vertex-coordinates
    private final FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private float sphereCoords[] = {
            // Example coordinates
            // You need to define all the vertices of the sphere here
    };

    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private final int vertexCount = sphereCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Color for the sphere (RGBA)
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Sphere() {
        // Initialize sphereCoords with actual coordinates
        sphereCoords = createSphereCoords(1.0f, 40, 40); // 1.0f is the radius of the sphere

        // Convert the array to FloatBuffer to pass to OpenGL
        ByteBuffer bb = ByteBuffer.allocateDirect(sphereCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(sphereCoords);
        vertexBuffer.position(0);
        mProgram = MainRenderer.program;
    }


    private float[] createSphereCoords(float radius, int numStacks, int numSlices) {
        int numVertices = (numStacks + 1) * (numSlices + 1);
        int numIndices = numStacks * numSlices * 6;

        float[] vertices = new float[numVertices * 3];
        short[] indices = new short[numIndices];

        int vertexIndex = 0;
        int indexIndex = 0;

        for (int i = 0; i <= numStacks; i++) {
            float phi = (float) (i * Math.PI / numStacks);
            for (int j = 0; j <= numSlices; j++) {
                float theta = (float) (j * 2 * Math.PI / numSlices);
                float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
                float y = (float) (radius * Math.sin(phi) * Math.sin(theta));
                float z = (float) (radius * Math.cos(phi));

                vertices[vertexIndex++] = x;
                vertices[vertexIndex++] = y;
                vertices[vertexIndex++] = z;
            }
        }

        // Now create the index data
        for (int i = 0; i < numStacks; i++) {
            for (int j = 0; j < numSlices; j++) {
                indices[indexIndex++] = (short) (i * (numSlices + 1) + j);
                indices[indexIndex++] = (short) ((i + 1) * (numSlices + 1) + j);
                indices[indexIndex++] = (short) (i * (numSlices + 1) + j + 1);

                indices[indexIndex++] = (short) (i * (numSlices + 1) + j + 1);
                indices[indexIndex++] = (short) ((i + 1) * (numSlices + 1) + j);
                indices[indexIndex++] = (short) ((i + 1) * (numSlices + 1) + j + 1);
            }
        }

        return vertices; // Only returning vertices here, but indices are needed too.
    }


    public void setColor(float r, float g, float b, float a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "v_Color");

        // Set color for drawing the sphere
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the sphere
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

