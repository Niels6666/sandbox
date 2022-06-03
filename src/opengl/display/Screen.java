package display;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwFocusWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL46C;
import org.lwjgl.system.MemoryStack;

import openglModules.Shader;
import openglModules.Texture;
import openglModules.VAO;

public class Screen {
	// The window handle
	private long window;
	VAO quad;
	Shader gridShader;
	Shader gridUpdateShader;
	Texture grid;
	Texture gridNew;

	public Screen() {
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		// glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 0);

		// Create the window
		window = glfwCreateWindow(240, 243, "the best powder toy", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
//		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
//				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//		});

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
		quad = new VAO();
		quad.bind();
		float[] positions = { -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f };
		quad.createIndexBuffer(new int[]{0, 1, 2, 2, 1, 3});
		quad.createFloatAttribute(0, positions, 2, 0, GL_STATIC_DRAW);
		quad.unbind();

		gridShader = new Shader("shaders/grid.vs", "shaders/grid.fs");
		gridShader.finishInit();
		gridShader.init_uniforms(List.of("transform", "color"));

		gridUpdateShader = new Shader("shaders/grid.cp");
		gridUpdateShader.finishInit();

		grid = new Texture(w, h, "C:/Users/huber/eclipse-workspace/powderToy/textures/tree.png");
		gridNew = new Texture(w, h);

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
				Matrix4f toSpace = new Matrix4f();
				toSpace.m00(2.0f);
				toSpace.m11(2.0f * w / h);
				toSpace.m30(-1.0f);
				toSpace.m31(-1.0f);
				toSpace.invert();
				Vector4f vec = new Vector4f((float) mouseX.get() / w * 2.0f - 1.0f,
						(float) -mouseY.get() * w / h * 2.0f + 1.0f, 0.0f, 0.0f);
				toSpace.transform(vec);
				mX = vec.x;
				mY = vec.y;
			}
			GL46C.glViewport(0, 0, (int) w, (int) h);

			glDisable(GL_CULL_FACE);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_MULTISAMPLE);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			GL46C.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

			Matrix4f transform = new Matrix4f();

			//update the grid
			// gridUpdateShader.start();
			// glBindImageTexture(0, grid.id, 0, false, 0, GL_READ_ONLY, GL_RGBA8);
			// glBindImageTexture(1, gridNew.id, 0, false, 0, GL_READ_WRITE, GL_RGBA8);
			// glDispatchCompute((grid.width + 7) / 8, (grid.height + 7) / 8, 1);
			// glMemoryBarrier(GL_ALL_BARRIER_BITS);
			// gridUpdateShader.stop();

			// //swap new and old grid
			// Texture tmp = gridNew;
			// gridNew = grid;
			// grid = tmp;

			//draw the grid
			gridShader.start();
			gridShader.loadMat4("transform", transform);
			gridShader.loadVec4("color", new Vector4f(1, 0, 1, 1));
			quad.bind();
			quad.bindAttribute(0);
			// glBindImageTexture(0, grid.id, 0, false, 0, GL_READ_ONLY, GL_RGBA8);
			grid.bindAsTexture(0);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			grid.unbindAsTexture(0);
			quad.unbindAttribute(0);
			quad.unbind();
			
			gridShader.stop();

			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents();
		}
	}

	public void run() {
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
