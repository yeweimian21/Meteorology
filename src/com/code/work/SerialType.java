package com.code.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SerialType {
	
	public static void main(String[] args) throws Exception {
        
		long startTime=System.currentTimeMillis();		//程序开始时的系统时间
		System.out.println("start");
		
		double result=0,a=0,b=0,x=0;
		String regex="\\s+";		//正则表达式，匹配一个或者多个空白字符
		
		/*打开文件，创建输入流，读取数据*/
		String fileAPath="D:/data/paralleldata/A.txt";
		File fileA=new File(fileAPath);
		FileReader readerA=new FileReader(fileA);
		BufferedReader bufferedReaderA=new BufferedReader(readerA);
		
		String fileBPath="D:/data/paralleldata/b.txt";
		File fileB=new File(fileBPath);
		FileReader readerB=new FileReader(fileB);
		BufferedReader bufferedReaderB=new BufferedReader(readerB);
		
		String fileXPath="D:/data/paralleldata/x.txt";
		File fileX=new File(fileXPath);
		FileReader readerX=new FileReader(fileX);
		BufferedReader bufferedReaderX=new BufferedReader(readerX);
		
		for (int latitude=0; latitude<360; latitude++) {
			for (int longitude=0; longitude<180; longitude++) {
				for (int altitude=0; altitude<38; altitude++) {
					double temp=0;		//temp为中间结果
					//一行一行读取，去除两端空格
					String xline=bufferedReaderX.readLine().trim();
					if(xline!=null) {
						//将读取的字符串按照空白字符分割为字符串数组
						String[] xArray=xline.split(regex);
						x=Double.parseDouble(xArray[3]);
					}
					String bline=bufferedReaderB.readLine().trim();
					if(bline!=null) {
						String[] bArray=bline.split(regex);
						b=Double.parseDouble(bArray[3]);
					}
					for (int adjacent_point = 0; adjacent_point <19; adjacent_point++){
	
						String aline=bufferedReaderA.readLine().trim();
						if(aline!=null) {
							String[] aArray=aline.split(regex);
							a=Double.parseDouble(aArray[4]);
							temp+=a*x;
						}
					}
				
					temp=temp-b;
					temp=Math.pow(temp, 2);
					
					//result累加中间结果temp
					result+=temp;
				}
			}
		}
		
		System.out.println("Result:  "+Math.pow(result, 0.5));
		//Result:  0.4464126889370735
		//关闭文件流
		bufferedReaderA.close();bufferedReaderX.close();bufferedReaderB.close();
		long endTime=System.currentTimeMillis();	//程序结束时的系统时间
		String time=(endTime-startTime)/1000+"s";	//毫秒数除以1000转换为秒
		System.out.println("Time:  "+time);
		System.out.println("end");
	}
}
