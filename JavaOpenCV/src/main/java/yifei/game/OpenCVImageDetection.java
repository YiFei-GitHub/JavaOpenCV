package yifei.game;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

public class OpenCVImageDetection {

    static {
        nu.pattern.OpenCV.loadLocally(); // 自动从Maven下载并加载本地库
    }

    public static void main(String[] args) {
        try {
            // 从resources加载模板图像
            InputStream templateStream = OpenCVImageDetection.class.getClassLoader().getResourceAsStream("target2.png");

            // JDK8兼容的读取字节流方法
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = templateStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] imageBytes = buffer.toByteArray();

            Mat template = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

            Robot robot = new Robot();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            // 设置匹配阈值
            double threshold = 0.8;

            while (true) {
                // 1. 截取屏幕
                BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(screenSize));

                // 2. 转换为OpenCV Mat格式
                Mat screenMat = bufferedImageToMat(screenCapture);

                // 3. 使用模板匹配
                Mat result = new Mat();
                Imgproc.matchTemplate(screenMat, template, result, Imgproc.TM_CCOEFF_NORMED);

                // 4. 寻找最佳匹配位置
                Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
                if (mmr.maxVal > threshold) {
                    Point matchLoc = mmr.maxLoc;

                    // 计算目标中心位置
                    int centerX = (int)(matchLoc.x + template.cols() / 2);
                    int centerY = (int)(matchLoc.y + template.rows() / 2);

                    // 移动并双击
                    robot.mouseMove(centerX, centerY);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(50);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                    System.out.printf("找到目标，位置: (%d, %d)，置信度: %.2f%n",
                            centerX, centerY, mmr.maxVal);
                }

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Mat bufferedImageToMat(BufferedImage bi) {
        // 根据BufferedImage类型进行不同处理
        if (bi.getType() == BufferedImage.TYPE_INT_RGB ||
                bi.getType() == BufferedImage.TYPE_INT_ARGB) {
            // 处理INT_RGB或INT_ARGB格式
            return convertIntBufferedImageToMat(bi);
        } else {
            // 处理其他格式（如TYPE_3BYTE_BGR）
            return convertByteBufferedImageToMat(bi);
        }
    }

    // 处理INT_RGB/INT_ARGB格式的BufferedImage
    private static Mat convertIntBufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = new byte[bi.getWidth() * bi.getHeight() * 3];

        int[] pixelData = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                int pixel = pixelData[y * bi.getWidth() + x];
                data[(y * bi.getWidth() + x) * 3] = (byte)((pixel >> 16) & 0xFF);     // B
                data[(y * bi.getWidth() + x) * 3 + 1] = (byte)((pixel >> 8) & 0xFF);  // G
                data[(y * bi.getWidth() + x) * 3 + 2] = (byte)(pixel & 0xFF);          // R
            }
        }
        mat.put(0, 0, data);
        return mat;
    }

    // 处理TYPE_3BYTE_BGR格式的BufferedImage
    private static Mat convertByteBufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}