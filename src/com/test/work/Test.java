package com.test.work;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Test {
	public static void main(String[] args) throws Exception {
//		double result=0,a=0;
//		String regex="\\s+";
//		
//		String fileAPath="D:/data/paralleldata/A.txt";
//		File fileA=new File(fileAPath);
//		FileReader readerA=new FileReader(fileA);
//		BufferedReader bufferedReaderA=new BufferedReader(readerA);
		
		
//		String[] aArray=aline.split(regex);
//		System.out.println("a length"+aArray.length);
//		for(int i=0;i<aArray.length;i++) {
//			System.out.println("|"+aArray[i]);
//		}
//		aline=bufferedReaderA.readLine().trim();
//		aArray=aline.split(regex);
//		for(int i=0;i<aArray.length;i++) {
//			System.out.println("|"+aArray[i]);
//		}
		
//		for(int i=0;i<22;i++) {
//			String aline=bufferedReaderA.readLine().trim();
//			if(aline!=null) {
//				String[] aArray=aline.split(regex);
//				System.out.println(Double.parseDouble(aArray[4]));
//				for(int j=0;j<aArray.length;j++) {
//					System.out.print("|"+aArray[j]);
//					
//				}
//				System.out.println();
//			}
//		}
		
		double[] a=new double[19];
		a[0]=1.000000000000000000000000000000;
		a[1]=-0.008252899162471294403076171875;
		a[2]=0.008252899162471294403076171875;
		a[3]=0.000037320332921808585524559021;
		a[4]=-0.000037320332921808585524559021;
		a[5]=0.000000000000000000000000000000;
		a[6]=0.000000000000000000000000000000;
		a[7]=0.000000000000000000000000000000;
		a[8]=0.000000000000000000000000000000;
		a[9]=0.000000000000000000000000000000;
		a[10]=0.000000000000000000000000000000;
		a[11]=0.000000000000000000000000000000;
		a[12]=0.000000000000000000000000000000;
		a[13]=0.000000000000000000000000000000;
		a[14]=-1.000000000000000000000000000000;
		a[15]=-0.008252899162471294403076171875;
		a[16]=0.008252899162471294403076171875;
		a[17]=0.000037320332921808585524559021;
		a[18]=-0.000037320332921808585524559021;

		double x=-0.000333309173583984375000000000;
		double b=0.000659873359836637973785400391;
		
		double temp=0;
		for(int i=0;i<19;i++) {
			temp+=a[i]*x;
		}
		temp=temp-b;
		temp=Math.pow(temp, 2);
		System.out.println(temp);
	}
}
