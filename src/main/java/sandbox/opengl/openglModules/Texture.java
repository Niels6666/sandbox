package sandbox.opengl.openglModules;

import static org.lwjgl.opengl.GL46C.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;

public class Texture {
	public int id;
	public int width;
	public int height;

	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
		id = glCreateTextures(GL_TEXTURE_2D);
		bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, this.width, this.height);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		unbind();

	}

	public Texture(String path) throws IOException {
		System.out.println("Loading texture: " + path);

		int width[] = new int[1];
		int height[] = new int[1];
		int channels[] = new int[1];
		ByteBuffer buff = STBImage.stbi_load(path, width, height, channels, 4);

		this.width = width[0];
		this.height = height[0];

		// byte[] array = new byte[width * height * 4];
		// for(int p = 0; p < width * height; p++){
		// 	array[4*p + 0] = (byte)0xFF;
		// 	array[4*p + 1] = (byte)0xFF;
		// 	array[4*p + 2] = (byte)0xFF;
		// 	array[4*p + 3] = (byte)0xFF;
		// }
		// ByteBuffer buffer = ByteBuffer.wrap(array);
		
		id = glCreateTextures(GL_TEXTURE_2D);
		bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, this.width, this.height);
		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, this.width, this.height, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) buff);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		unbind();

		STBImage.stbi_image_free(buff);

	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void bindAsTexture(int unit) {
		glActiveTexture(unit);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbindAsTexture(int unit) {
		glActiveTexture(unit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void delete(){
		glDeleteTextures(id);
	}

}
