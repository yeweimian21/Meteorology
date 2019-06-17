package com.code.work;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Parallel {
	
	public static long startTime;
	
	//map��
	public static class WeatherMapper extends Mapper<LongWritable, Text, Text, Text>{
		private String file=null;		//���ݼ��ļ���
		public String regex="\\s+";
		
		@Override
		//Map���setup():��ִ��Map����ǰ��������ر���������Դ�ļ��г�ʼ���������˷�����MapReduce��ܽ���ִ��һ�Ρ�
		protected void setup(Context context)
				throws IOException, InterruptedException {
			// ��ȡ�ļ�����
			file = ((FileSplit) context.getInputSplit()).getPath().getName();
		}
		@Override
		//map����
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//���д����ļ�
			String line = value.toString().trim();
			String[] elements = line.split(regex);
			//��ȡ��i,j,k
			String i=elements[0];
			String j=elements[1];
			String k=elements[2];
			
			/*map�����keyΪi,j,k����(i,j,k)���й�Լ��keyΪͬһ����(i,j,k)�Ļᱻ�ֵ�reduce��*/
			Text keyMapOut = new Text(i + "," + j + "," +k);
			
			Text valueMapOut = null;
			
			if (file.equals("A.txt")) 			//A.txtʱ��map�����value��"a"��־,a��ֵ
			{			
				String a=elements[elements.length-1];
				valueMapOut = new Text("a,"+a);
            }
			else if(file.equals("x.txt")) 		//x.txtʱ��map�����value��x��־��x��ֵ
			{		
				String x=elements[elements.length-1];
				valueMapOut = new Text("x,"+x);
			}
			else 								//b.txtʱ��map�����value��b��־��b��ֵ
			{								
				String b=elements[elements.length-1];
				valueMapOut = new Text("b,"+b);
			}
			
			//map�����keyΪ(i,j,k)��valueΪ(��־,ֵ)
			context.write(keyMapOut, valueMapOut);
		}
	}
	//reduce��
	public static class WeatherReducer extends Reducer<Text, Text, Text, DoubleWritable>{
		
		public static double sum=0;		
		
		@Override
		//reduce����
		protected void reduce(Text key, Iterable<Text> valueSet, Context context)
				throws IOException, InterruptedException {
			ArrayList<Double> aList=new ArrayList<Double>();		//a��ֵ�ļ���
			double x=0;
			double b=0;
			double temp=0;
			
			//����һ��key��Ӧ��value����
			for (Text value : valueSet) {
                String[] line = value.toString().split(",");	//��","���зָ�
                String flag=line[0];		//flagΪa����x����b
                String val=line[1];			//valueΪ��Ӧ��ֵ
                if (flag.equals("a")) {
                    aList.add(Double.parseDouble(val));
                } 
                else if (flag.equals("x")) {
                    x=Double.parseDouble(val);
                }
                else {
                	b=Double.parseDouble(val);
                }
            }
			
			for(Double a : aList) {
				temp+=a*x;
			}
			temp=temp-b;
			temp=Math.pow(temp, 2);
			
			sum+=temp;
			
			//reduce�����keyΪ(i,j,k)��valueΪ�м���
			context.write(key, new DoubleWritable(temp));
		}
		
		@Override
		//Reduce���cleanup():��ִ�����Reduce����󣬽�����ر�������Դ���ͷŹ������˷�����MapReduce��ܽ���ִ��һ�Ρ�
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
			double result=Math.pow(sum,0.5);
			//result:	0.4464126889370711
			//�����Ľ�����
			context.write(new Text("Result="), new DoubleWritable(result));
			
			long endTime=System.currentTimeMillis();
			String time=(endTime-startTime)/1000.0/60.0+"minutes";
			System.out.println("Time:  "+time);
			System.out.println("end");
		}
	}
	
	public static void main(String[] args) throws IOException, 
			ClassNotFoundException, InterruptedException {
		startTime=System.currentTimeMillis();
		System.out.println("start");
		
		System.setProperty("hadoop.home.dir", "D:/hadoop-2.6.5");
		//��������ļ�·��
		String input1 = "hdfs://172.19.30.177:9000/input/parallel/A.txt";
        String input2 = "hdfs://172.19.30.177:9000/input/parallel/x.txt";
        String input3 = "hdfs://172.19.30.177:9000/input/parallel/b.txt";
        String output = "hdfs://172.19.30.177:9000/outputparallel/temp.txt";
        
        Configuration conf = new Configuration();
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");
        conf.addResource("classpath:/hadoop/yarn-site.xml");
        
        Job job = Job.getInstance(conf, "TempJob");
        job.setJarByClass(Parallel.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(WeatherMapper.class);
        job.setReducerClass(WeatherReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        //����3���������ݼ��ļ�
        FileInputFormat.setInputPaths(job, 
        		new Path(input1),new Path(input2),new Path(input3));
        Path outputPath = new Path(output);
        //ɾ��֮ǰ�����Ŀ¼
        outputPath.getFileSystem(conf).delete(outputPath, true);
        //�������Ŀ¼
        FileOutputFormat.setOutputPath(job, outputPath);
		
        System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
