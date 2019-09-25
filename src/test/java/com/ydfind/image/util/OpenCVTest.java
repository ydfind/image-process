//import org.junit.Test;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.highgui.HighGui;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * OpenCV测试
// * @author ydfind
// * @date 2019.9.20
// */
//public class OpenCVTest {
//
//    /**
//     * 测试OpenCV是否能运行：需要自行修改图片位置
//     * @throws Exception 测试是否成功
//     */
//    @Test
//    public void testOpenCV() throws Exception {
//        System.load("D:\\PData\\baidunet\\image-process\\src\\main\\resources\\lib\\opencv_java411.dll");
//        handler();
//    }
//
//    private void handler() throws Exception {
//        String src = "D:\\Utemp\\image\\example-equalizeHist.png";
//        String trg = "D:\\Utemp\\image\\example-equalizeHist-out.png";
//        // 读取显示原图片
//        Mat image = Imgcodecs.imread(src, 1);
//        if (image.empty()){
//            throw new Exception("image is empty!");
//        }
////        HighGui.imshow("Original Image", image);
//        // 处理
//        List<Mat> imageRGB = new ArrayList<>();
//        Core.split(image, imageRGB);
//        for (int i = 0; i < 3; i++) {
//            Imgproc.equalizeHist(imageRGB.get(i), imageRGB.get(i));
//        }
//        Core.merge(imageRGB, image);
//        // 显示处理后图片
////        HighGui.imshow("Processed Image", image);
//
//        Imgcodecs.imwrite(trg, image);
////        HighGui.waitKey();
//    }
//}
