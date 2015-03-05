package malte0811.multiDim.render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

public abstract class RenderAlgo {
	public static int mainShaderId;

	public abstract double[] getInitialParams();

	public abstract void render(double[][] vertices, int[][] edges,
			double[] options, boolean drawVertices, float[][] colors);

	public static void init() {
		int vsId = loadShader(System.getProperty("user.dir")
				+ "\\src\\vertex.glsl", GL20.GL_VERTEX_SHADER);
		int frId = loadShader(System.getProperty("user.dir")
				+ "\\src\\fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		mainShaderId = GL20.glCreateProgram();
		GL20.glAttachShader(mainShaderId, vsId);
		GL20.glAttachShader(mainShaderId, frId);
		// Position information will be attribute 0
		GL20.glBindAttribLocation(mainShaderId, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(mainShaderId, 1, "in_Color");
		GL20.glLinkProgram(mainShaderId);
		GL20.glValidateProgram(mainShaderId);
		System.out.println("Loaded shaders: " + mainShaderId);
		int errorCheckValue = GL11.glGetError();
		if (errorCheckValue != GL11.GL_NO_ERROR) {
			System.out.println("ERROR - Could not create the shaders:"
					+ GLU.gluErrorString(errorCheckValue));
			System.exit(-1);
		}
	}

	public static int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}

		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		return shaderID;
	}

	public void renderNoColor(float[][] rV, int[][] edges, int shader) {
		IntBuffer indices = BufferUtils.createIntBuffer(2 * edges.length);
		float[] tmp = { 0, 1 };
		FloatBuffer interL = BufferUtils.createFloatBuffer(8 * rV.length);
		int[] vId = new int[rV.length];
		int i2 = 0;
		for (int i = 0; i < rV.length; i++) {
			if (rV[i] != null) {
				interL.put(rV[i]);
				vId[i] = i2;
				interL.put(tmp);
				interL.put(tmp);
				interL.put(tmp);
				i2++;
			}
		}
		interL.flip();
		i2 = 0;
		for (int[] i : edges) {
			if (rV[i[0]] != null && rV[i[1]] != null) {
				i2 += 2;
				indices.put(vId[i[0]]);
				indices.put(vId[i[1]]);
			}
		}
		indices.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		// A VAO can have up to 16 attributes (VBO's) assigned to it by default
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		int vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices,
				GL15.GL_STATIC_DRAW);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		// A VBO is a collection of Vectors which in this case resemble the
		// location of each vertex.
		int vboInterId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboInterId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, interL, GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 32, 0);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 32, 16);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL20.glUseProgram(shader);
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_LINES, i2, GL11.GL_UNSIGNED_INT, 0);

		GL20.glUseProgram(0);
		// deselect
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		// Disable the VBO index from the VAO attributes list
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

		// Delete the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboInterId);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboiId);
	}

	public void renderColor(float[][] rV, int[][] edges, float[][] colors,
			int shader) {
		IntBuffer vboI = BufferUtils.createIntBuffer(2 * edges.length);
		for (int i = 0; i < 2 * edges.length; i++) {
			vboI.put(i);
		}
		vboI.flip();
		float[][] renderV = new float[rV.length][4];
		for (int i = 0; i < rV.length; i++) {
			renderV[i][0] = rV[i][0];
			renderV[i][1] = rV[i][1];
			renderV[i][3] = 1;
		}

		FloatBuffer verticesBuffer = BufferUtils
				.createFloatBuffer(8 * edges.length);
		FloatBuffer colorsBuf = BufferUtils.createFloatBuffer(8 * edges.length);
		int anzV = 0;
		for (int i = 0; i < edges.length; i++) {
			if (renderV[edges[i][0]] == null || renderV[edges[i][1]] == null) {
				continue;
			}
			anzV += 2;
			verticesBuffer.put(renderV[edges[i][0]]);
			verticesBuffer.put(renderV[edges[i][1]]);
			colorsBuf.put(colors[i]);
			colorsBuf.put(1);
			colorsBuf.put(colors[i]);
			colorsBuf.put(1);
		}
		colorsBuf.flip();
		verticesBuffer.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		// A VAO can have up to 16 attributes (VBO's) assigned to it by default
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		// A VBO is a collection of Vectors which in this case resemble the
		// location of each vertex.
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer,
				GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// colors
		int vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuf, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		int vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, vboI,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL20.glUseProgram(shader);
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_LINES, anzV, GL11.GL_UNSIGNED_INT, 0);
		GL20.glUseProgram(0);
		// deselect
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		// Disable the VBO index from the VAO attributes list
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);

		// Delete the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbocId);
	}
}
