package com.example.sphere;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private Sphere sphere;
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private final String vertex_shader = "uniform mat4 u_MVPMatrix; // A constant representing the combined model/view/projection matrix.\n" +
            "attribute vec4 a_Position; // Per-vertex position information we will pass in.\n" +
            "attribute vec4 a_Color; // Per-vertex color information we will pass in.\n" +
            "\n" +
            "// This will be passed into the fragment shader.\n" +
            "varying vec4 v_Color; \n" +
            "\n" +
            "void main() {\n" +
            "    // Pass through the color.\n" +
            "    v_Color = a_Color;\n" +
            "    \n" +
            "    // gl_Position is a special variable used to store the final position.\n" +
            "    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.\n" +
            "    gl_Position = u_MVPMatrix * a_Position;\n" +
            "}\n";

    private final String fragment_shader = "precision mediump float; // Set the default precision to medium. We don't need as high of a precision in the fragment shader.\n" +
            "varying vec4 v_Color; // This is the color from the vertex shader interpolated across the triangle per fragment.\n" +
            "\n" +
            "void main() {\n" +
            "    // gl_FragColor is a special variable that holds the color of the pixel that will be drawn to the screen.\n" +
            "    gl_FragColor = v_Color;\n" +
            "}\n";

    private final Random random = new Random();
    // ... Other code ...

    public static int program;

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        // Check for compile errors
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("Shader", "Could not compile shader " + type + ":");
            Log.e("Shader", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }



    private void createAndLinkProgram() {
        // Load the shaders from files or resources and compile them
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertex_shader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader);

        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        // Check for linking errors
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            // Handle the error, log the info log etc.
            GLES20.glDeleteProgram(program);
            throw new RuntimeException("Error linking program: " + GLES20.glGetProgramInfoLog(program));
        }
    }

    public MainRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Enable depth testing for hidden-surface elimination.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Create the sphere object that we will draw.
        sphere = new Sphere();

        // Load the shaders, create a GLES program, and link the shaders to the program.
        createAndLinkProgram();

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(program);

        // Additional setup can include configuring the view, initializing lighting parameters,
        // and other scene settings which are static and don't need to be changed often.

        // Set up a timer to change the sphere's color every 10 seconds.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Generate random color values
                float red = random.nextFloat();
                float green = random.nextFloat();
                float blue = random.nextFloat();

                // Set the new color for the sphere. Since OpenGL uses a separate thread,
                // you need to queue this task to run on the OpenGL thread.
                sphere.setColor(red, green, blue, 1.0f);
            }
        }, 0, 10000);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // This projection matrix is applied to object coordinates in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        sphere.draw(modelViewProjectionMatrix);
    }
}
