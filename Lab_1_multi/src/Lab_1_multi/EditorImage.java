package Lab_1_multi;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//bi bit count

public class EditorImage {
    // ���.�. m[I(A)]
    public static double mathExp(double[][] color, int width, int height) {
        double res = 0;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                res += color[i][j];
            }
        }
        return res / (width * height);
    }
    // ������ �������������. ����������
    public static double devEstimate(double[][] color, int width, int height) {
        double res = 0;
        double math_O = mathExp(color, width, height);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                res += Math.pow(color[i][j] - math_O, 2);
            }
        }
        return Math.sqrt(res / ((width * height) - 1));
    }
    // ���������� ����.����������
    public static double cor(double[][] color1, double[][] color2, int width, int height) {
        double res = 0;
        double[][] numerator = new double[width][height];
        double mathE1 = mathExp(color1, width, height);
        double mathE2 = mathExp(color2, width, height);
        double devE1 = devEstimate(color1, width, height);
        double devE2 = devEstimate(color2, width, height);
        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j) {
                numerator[i][j] = (color1[i][j] - mathE1) * (color2[i][j] - mathE2);
                res += numerator[i][j];
            }
        res /= (width * height);
        return res / (devE1 * devE2);
    }
    // ���������� PSNR
    public static double PSNR(double[][] color, double[][] newColor, int width, int height) {
        double res = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                res += Math.pow(color[i][j] - newColor[i][j], 2); //SUM_SUM(I - I')^2
            }
        }
        res = (width * height * Math.pow(255, 2)) / res;
        res = 10 * Math.log10(res);
        return res;
    }
    // �������������� �� Y_Cb_Cr � RGB
    public static double clipping(double newColor){
        int max = 255;
        int min = 0;
        if (newColor > max){
            newColor = max;
        } else if (newColor < min){
            newColor = min;
        }
        return newColor;
    }
    // ��������� 1 ������ ����� ���������� ����� � �������� + ��������������
    public static double[][] decimation_and_normalSize_a(double[][] color, int width, int height, int n) {
        double[][] res = new double[width][height];
        for (int i = 0; i < width; i += n) {
            for (int j = 0; j < height; j += n) {
                res[i][j] = color[i][j];
            }
        }
        // ��������������
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (res[i][j] == 0 && i % n == 0)
                    res[i][j] = res[i][j - 1];
                else if (res[i][j] == 0)
                    res[i][j] = res[i - 1][j];
            }
        }

        return res;
    }
    // ��������� 2 ������ ����� ������ �������� ��������������� + ��������������
    public static double[][] decimation_and_normalSize_b(double[][] color, int width, int height, int n) {
        double[][] res = new double[width][height];
        for (int i = 0; i < width; i += n) {
            for (int j = 0; j < height; j += n) {
                res[i][j] = avg(color, i, j, n);

            }
        }
        // �������������
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (res[i][j] == 0 && i % n == 0)
                    res[i][j] = res[i][j - 1];
                else if (res[i][j] == 0)
                    res[i][j] = res[i - 1][j];
            }
        }
        return res;
    }
    public static double avg(double[][] color, int i, int j, int n) {
        double res = 0;
        for (int k = 0; k < n; k++) {
            for (int l = 0; l < n; l++) {
                if (i + k != color.length && j + l != color[i + k].length)
                    res += color[i + k][j + l];
            }
        }
        res = res / (double) (n * n);
        return res;
    }
    public static int[] calc(double[][] color, int width, int height){
        int[] ptr = new int[256];
        for (int x = 0; x < ptr.length; x++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int tmp = (int)color[i][j];
                    if (tmp == x) {
                        ptr[x]++; // � ������ x ���-�� ��������, ���������� ������� ����� x
                    }
                }
            }
        }
        return ptr;
    }
    //���������� log2
    public static double log2(double n) {
        if (n == 0)
            return 0;
        else
            return (Math.log(n) / Math.log(2));
    }
    //���������� ��������
    public static double entropy(double[][] color, int width, int height) {
        double H = 0;
        double[] p = calc_P(color, width, height);
        for (double pb : p) {
            H += pb * log2(pb);
        }
        return (H * (-1));
    }
    public static double[] calc_P(double[][] color, int width, int height) {
        // ����������� p(x)
        double[] p = new double[256];
        // N - ���������� �������� � ������ ����������, ������� ����� �������� x (0,255)
        int[] n = new int[256];
        for (int x = 0; x < n.length; x++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int tmp = (int)color[i][j];
                    if (tmp == x) {
                        n[x]++; // � ������ x ���-�� ��������, ���������� ������� ����� x
                    }
                }
            }
            p[x] = (double) n[x] / (height * width);
        }
        return p;
    }
    // ������ DPCM � ����� ��������
    public static double [][] DPCM(double[][] color, int width, int height,int flag){
        double[][] result = new double[width - 1][height - 1];
        switch (flag) {
            case 1:
                for (int i = 1,k = 0; i < width; i++,k++) {
                    for (int j = 1,t = 0; j < height; j++,t++) {
                        result[k][t] = color[i][j] - color[i][j - 1];
                    }
                }
                break;
            case 2:
                for (int i = 1,k = 0; i < width; i++,k++) {
                    for (int j = 1,t = 0; j < height; j++,t++) {
                        result[k][t] = color[i][j] - color[i - 1][j];
                    }
                }
                break;
            case 3:
                for (int i = 1,k = 0; i < width; i++,k++) {
                    for (int j = 1,t = 0; j < height; j++,t++) {
                        result[k][t] = color[i][j] - color[i - 1][j - 1];
                    }
                }
                break;
            case 4:
                for (int i = 1,k = 0; i < width; i++,k++) {
                    for (int j = 1,t = 0; j < height; j++,t++) {
                        result[k][t] = color[i][j] - ((color[i][j - 1] + color[i - 1][j] + color[i - 1][j - 1]) / 3);
                    }
                }
                break;
        }
       return result;
    }

    private final BufferedImage image;
    private final double[][] red,blue,green; // �������� �������� R G B
    private double[][] Y, Cb, Cr; // �������� �������� Y Cb Cr


   /*  private double[][] new_red, new_blue, new_green; // ����� �������������� �� Y Cb Cr � R G B

    private double correlCr_Cb,correlCr_Y,correlY_Cb;
    private double psnrRed,psnrGreen,psnrBlue; // �������� psnr �� �������� ������
    private double newPsnrRed,newPsnrGreen,newPsnrBlue, psnrCb, pnsrCr ; // �������� psnr �� �������� ������

     private double[][]  new_Cb_a, new_Cr_a; // ����� ��������� 1 ��������
     private double[][]  new_Cb_b, new_Cr_b; // ����� ��������� 2
     private double[][] new_red2_a, new_blue2_a, new_green2_a;
     private double[][] new_red2_b, new_blue2_b, new_green2_b;

    //��� �������� � 4 ����
    private double[][]  new_Cb_a_4, new_Cr_a_4;
    private double[][]  new_Cb_b_4, new_Cr_b_4;
    private double[][] new_red2_a_4, new_blue2_a_4, new_green2_a_4;
    private double[][] new_red2_b_4, new_blue2_b_4, new_green2_b_4;*/

    public EditorImage(BufferedImage image){
        this.image = image;
        red = new double[image.getWidth()][image.getHeight()];
        blue = new double[image.getWidth()][image.getHeight()];
        green = new double[image.getWidth()][image.getHeight()];
    }

    //3 ������� - ����������� � ������ ������
    public void getResEx_3() throws IOException {
        int height = image.getHeight(); // �����/������
        int width = image.getWidth();

        // ������� ��� ����� �����������
        BufferedImage imageRed = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        BufferedImage imageGreen = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        BufferedImage imageBlue = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        System.out.println(image);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++) {

                int colorRGB = image.getRGB(i, j); // ���� ���������� �������
                int red = (colorRGB & 0x00ff0000) >> 16; // ��������� ��������� ����������
                int green = (colorRGB & 0x0000ff00) >> 8;
                int blue = colorRGB & 0x000000ff;

                this.red[i][j] = red;
                this.green[i][j] = green;
                this.blue[i][j] = blue;

                Color pixRed = new Color(red,0,0);
                Color pixGreen = new Color(0,green,0);
                Color pixBlue = new Color(0,0,blue);

                imageRed.setRGB(i,j,pixRed.getRGB() );
                imageGreen.setRGB(i,j, pixGreen.getRGB() );
                imageBlue.setRGB(i,j,pixBlue.getRGB() );
            }
        }
        ImageIO.write(imageBlue,"bmp",new File("res/lab1_Blue.bmp"));
        ImageIO.write(imageRed,"bmp",new File("res/lab1_Red.bmp"));
        ImageIO.write(imageGreen,"bmp",new File("res/lab1_Green.bmp"));
    }

    // 4� ������� -  ����.����������
    public double getResEx_4a(String str1, String str2) {
        double[][] color1 = new double[image.getWidth()][image.getHeight()];
        double[][] color2 = new double[image.getWidth()][image.getHeight()];
        try {
            if (str1.equals("red") && str2.equals("green") || str1.equals("green") && str2.equals("red")) {
                color1 = this.red;
                color2 = this.green;
            } else if (str1.equals("red") && str2.equals("blue") || str1.equals("blue") && str2.equals("red")) {
                color1 = this.red;
                color2 = this.blue;
            } else if (str1.equals("green") && str2.equals("blue") || str1.equals("blue") && str2.equals("green")) {
                color1 = this.green;
                color2 = this.blue;
            } else throw new Exception("������ ����� ������!");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        double res = 0;
        double[][] tmp = new double[image.getWidth()][image.getHeight()];

        double math_o_1 = mathExp(color1, image.getWidth(), image.getHeight()); //m[I(A)]
        double math_o_2 = mathExp(color2, image.getWidth(), image.getHeight()); //m[I(B)]

        double devE1 = devEstimate(color1, image.getWidth(), image.getHeight()); // ����. ���������� �
        double devE2 = devEstimate(color2, image.getWidth(), image.getHeight()); // ����. ���������� B

        for (int i = 0; i < image.getWidth(); ++i){
            for (int j = 0; j < image.getHeight(); ++j) {
                tmp[i][j] = (color1[i][j] - math_o_1) * (color2[i][j] - math_o_2);
                res += tmp[i][j];
            }
        }
        res /= (image.getWidth() * image.getHeight());

        return res / (devE1 * devE2); // r A,B
    }

    // 5 ������� - �������������� � Y_Cb_Cr + ������ ����.���������
    public void getResEx_5(){

        Y = new double[image.getWidth()][image.getHeight()];
        Cb = new double[image.getWidth()][image.getHeight()];
        Cr = new double[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); ++i)
            for (int j = 0; j < image.getHeight(); ++j) {
                Y[i][j] = 0.299 * red[i][j] + 0.587 * green[i][j] + 0.114 * blue[i][j];
                Cb[i][j] = 0.5643 * (blue[i][j] - Y[i][j]) + 128;
                Cr[i][j] = 0.7132 * (red[i][j] - Y[i][j]) + 128;
            }

        double correlCr_Cb = cor(Cr, Cb, image.getWidth(), image.getHeight());
        double correlCr_Y = cor(Cr, Y , image.getWidth(), image.getHeight());
        double correlY_Cb = cor(Y, Cb, image.getWidth(), image.getHeight());

        System.out.println("������ ����. ���������� ����� Y � Cb: " + correlY_Cb);
        System.out.println("������ ����. ���������� ����� Y � Cr: " + correlCr_Y);
        System.out.println("������ ����. ���������� ����� Cr � Cb: " + correlCr_Cb + "\n");
    }

    // 6 ������� - ���������� � Y_Cb_Cr
    public void getResEx_6() throws IOException {

        // ������� ��� ����� �����������
        BufferedImage image_Y = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        BufferedImage image_Cb = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        BufferedImage image_Cr = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                Color pixY = new Color((int)Y[i][j],(int)Y[i][j],(int)Y[i][j]);
                Color pixCb = new Color((int)Cb[i][j],(int)Cb[i][j],(int)Cb[i][j]);
                Color pixCr = new Color((int)Cr[i][j],(int)Cr[i][j],(int)Cr[i][j]);

                image_Y.setRGB(i,j,pixY.getRGB() );
                image_Cb.setRGB(i,j, pixCb.getRGB() );
                image_Cr.setRGB(i,j,pixCr.getRGB() );

            }
        }
        ImageIO.write(image_Y,"bmp",new File("res/lab1_Y.bmp"));
        ImageIO.write(image_Cb,"bmp",new File("res/lab1_Cb.bmp"));
        ImageIO.write(image_Cr,"bmp",new File("res/lab1_Cr.bmp"));
    }

    // 7 ������� - �������� �������������� �� Y_Cb_Cr � RGB + ������ psnr
    public void getResEx_7() throws IOException {
        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        // ����� �������������� �� YCbCr � RGB:
        double [][] restore_green = new double[image.getWidth()][image.getHeight()];
        double [][] restore_red = new double[image.getWidth()][image.getHeight()];
        double [][] restore_blue = new double[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                restore_green[i][j] = Y[i][j] - 0.714 * (Cr[i][j] - 128) - 0.334 * (Cb[i][j] - 128);
                restore_red[i][j] = Y[i][j] + 1.402 * (Cr[i][j] - 128);
                restore_blue[i][j] = Y[i][j] + 1.772 * (Cb[i][j] - 128);

                // �������������(���������)
                restore_green[i][j] = clipping(restore_green[i][j]);
                restore_red[i][j] = clipping(restore_red[i][j]);
                restore_blue[i][j] = clipping(restore_blue[i][j]);

                Color pix = new Color((int) restore_red[i][j], (int) restore_green[i][j], (int) restore_blue[i][j]);
                newImage.setRGB(i, j, pix.getRGB());
            }
        }

        ImageIO.write(newImage,"bmp",new File("res/lab1_After_converting_ex7.bmp"));

        double psnrRed = PSNR(red,restore_red, image.getWidth(), image.getHeight());
        double psnrBlue = PSNR(blue,restore_blue, image.getWidth(), image.getHeight());
        double psnrGreen = PSNR(green,restore_green, image.getWidth(), image.getHeight());

        System.out.println("�������� PSNR ��� R: " + psnrRed );
        System.out.println("�������� PSNR ��� B: " + psnrBlue );
        System.out.println("�������� PSNR ��� G: " + psnrGreen + "\n");
    }

    /*  public void getResEx_8a() throws IOException {
      BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

      double[][] Cb_dec = decimation_a(Cb, image.getWidth(), image.getHeight(), 2);
      double[][] Cr_dec = decimation_a(Cr, image.getWidth(), image.getHeight(), 2);

      for (int i = 0; i < image.getWidth(); i++) {
          for (int j = 0; j < image.getHeight(); j++) {

              Color pix = new Color((int)Y[i][j], (int)Cb_dec[i][j], (int)Cr_dec[i][j]);
              newImage.setRGB(i, j, pix.getRGB());
          }
      }
      ImageIO.write(newImage,"bmp",new File("res/lab1_After_decimation(a).bmp"));
  }*/
    // 8,9,10 ������� - ��������� 2 + �������������� ��������� ������� + �������������� �� Y_Cb_Cr � RGB + psnr
    // + 11 ������� - ���������� 4 ...
    public void getResEx_8_9_10(int n) throws IOException {

        // ����� ��������� 1 ��������
        double[][]  dec_Cb_a = decimation_and_normalSize_a(Cb, image.getWidth(),image.getHeight(), n);
        double[][]  dec_Cr_a = decimation_and_normalSize_a(Cr, image.getWidth(),image.getHeight(), n);

        // ����� ��������� 2 ��������
        double[][] dec_Cb_b = decimation_and_normalSize_b(Cb, image.getWidth(),image.getHeight(), n);
        double[][] dec_Cr_b = decimation_and_normalSize_b(Cr, image.getWidth(),image.getHeight(), n);

        BufferedImage newImage1 = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        BufferedImage newImage2 = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        //�������� �������������� �� YCbCr � RGB
        double [][] new_green_dec_a = new double[image.getWidth()][image.getHeight()];
        double [][] new_red_dec_a = new double[image.getWidth()][image.getHeight()];
        double [][] new_blue_dec_a = new double[image.getWidth()][image.getHeight()];

        double[][] new_green_dec_b = new double[image.getWidth()][image.getHeight()];
        double[][] new_red_dec_b = new double[image.getWidth()][image.getHeight()];
        double [][] new_blue_dec_b= new double[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                new_green_dec_a[i][j] = Y[i][j] - 0.714 * (dec_Cr_a[i][j] - 128) - 0.334 * (dec_Cb_a[i][j] - 128);
                new_red_dec_a [i][j] = Y[i][j] + 1.402 * (dec_Cr_a[i][j] - 128);
                new_blue_dec_a[i][j] = Y[i][j] + 1.772 * (dec_Cb_a[i][j] - 128);

                new_green_dec_b[i][j] = Y[i][j] - 0.714 * (dec_Cr_b[i][j] - 128) - 0.334 * (dec_Cb_b[i][j] - 128);
                new_red_dec_b[i][j] = Y[i][j] + 1.402 * (dec_Cr_b[i][j] - 128);
                new_blue_dec_b[i][j] = Y[i][j] + 1.772 * (dec_Cb_b[i][j] - 128);

                // �������������(���������)
                new_green_dec_a[i][j] = clipping(new_green_dec_a[i][j]);
                new_red_dec_a [i][j] = clipping(new_red_dec_a [i][j]);
                new_blue_dec_a[i][j] = clipping(new_blue_dec_a[i][j]);

                new_green_dec_b[i][j] = clipping(new_green_dec_b[i][j]);
                new_red_dec_b[i][j] = clipping(new_red_dec_b[i][j]);
                new_blue_dec_b[i][j] = clipping(new_blue_dec_b[i][j]);

                Color pix = new Color((int) new_red_dec_a [i][j], (int) new_green_dec_a[i][j], (int) new_blue_dec_a[i][j]);
                Color pix2 = new Color((int) new_red_dec_b[i][j], (int) new_green_dec_b[i][j], (int) new_blue_dec_b[i][j]);
                newImage1.setRGB(i, j, pix.getRGB());
                newImage2.setRGB(i, j, pix2.getRGB());
            }
        }

        if (n == 2) {
            ImageIO.write(newImage1, "bmp", new File("res/lab1_After_decimation_2_ex_8_10_a.bmp"));
            ImageIO.write(newImage2, "bmp", new File("res/lab1_After_decimation_2_ex_8_10_b.bmp"));
        }else {
            ImageIO.write(newImage1, "bmp", new File("res/lab1_After_decimation_4_ex_11_a.bmp"));
            ImageIO.write(newImage2, "bmp", new File("res/lab1_After_decimation_4_ex_11_b.bmp"));
        }

        double newPsnrRed_a = PSNR(red,new_red_dec_a , image.getWidth(), image.getHeight());
        double newPsnrBlue_a = PSNR(blue,new_blue_dec_a, image.getWidth(), image.getHeight());
        double newPsnrGreen_a = PSNR(green,new_green_dec_a, image.getWidth(), image.getHeight());
        double psnrCb_a = PSNR(Cb,dec_Cb_a, image.getWidth(), image.getHeight());
        double pnsrCr_a = PSNR(Cr,dec_Cr_a, image.getWidth(), image.getHeight());

        double newPsnrRed_b = PSNR(red,new_red_dec_b, image.getWidth(), image.getHeight());
        double newPsnrBlue_b = PSNR(blue,new_blue_dec_b, image.getWidth(), image.getHeight());
        double newPsnrGreen_b = PSNR(green,new_green_dec_b, image.getWidth(), image.getHeight());
        double psnrCb_b = PSNR(Cb,dec_Cb_b, image.getWidth(), image.getHeight());
        double pnsrCr2_b = PSNR(Cr,dec_Cr_b, image.getWidth(), image.getHeight());

        if (n==2) {
            System.out.println("������ 8�-10:");
        } else System.out.println("����� 11a:");
        System.out.println("�������� PSNR ��� R: " + newPsnrRed_a  );
        System.out.println("�������� PSNR ��� B: " + newPsnrBlue_a );
        System.out.println("�������� PSNR ��� G: " + newPsnrGreen_a );
        System.out.println("�������� PSNR ��� Cb: " + psnrCb_a );
        System.out.println("�������� PSNR ��� Cr: " + pnsrCr_a + "\n");

        if (n==2) {
            System.out.println("������ 8�-10:");
        } else System.out.println("����� 11�:");
        System.out.println("�������� PSNR ��� R: " + newPsnrRed_b );
        System.out.println("�������� PSNR ��� B: " + newPsnrBlue_b );
        System.out.println("�������� PSNR ��� G: " + newPsnrGreen_b );
        System.out.println("�������� PSNR ��� Cb: " + psnrCb_b );
        System.out.println("�������� PSNR ��� Cr: " + pnsrCr2_b + "\n");
    }

    public void getResEx_12() throws IOException {
        FileWriter histR = new FileWriter("src/p12_txt/HistRed.txt");
        FileWriter histB = new FileWriter("src/p12_txt/HistBlue.txt");
        FileWriter histG = new FileWriter("src/p12_txt/HistGreen.txt");
        FileWriter histY = new FileWriter("src/p12_txt/HistY.txt");
        FileWriter histCb = new FileWriter("src/p12_txt/HistCb.txt");
        FileWriter histCr = new FileWriter("src/p12_txt/HistCr.txt");
        //calc(double[][] color, int width, int height)

        int [] ptrR = calc(red, image.getWidth(), image.getHeight());
        int [] ptrB = calc(blue, image.getWidth(), image.getHeight());
        int [] ptrG = calc(green, image.getWidth(), image.getHeight());
        int [] ptrY = calc(Y, image.getWidth(), image.getHeight());
        int [] ptrCb = calc(Cb, image.getWidth(), image.getHeight());
        int [] ptrCr = calc(Cr, image.getWidth(), image.getHeight());

        for (int x = 0; x < ptrR.length; x++) {

            histR.write(x + "\t" + ptrR[x] + "\n");
            histR.flush();

            histB.write(x + "\t" + ptrB[x] + "\n");
            histB.flush();

            histG.write(x + "\t" + ptrG[x] + "\n");
            histG.flush();

            histY.write(x + "\t" + ptrY[x] + "\n");
            histY.flush();

            histCb.write(x + "\t" + ptrCb[x] + "\n");
            histCb.flush();

            histCr.write(x + "\t" + ptrCr[x] + "\n");
            histCr.flush();
        }
    }

    //13 ������� - ��������
    public void getResEx_13(){
        System.out.println("������� 13:");
        System.out.println("�������� R �����: " + entropy(red, image.getWidth(), image.getHeight()));
        System.out.println("�������� B �����: " + entropy(blue, image.getWidth(), image.getHeight()));
        System.out.println("�������� G �����: " + entropy(green, image.getWidth(), image.getHeight()));
        System.out.println("�������� Y �����: " + entropy(Y, image.getWidth(), image.getHeight()));
        System.out.println("�������� Cb �����: " + entropy(Cb, image.getWidth(), image.getHeight()));
        System.out.println("�������� Cr �����: " + entropy(Cr, image.getWidth(), image.getHeight()) + "\n");
    }

    // ������ DPCM + ��������
    public void getResEx_14_16(){
        System.out.println("������� 14,16: ");
        for (int i = 1; i < 5; i++) {
            double[][] red_DPCM = DPCM(red, image.getWidth(), image.getHeight(), i);
            double[][] green_DPCM = DPCM(green, image.getWidth(), image.getHeight(), i);
            double[][]  blue_DPCM = DPCM(blue, image.getWidth(), image.getHeight(), i);
            double[][]  Y_DPCM = DPCM(Y, image.getWidth(), image.getHeight(), i);
            double[][]  Cb_DPCM = DPCM(Cb, image.getWidth(), image.getHeight(), i);
            double[][]  Cr_DPCM = DPCM(Cr, image.getWidth(), image.getHeight(), i);



            System.out.println("������� " + i);
            System.out.println("�������� R �����: " + entropy(red_DPCM, image.getWidth() - 1, image.getHeight() - 1) );
            System.out.println("�������� B �����: " + entropy(blue_DPCM, image.getWidth() - 1, image.getHeight()- 1));
            System.out.println("�������� G �����: " + entropy(green_DPCM, image.getWidth() - 1, image.getHeight() - 1));
            System.out.println("�������� Y �����: " + entropy(Y_DPCM, image.getWidth() - 1, image.getHeight() - 1));
            System.out.println("�������� Cb �����: " + entropy(Cb_DPCM, image.getWidth() - 1, image.getHeight() - 1));
            System.out.println("�������� Cr �����: " + entropy(Cr_DPCM, image.getWidth() - 1, image.getHeight() - 1) + "\n");
        }
    }

    // 15 ������� - ����������� DPCM
    public void getResEx_15() throws IOException {
        FileWriter histR = new FileWriter("src/p15_txt/HistRedDPCM.txt");
        FileWriter histB = new FileWriter("src/p15_txt/HistBlueDPCM.txt");
        FileWriter histG = new FileWriter("src/p15_txt/HistGreenDPCM.txt");
        FileWriter histY = new FileWriter("src/p15_txt/HistYDPCM.txt");
        FileWriter histCb = new FileWriter("src/p15_txt/HistCbDPCM.txt");
        FileWriter histCr = new FileWriter("src/p15_txt/HistCrDPCM.txt");

        int i = 1;
        double[][] red_DPCM = DPCM(red, image.getWidth(), image.getHeight(), i);
        double[][] green_DPCM = DPCM(green, image.getWidth(), image.getHeight(), i);
        double[][]  blue_DPCM = DPCM(blue, image.getWidth(), image.getHeight(), i);
        double[][]  Y_DPCM = DPCM(Y, image.getWidth(), image.getHeight(), i);
        double[][]  Cb_DPCM = DPCM(Cb, image.getWidth(), image.getHeight(), i);
        double[][]  Cr_DPCM = DPCM(Cr, image.getWidth(), image.getHeight(), i);

        int [] ptrR = calc(red_DPCM, image.getWidth() - 1, image.getHeight() - 1);
        int [] ptrB = calc(blue_DPCM, image.getWidth() - 1, image.getHeight() - 1);
        int [] ptrG = calc(green_DPCM, image.getWidth() - 1, image.getHeight() - 1);
        int [] ptrY = calc(Y_DPCM, image.getWidth() - 1, image.getHeight() - 1);
        int [] ptrCb = calc(Cb_DPCM, image.getWidth() - 1, image.getHeight() - 1);
        int [] ptrCr = calc(Cr_DPCM, image.getWidth() - 1, image.getHeight() - 1);

        for (int x = 0; x < ptrR.length; x++) {

            histR.write(x + "\t" + ptrR[x] + "\n");
            histR.flush();

            histB.write(x + "\t" + ptrB[x] + "\n");
            histB.flush();

            histG.write(x + "\t" + ptrG[x] + "\n");
            histG.flush();

            histY.write(x + "\t" + ptrY[x] + "\n");
            histY.flush();

            histCb.write(x + "\t" + ptrCb[x] + "\n");
            histCb.flush();

            histCr.write(x + "\t" + ptrCr[x] + "\n");
            histCr.flush();
        }
    }
    
    public int Sat(int x, int min, int max) throws IOException {
    	if(x < min) {
    		x = min;
    	}
    	else if(x > max) {
    		x = max;
    	}
    	else {}
    	return x;
    }

    public void getResEx_Dop() throws IOException {
    	
    	int height = image.getHeight(); // �����/������
        int width = image.getWidth();

        // ������� ��� ����� �����������
        BufferedImage imageDop = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        //BufferedImage imageGreen = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        //BufferedImage imageBlue = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        //System.out.println(image);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++) {

                int colorRGB = image.getRGB(i, j); // ���� ���������� �������
                int red = (colorRGB & 0x00ff0000) >> 16; // ��������� ��������� ����������
                int green = (colorRGB & 0x0000ff00) >> 8;
                int blue = colorRGB & 0x000000ff;
                
                this.red[i][j] = red;
                this.green[i][j] = green;
                this.blue[i][j] = blue;
                
                red = Sat(red - 50, 0, 255);
                
                green = Sat(green - 50, 0, 255);
                
                blue = Sat(blue - 50, 0, 255);
                
                Color Up = new Color(red,green,blue);

                //Color pixRed = new Color(red,0,0);
                //Color pixGreen = new Color(0,green,0);
                //Color pixBlue = new Color(0,0,blue);

                imageDop.setRGB(i,j,Up.getRGB() );
                //imageGreen.setRGB(i,j, pixGreen.getRGB() );
                //imageBlue.setRGB(i,j,pixBlue.getRGB() );
            }
        }
        ImageIO.write(imageDop,"bmp",new File("res/lab1_Dop_rgb.bmp"));
        
        double [][] newY = new double[image.getWidth()][image.getHeight()];
        double [][] restore_green = new double[image.getWidth()][image.getHeight()];
        double [][] restore_red = new double[image.getWidth()][image.getHeight()];
        double [][] restore_blue = new double[image.getWidth()][image.getHeight()];
        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++) {
            	newY[i][j] = Y[i][j] - 50;
            	
            	newY[i][j]= Sat((int)newY[i][j], 0, 255);
            	
            	restore_green[i][j] = newY[i][j] - 0.714 * (Cr[i][j] - 128) - 0.334 * (Cb[i][j] - 128);
                restore_red[i][j] = newY[i][j] + 1.402 * (Cr[i][j] - 128);
                restore_blue[i][j] = newY[i][j] + 1.772 * (Cb[i][j] - 128);

                // �������������(���������)
                restore_green[i][j] = clipping(restore_green[i][j]);
                restore_red[i][j] = clipping(restore_red[i][j]);
                restore_blue[i][j] = clipping(restore_blue[i][j]);
                
            	Color col_Y = new Color((int)restore_red[i][j], (int)restore_green[i][j], (int)restore_blue[i][j]);
            	
            	newImage.setRGB(i, j, col_Y.getRGB());
            }
        }
        ImageIO.write(newImage, "bmp", new File("res/lab1_Dop_Y.bmp"));
    }
}
