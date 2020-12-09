package com.gdiot.ssm.util;

/**
 * @author ZhouHR
 */
public class JvmMemoryUtil {

    public static void memory() {
        long vmFree = 0;
        long vmUse = 0;
        long vmTotal = 0;
        long vmMax = 0;
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        vmTotal = rt.totalMemory() / byteToMb;
        vmFree = rt.freeMemory() / byteToMb;
        vmMax = rt.maxMemory() / byteToMb;
        vmUse = vmTotal - vmFree;
        System.out.println("JVM内存已用的空间为：" + vmUse + " MB");
        System.out.println("JVM内存的空闲空间为：" + vmFree + " MB");
        System.out.println("JVM总内存空间为：" + vmTotal + " MB");
        System.out.println("JVM总内存空间为：" + vmMax + " MB");
        System.out.println("===================================================");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        memory();
    }

}
