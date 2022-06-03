package openglModules;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL46C.*;

public class Texture {
	public int id;
	public int width;
	public int height;

	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
		id = glGenTextures();

		bind();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA8, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		unbind();

	}

	public Texture(int width, int height, String path) {
		this.width = width;
		this.height = height;
		id = glGenTextures();

		byte[] array = new byte[width * height * 4];
		for(int p = 0; p < width * height; p++){
			array[4*p + 0] = (byte)0xFF;
			array[4*p + 1] = (byte)0xFF;
			array[4*p + 2] = (byte)0xFF;
			array[4*p + 3] = (byte)0xFF;
		}
		ByteBuffer buffer = ByteBuffer.wrap(array);
		

// 		ByteBuffer buf = null;
// 		try {
// 			BufferedImage img = ImageIO.read(new File(path));
// 			System.out.println(img.getWidth() + " x " + img.getHeight() + " " + img.getType());
// 			ByteArrayOutputStream baos = new ByteArrayOutputStream();
// 			ImageIO.write(img, "png", baos);
// //			baos.flush();
// 			byte[] imageInByte = baos.toByteArray();
// 			baos.close();
// 			buf = ByteBuffer.wrap(imageInByte);
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}

		bind();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA8, GL_UNSIGNED_BYTE, (ByteBuffer) buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		unbind();

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

}
