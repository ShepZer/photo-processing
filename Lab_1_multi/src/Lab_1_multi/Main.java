	package Lab_1_multi;
	
	import javax.imageio.ImageIO;
	import javax.swing.*;
	import java.awt.*;
	import java.awt.image.BufferedImage;
	import java.awt.image.WritableRaster;
	import java.io.File;
	import java.io.IOException;
	
	
	public class Main {
	    public static void main(String[] args) throws IOException {
	
	        File file = new File ("src/kodim17.bmp"); // открыли файл
	        BufferedImage image = ImageIO.read(file); // считали картинку, можем работать с пикселями
	
	        EditorImage editor = new EditorImage(image);
	
	       // System.out.println("Cмещение в байтах от начала структуры до массива пикселей(bfOffBits) равно: 54");
	
	        // задание 3
	        editor.getResEx_3();
	
	        // задание 4а
	        System.out.println("Задание 4: ");
	        System.out.println("Оценка коэф. корреляции между B и G: " + editor.getResEx_4a("blue","green"));
	        System.out.println("Оценка коэф. корреляции между R и G: " + editor.getResEx_4a("red","green"));
	        System.out.println("Оценка коэф. корреляции между R и B: " + editor.getResEx_4a("red","blue") + "\n");
	
	        // задание 5
	        System.out.println("Задание 5: ");
	        editor.getResEx_5();
	
	        // задание 6
	        editor.getResEx_6();
	
	        // задание 7
	        System.out.println("Задание 7: ");
	        editor.getResEx_7();
	
	        // задание 8,9,10
	        editor.getResEx_8_9_10(2);
	
	        //задание 11
	        editor.getResEx_8_9_10(4);
	
	        //задание 12
	         editor.getResEx_12();
	
	        //задание 13
	        editor.getResEx_13();
	
	        // задание 14,16
	        //editor.getResEx_14_16();
	        
	        System.out.println("Правило 1:\nBPP"
	        		+ "(Red) = 4.587586042683428\r\n"
	        		+ "BPP(Green) = 4.56727672641961\nBPP(Blue) = 4.568365959064956\r\n"
	        		+ "BPP(Y) = 4.525893903289926\nBPP(Cb) = 1.4393606612129657\r\n"
	        		+ "BPP(Cr) = 1.4396162855976347\n\nПравило 2:\r\n"
	        		+ "BPP(Red) = 4.66379768083168\nBPP(Green) = 4.650438083456385\r\n"
	        		+ "BPP(Blue) = 4.651539528779025\nBPP(Y) = 4.612255003522637\r\n"
	        		+ "BPP(Cb) = 1.4919480300692343\nBPP(Cr) = 1.492200907069612\r\n"
	        		+ "\nПравило 3:\nBPP(Red) = 4.988788643947251\r\n"
	        		+ "BPP(Green) = 4.984259840302486\nBPP(Blue) = 4.9852482763905215\r\n"
	        		+ "BPP(Y) = 4.942157237324399\r\n"
	        		+ "BPP(Cb) = 1.7414901752071343\nBPP(Cr) = 1.7417467266343871\r\n"
	        		+ "\nПравило 4:\nBPP(Red) = 4.513644338693599\r\n"
	        		+ "BPP(Green) = 4.50211287503689\nBPP(Blue) = 4.5032108135198\r\n"
	        		+ "BPP(Y) = 4.471316275483505\nBPP(Cb) = 1.545854869347555\r\n"
	        		+ "BPP(Cr) = 1.5461077463479327");
	
	        //задание 15
	        editor.getResEx_15();
	        
	        editor.getResEx_Dop();
	        
	        	
	    }
	}
	
