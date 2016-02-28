package edu.orissermaroix.phash64;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


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
	public static long computeHash(String imagePath) 
			throws FileNotFoundException, IOException {
		final int SIZE = 8;
		
		//process image
		BufferedImage bim = ImageIO.read(new FileInputStream(imagePath));
		Image resizedImg = bim.getScaledInstance(SIZE, SIZE, Image.SCALE_FAST);
		BufferedImage rBimg = new BufferedImage(SIZE, SIZE, bim.getType());

		//draw image
		Graphics2D g = rBimg.createGraphics();
		g.drawImage(resizedImg, 0, 0, null);
		g.dispose(); //optionnal... work without...

		int avg = 0;
		int[] img = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE; i++)
			for (int j = 0, k; j < SIZE; j++) {
				k = i * SIZE + j;
				Color c = new Color(rBimg.getRGB(i, j));
				img[k] = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
				avg += img[k];
			}
		avg /= (SIZE * SIZE);

		//create the hash
		long hash = 0;
		for (int i = 0; i < SIZE * SIZE; i++)
			hash = hash << 1 | (img[i] > avg ? 1 : 0);
		
		return hash;
	}
	
	/**
	 * Some tests...
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected static void demo() throws FileNotFoundException, IOException {
		long a = computeHash("res/a.jpg");
		long b = computeHash("res/b.jpg");
		long c = computeHash("res/c.jpg");
		
		System.out.println(hamming(a, a)); //a & a are same
		System.out.println(hamming(a, b)); //a & b are similar
		System.out.println(hamming(a, c)); //a & c are different
		System.out.println(hamming(c, b)); //b & c are different
		System.out.println(hamming(b, c)); //commutativity
	}
	
	/**
	 * This function display how to use this class from command line.
	 */
	protected static void usage() {
		System.out.println("Usage : ");
		System.out.println("java edu.orissermaroix.phash64.PHash64 hash <img_path>");
		System.out.println("java edu.orissermaroix.phash64.PHash64 distance <img_path1> <img_path2>");
	}
	
	/**
	 * This function is used for using this class from the command line.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) 
			throws FileNotFoundException, IOException{
		try {
			String cmd = args[0];
			if (cmd.equals("demo"))
				demo();
			else if (cmd.equals("hash"))
				System.out.println(computeHash(args[1]));
			else if (cmd.equals("distance"))
				System.out.println(hamming(computeHash(args[1]), computeHash(args[2])));
		} catch (ArrayIndexOutOfBoundsException e) {
			usage();
		}
	}
}
