package com.ydfind.image.biz.fft;

public class FFT {

//	double r_data[] = null;
//	double i_data[] = null;

    // compute the FFT of x[], assuming its length is a power of 2
    public static void fft(MyComplex[] src, int row, int width, MyComplex[] dest) {

        MyComplex[] temp = new MyComplex[width];
        for (int k = 0; k < width; k++) {
            temp[k] = src[row * width + k];
        }
        temp = fft(temp);
        //set output
        for (int k = 0; k < width; k++) {
            dest[row * width + k] = temp[k];
        }
    }
    public static MyComplex[] fft(MyComplex[] x) {
        int N = x.length;

        // base case
        if (N == 1) {
            return new MyComplex[]{x[0]};
        }

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        MyComplex[] even = new MyComplex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        MyComplex[] q = fft(even);

        // fft of odd terms
        MyComplex[] odd = even; // reuse the array
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        MyComplex[] r = fft(odd);

        // combine
        MyComplex[] y = new MyComplex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            MyComplex wk = new MyComplex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }
}
