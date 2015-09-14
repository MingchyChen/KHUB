package com.claridy.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(">>>>>>>>>>>>>>>>>>bool:"+ TestClass.isEmail("sdfsdf@qq.com"));
		scaleImage("D://picture//test.jpg" ,  "D://222.jpg" ,  90);
	}

	public static void scaleImage(InputStream imgInputStream,
			OutputStream imgOutputStream, int scale) {
		try {
			Image src = javax.imageio.ImageIO.read(imgInputStream);
			int width = (int) (src.getWidth(null) * scale / 100.0);
			int height = (int) (src.getHeight(null) * scale / 100.0);
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			bufferedImage.getGraphics().drawImage(
					src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
					0, 0, null);
			JPEGImageEncoder encoder = JPEGCodec
					.createJPEGEncoder(imgOutputStream);
			encoder.encode(bufferedImage);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void scaleImage(String imgSrc, String imgDist, int scale) {
		try {
			File file = new File(imgSrc);
			if (!file.exists()) {
				return;
			}
			InputStream is = new FileInputStream(file);
			OutputStream os = new FileOutputStream(imgDist);
			scaleImage(is, os, scale);
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判斷傳過來的參數是否是url格式
	 * 
	 * @param title
	 * @return
	 */
	public static boolean isUrl(String title) {
		String regex = "((http://)?([a-z]+[.])|(www.))\\w+[.]([a-z]{2,4})?[[.]([a-z]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z]{2,4}+|/?)";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(title);
		return mat.find();
	}

	/**
	 * 判斷傳過來的參數是否是email格式
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		Pattern pattern = Pattern.compile(".+@.+\\..+");
		Matcher matcher = pattern.matcher(strEmail);
		return matcher.matches();
	}
}
