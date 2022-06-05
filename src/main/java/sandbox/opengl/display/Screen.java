package sandbox.opengl.display;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL46C;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;

import sandbox.opengl.openglModules.Shader;
import sandbox.opengl.openglModules.Texture;
import sandbox.opengl.openglModules.VAO;

public class Screen {
	// The window handle
	private long window;
	Callback debugCallback;

	VAO quad;
	Shader backgroundShader;
	Shader gridShader;
	Shader gridUpdateShader;

	Texture background;
	Texture grid;
	Texture gridNew;
	
	float radius = 20;
	int lmbState = 0;
	int rmbState = 0;
	final int zoom = 4;

	public Screen() {
	}

	private void init() throws IOException {

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		// glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 0);

		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(1200, 800, "the best powder toy", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			radius *= 1.0 + yoffset * 0.1;
			radius = Math.min(Math.max(radius, 5), 100);
		});

		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if(button == GLFW_MOUSE_BUTTON_LEFT){
				lmbState = action;
			}
			if(button == GLFW_MOUSE_BUTTON_RIGHT){
				rmbState = action;
			}
		});

		// Get the thread stack and push a new frame
		int w, h;
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);
			w = pWidth.get(0);
			h = pHeight.get(0);
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		// make the window full-screen
		// glfwMaximizeWindow(window);
		// take the focus
		glfwFocusWindow(window);

		createCapabilities();

		PrintStream customPrintStream = new PrintStream(System.out){
			@Override
			public void print(String s) {
				super.print(s);
			}
			@Override
			public void println() {
				super.println();
			}
		};
		debugCallback = GLUtil.setupDebugMessageCallback(customPrintStream);

		quad = new VAO();
		quad.bind();
		float[] positions = { -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f };
		quad.createIndexBuffer(new int[]{0, 1, 2, 2, 1, 3});
		quad.createFloatAttribute(0, positions, 2, 0, GL_STATIC_DRAW);
		quad.unbind();

		backgroundShader = new Shader("shaders/background.vs", "shaders/background.fs");
		backgroundShader.finishInit();
		backgroundShader.init_uniforms(List.of("transform"));

		gridShader = new Shader("shaders/grid.vs", "shaders/grid.fs");
		gridShader.finishInit();
		gridShader.init_uniforms(List.of("transform", "mouseCoords", "radius"));

		gridUpdateShader = new Shader("shaders/grid_update.cp");
		gridUpdateShader.finishInit();
		gridUpdateShader.init_uniforms(List.of("mouseCoords", 
		"radius", "lmbState", "rmbState", "updateRound"));

		background = new Texture("textures/tree.png");

		grid = new Texture(w / zoom, h / zoom);
		gridNew = new Texture(w / zoom, h / zoom);

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	private void loop() {
		while (!glfwWindowShouldClose(window)) {
			float w, h;
			float mX, mY;
			try (MemoryStack stack = stackPush()) {
				IntBuffer pWidth = stack.callocInt(1);
				IntBuffer pHeight = stack.callocInt(1);
				glfwGetWindowSize(window, pWidth, pHeight);
				w = pWidth.get();
				h = pHeight.get();

				DoubleBuffer mouseX = stack.callocDouble(1);
				DoubleBuffer mouseY = stack.callocDouble(1);
				glfwGetCursorPos(window, mouseX, mouseY);
				mX = (float)mouseX.get();
				mY = (float)mouseY.get();

			}
			GL46C.glViewport(0, 0, (int) w, (int) h);

			glDisable(GL_CULL_FACE);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_MULTISAMPLE);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			GL46C.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

			Matrix4f transform = new Matrix4f();

			Vector2i mouseCoords = new Vector2i((int)mX / zoom, (int)mY / zoom);

			//update the grid
			glClearTexImage(gridNew.id, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			gridUpdateShader.start();
			gridUpdateShader.loadiVec2("mouseCoords", mouseCoords);
			gridUpdateShader.loadFloat("radius", radius);
			gridUpdateShader.loadInt("lmbState", lmbState);
			gridUpdateShader.loadInt("rmbState", rmbState);
			for(int updateRound = 0; updateRound<2; updateRound++){
				gridUpdateShader.loadInt("updateRound", updateRound);
				glBindImageTexture(0, grid.id, 0, false, 0, GL_READ_WRITE, GL_RGBA8);
				glBindImageTexture(1, gridNew.id, 0, false, 0, GL_READ_WRITE, GL_RGBA8);
				glDispatchCompute((grid.width + 7) / 8, (grid.height + 7) / 8, 1);
				glMemoryBarrier(GL_ALL_BARRIER_BITS);
			}
			gridUpdateShader.stop();

			//swap new and old grid
			Texture tmp = gridNew;
			gridNew = grid;
			grid = tmp;

			quad.bind();
			quad.bindAttribute(0);

			//Draw the background
			backgroundShader.start();
			backgroundShader.loadMat4("transform", transform);
			background.bindAsTexture(0);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			background.unbindAsTexture(0);
			backgroundShader.stop();

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
			
			//Draw the grid
			gridShader.start();
			gridShader.loadMat4("transform", transform);
			gridShader.loadiVec2("mouseCoords", mouseCoords);
			gridShader.loadFloat("radius", radius);
			grid.bindAsTexture(0);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			grid.unbindAsTexture(0);
			gridShader.stop();

			glDisable(GL_BLEND);

			quad.unbindAttribute(0);
			quad.unbind();
			
			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents();
		}
	}

	public void run() throws IOException {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
