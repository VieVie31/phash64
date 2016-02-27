package edu.orissermaroix.phash64;

import javax.imageio.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class PHash64 {
	/**
	 * This function compute the Hamming distance between 2 longs.
	 * See more about : https://en.wikipedia.org/wiki/Hamming_distance
	 * 
	 * @param a, a long
	 * @param b, a long
	 * @return the hamming distance between a and b as int
	 */
	public static int hamming(long a, long b) {
		int out = 0;

		long c = a ^ b;
		while (c != 0) {
			out += c & 1;
			c = c >>> 1;
		}

		return out;
	}

	/**
	 * This function compute the perceptual hash of an image.
	 * See more about : https://en.wikipedia.org/wiki/Perceptual_hashing
	 * 
	 * @param imagePath
	 * @return the hash as a long
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static long computeHash(String imagePath) throws FileNotFoundException, IOException {
		//process image
		BufferedImage bim = ImageIO.read(new FileInputStream(imagePath));
		Image resizedImg = bim.getScaledInstance(8, 8, Image.SCALE_FAST);
		BufferedImage rBimg = new BufferedImage(8, 8, bim.getType());

		//draw image
		Graphics2D g = rBimg.createGraphics();
		g.drawImage(resizedImg, 0, 0, null);
		g.dispose();

		long avg = 0;
		int[][] img = new int[8][8];
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				Color c = new Color(rBimg.getRGB(i, j));
				img[i][j] = (int) ((c.getRed() + c.getGreen() + c.getBlue()) / 3);
				avg += img[i][j];
			}

		avg /= 64;

		//create the hash
		long hash = 0;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				hash = hash << 1 | (img[i][j] > avg ? 1 : 0);

		return hash;
	}
}
