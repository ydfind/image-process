package com.ydfind.image.biz.fft;

import lombok.Data;

/**
 * 复数类
 * @author ydfind
 * @date 2019.10.17
 */
@Data
public class MyComplex {

    private double real;
    private double imaginary;

    public MyComplex(){
        this.real = 0.0;
        this.imaginary = 0.0;
    }

    public MyComplex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * 绝对值，或幅度
     * @return （real * real + imaginary * imaginary）的平方根
     */
    public double abs()   {
        return Math.hypot(real, imaginary);
    }

    /**
     * 平方
     * @return real * real + imaginary * imaginary
     */
    public double power()   {
        return real * real + imaginary * imaginary;
    }

    /**
     * 反正切
     * @return atan(real/imaginary);
     */
    public double atan() {
        return Math.atan2(real, imaginary);
    }

    /**
     * 复数加法
     * @param complex 第二个参数
     * @return 计算结果
     */
    public MyComplex plus(MyComplex complex) {
        return new MyComplex(this.real + complex.real, this.imaginary + complex.imaginary);
    }

    /**
     * 复数减法
     * @param complex 第二个参数
     * @return 计算结果
     */
    public MyComplex minus(MyComplex complex) {
        return new MyComplex(this.real - complex.real, this.imaginary - complex.imaginary);
    }

    /**
     * 复数乘积
     * @param complex 第二个参数
     * @return 计算结果
     */
    public MyComplex times(MyComplex complex) {
        double real = this.real * complex.real - this.imaginary * complex.imaginary;
        double imag = this.real * complex.imaginary + this.imaginary * complex.real;
        return new MyComplex(real, imag);
    }

    /**
     * 乘法:（a+bi）(c+di)=(ac−bd)+(ad+bc)i
     * @param alpha 参数
     * @return 计算结果
     */
    public MyComplex times(double alpha) {
        return new MyComplex(alpha * real, alpha * imaginary);
    }

    /**
     * 共轭计算
     * @return 计算结果
     */
    public MyComplex conjugate() {
        return new MyComplex(real, -imaginary);
    }

    /**
     * 倒数
     * @return 计算结果
     */
    public MyComplex reciprocal() {
        double scale = real * real + imaginary * imaginary;
        return new MyComplex(real / scale, -imaginary / scale);
    }

    /**
     * 除法
     * @param complex 第二个操作数
     * @return 计算结果
     */
    public MyComplex divides(MyComplex complex) {
        return times(complex.reciprocal());
    }

    /**
     * sin
     * @return 计算结果
     */
    public MyComplex sin() {
        return new MyComplex(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
    }

    /**
     * cos
     * @return 计算结果
     */
    public MyComplex cos() {
        return new MyComplex(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
    }

    /**
     * tan
     * @return 计算结果
     */
    public MyComplex tan() {
        return sin().divides(cos());
    }

    /**
     * e的复数次方
     * @return 计算结果
     */
    public MyComplex exp() {
        double expReal = Math.exp(real);
        return new MyComplex(expReal * Math.cos(imaginary), expReal * Math.sin(imaginary));
    }
}