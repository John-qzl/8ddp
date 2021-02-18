package com.cssrc.ibms.core.webseal.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @ClassName: ExtractWatermark
 * @Description: 提取图片水印
 * @author zxg
 * @date 2016年12月5日 下午3:03:34
 * 
 */
public class ExtractWatermark
{
    public static void main(String[] args)
    {
        ExtractWatermark distill = new ExtractWatermark();
        distill.start(32, 32);
    }
    
    public void start(int wWidth, int wHeight)
    {
        // mImage是嵌入水印后的图像
        BufferedImage mImage = ImageUtil.getImage("D://result.bmp");
        // 原始图像
        BufferedImage oImage = ImageUtil.getImage("D://lena.jpg");
        WritableRaster oRaster = oImage.getRaster();
        WritableRaster mRaster = mImage.getRaster();
        int oWidth = oRaster.getWidth();
        int oHeight = oRaster.getHeight();
        int[] oPixels = new int[3 * oWidth * oHeight];
        int[] mPixels = new int[3 * oWidth * oHeight];
        oRaster.getPixels(0, 0, oWidth, oHeight, oPixels);
        mRaster.getPixels(0, 0, oWidth, oHeight, mPixels);
        // 得rgb图像三层矩阵，mRgbPixels[0]表示b层分量
        int[][][] mRgbPixels = ImageUtil.getRGBArrayToMatrix(mPixels, oWidth, oHeight);
        int[][][] oRgbPixels = ImageUtil.getRGBArrayToMatrix(oPixels, oWidth, oHeight);
        double[][] oDPixels = MathTool.intToDoubleMatrix(mRgbPixels[2]);
        double[][] mDPixels = MathTool.intToDoubleMatrix(oRgbPixels[2]);
        double[][] result = new double[wWidth][wHeight];
        for (int i = 0; i < wWidth; i++)
        {
            for (int j = 0; j < wHeight; j++)
            {
                double[][] oBlk = new double[8][8];
                double[][] mBlk = new double[8][8];
                int d = 0;
                int f = 0;
                for (int m = 0; m < 8; m++)
                {
                    for (int n = 0; n < 8; n++)
                    {
                        oBlk[m][n] = oDPixels[8 * i + m][8 * j + n];
                        mBlk[m][n] = mDPixels[8 * i + m][8 * j + n];
                    }
                }
                double[][] dOBlk = FDct.fDctTransform(oBlk);
                double[][] dMBlk = FDct.fDctTransform(mBlk);
                if (dOBlk[3][3] > dMBlk[3][3])
                {
                    d++;
                }
                else
                {
                    f++;
                }
                if (dOBlk[3][4] > dMBlk[3][4])
                {
                    d++;
                }
                else
                {
                    f++;
                }
                if (dOBlk[3][5] > dMBlk[3][5])
                {
                    d++;
                }
                else
                {
                    f++;
                }
                if (dOBlk[4][3] > dMBlk[4][3])
                {
                    d++;
                }
                else
                {
                    f++;
                }
                if (dOBlk[5][3] > dMBlk[5][3])
                {
                    d++;
                }
                else
                {
                    f++;
                }
                if (d < f)
                {
                    result[i][j] = 0;
                }
                else
                {
                    result[i][j] = 1;
                }
            }
        }
        double[] outResult = ImageUtil.matrixToArray(result);
        // 把嵌入水印的结果写到BufferedImage对象
        ImageUtil.setImage(outResult, wWidth, wHeight, "D://mark.bmp", "bmp", BufferedImage.TYPE_BYTE_BINARY);
    }
    
}