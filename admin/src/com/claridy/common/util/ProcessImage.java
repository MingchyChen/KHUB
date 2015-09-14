package com.claridy.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ProcessImage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProcessImage.scaleImage("D://picture//qq.jpg","D://qq.jpg");
		File picture = new File("D://qq.jpg");    
        System.out.println(String.format("%.1f",picture.length()/1024.0));  
	}

	public static void scaleImage(InputStream imgInputStream,
			OutputStream imgOutputStream) {
		System.out.println(new Date());
		try {
			Image src = javax.imageio.ImageIO.read(imgInputStream);
			int width;
			int height;
			// 为等比缩放计算输出的图片宽度及高度
			double rate1 = ((double) src.getWidth(null))
					/ (double) 940 + 0.1;
			double rate2 = ((double) src.getHeight(null))
					/ (double) 1160 + 0.1;
			// 根据缩放比率大的进行缩放控制
			double rate = rate1 > rate2 ? rate1 : rate2;
			width = (int) (((double) src.getWidth(null)) / rate);
			height = (int) (((double) src.getHeight(null)) / rate);
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			bufferedImage.getGraphics().drawImage(
					src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
					0, 0, null);
			JPEGImageEncoder encoder = JPEGCodec
					.createJPEGEncoder(imgOutputStream);
			encoder.encode(bufferedImage);
			imgInputStream.close();
			imgOutputStream.close();
			System.out.println(new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void scaleImage(String imgSrc, String imgDist) {
		try {
			File file = new File(imgSrc);
			if (!file.exists()) {
				return;
			}
			InputStream is = new FileInputStream(file);
			OutputStream os = new FileOutputStream(imgDist);
			scaleImage(is, os);
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
