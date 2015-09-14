package com.claridy.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.zkoss.image.Image;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageBinary {

	public static BufferedImage base64StringToImage(String image) throws IOException{
		BASE64Decoder decoder=new BASE64Decoder();
		byte[] bytes = decoder.decodeBuffer(image);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);   
        BufferedImage bi =ImageIO.read(bais);
        return bi;
	}
	
	public static String getImageBinary(Image image,File file) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		BASE64Encoder encoder=new BASE64Encoder();
		BufferedImage bi=ImageIO.read(file);
		String suffix=image.getName().substring(image.getName().indexOf(".")+1);
		ImageIO.write(bi,suffix, baos); 
		byte[] bytes=baos.toByteArray();
		return encoder.encodeBuffer(bytes).trim();
	} 
}
