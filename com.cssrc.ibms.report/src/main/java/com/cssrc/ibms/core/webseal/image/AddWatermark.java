package com.cssrc.ibms.core.webseal.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @ClassName: AddWatermark
 * @Description: 图片添加水印
 * @author zxg
 * @date 2016年12月5日 下午3:02:59
 * 
 */
public class AddWatermark
{
    private static final int d = 5;
    
    public static void main(String[] args)
    {
        AddWatermark embed = new AddWatermark();
        embed.start();
    }
    
    public void start()
    {
        BufferedImage oImage = ImageUtil.getImage("D://lena.jpg");
        BufferedImage wImage = ImageUtil.getImage("D://zhong.bmp");
        int type = oImage.getType();
        WritableRaster oRaster = oImage.getRaster();
        WritableRaster wRaster = wImage.getRaster();
        int oWidth = oRaster.getWidth();
        int oHeight = oRaster.getHeight();
        int wWidth = wRaster.getWidth();
        int wHeight = wRaster.getHeight();
        int[] oPixels = new int[3 * oWidth * oHeight];
        int[] wPixels = new int[wWidth * wHeight];
        oRaster.getPixels(0, 0, oWidth, oHeight, oPixels);
        wRaster.getPixels(0, 0, wWidth, wHeight, wPixels);
        int[][][] RGBPixels = ImageUtil.getRGBArrayToMatrix(oPixels, oWidth, oHeight);
        // 得到RGB图像的三层矩阵表示
        double[][] rPixels = MathTool.intToDoubleMatrix(RGBPixels[2]);
        int[][] wDMatrix = ImageUtil.arrayToMatrix(wPixels, wWidth, wHeight);
        double[][] result = rPixels;
        // 嵌入算法
        for (int i = 0; i < wWidth; i++)
        {
            for (int j = 0; j < wHeight; j++)
            {
                double[][] blk = new double[8][8];
                // 对原始图像8 * 8 分块
                for (int m = 0; m < 8; m++)
                {
                    for (int n = 0; n < 8; n++)
                    {
                        blk[m][n] = rPixels[8 * i + m][8 * j + n];
                    }
                }
                double[][] dBlk = FDct.fDctTransform(blk);
                if (wDMatrix[i][j] == 0)
                {
                    dBlk[3][3] = dBlk[3][3] - d;
                    dBlk[3][4] = dBlk[3][4] - d;
                    dBlk[3][5] = dBlk[3][5] - d;
                    dBlk[4][3] = dBlk[4][3] - d;
                    dBlk[5][3] = dBlk[5][3] - d;
                }
                else
                {
                    dBlk[3][3] = dBlk[3][3] + d;
                    dBlk[3][4] = dBlk[3][4] + d;
                    dBlk[3][5] = dBlk[3][5] + d;
                    dBlk[4][3] = dBlk[4][3] + d;
                    dBlk[5][3] = dBlk[5][3] + d;
                }
                blk = IFDct.iFDctTransform(dBlk);
                for (int m = 0; m < 8; m++)
                {
                    for (int n = 0; n < 8; n++)
                    {
                        result[8 * i + m][8 * j + n] = blk[m][n];
                    }
                }
            }
        }
        double[][][] temp = new double[3][oWidth][oHeight];
        temp[0] = MathTool.intToDoubleMatrix(RGBPixels[0]);
        temp[1] = MathTool.intToDoubleMatrix(RGBPixels[1]);
        temp[2] = result;
        double[] rgbResult = ImageUtil.getRGBMatrixToArray(temp);
        // 将BufferedImage对象写入磁盘
        ImageUtil.setImage(rgbResult, oWidth, oHeight, "D://result.bmp", "bmp", type);
    }
    
}