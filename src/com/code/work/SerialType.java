package com.code.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SerialType {
	
	public static void main(String[] args) throws Exception {
        
		long startTime=System.currentTimeMillis();		//����ʼʱ��ϵͳʱ��
		System.out.println("start");
		
		double result=0,a=0,b=0,x=0;
		String regex="\\s+";		//������ʽ��ƥ��һ�����߶���հ��ַ�
		
		/*���ļ�����������������ȡ����*/
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
					double temp=0;		//tempΪ�м���
					//һ��һ�ж�ȡ��ȥ�����˿ո�
					String xline=bufferedReaderX.readLine().trim();
					if(xline!=null) {
						//����ȡ���ַ������տհ��ַ��ָ�Ϊ�ַ�������
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
					
					//result�ۼ��м���temp
					result+=temp;
				}
			}
		}
		
		System.out.println("Result:  "+Math.pow(result, 0.5));
		//Result:  0.4464126889370735
		//�ر��ļ���
		bufferedReaderA.close();bufferedReaderX.close();bufferedReaderB.close();
		long endTime=System.currentTimeMillis();	//�������ʱ��ϵͳʱ��
		String time=(endTime-startTime)/1000+"s";	//����������1000ת��Ϊ��
		System.out.println("Time:  "+time);
		System.out.println("end");
	}
}
