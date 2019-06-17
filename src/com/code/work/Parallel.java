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
	
	//map类
	public static class WeatherMapper extends Mapper<LongWritable, Text, Text, Text>{
		private String file=null;		//数据集文件名
		public String regex="\\s+";
		
		@Override
		//Map类的setup():在执行Map任务前，进行相关变量或者资源的集中初始化工作。此方法被MapReduce框架仅且执行一次。
		protected void setup(Context context)
				throws IOException, InterruptedException {
			// 获取文件名称
			file = ((FileSplit) context.getInputSplit()).getPath().getName();
		}
		@Override
		//map方法
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//按行处理文件
			String line = value.toString().trim();
			String[] elements = line.split(regex);
			//提取出i,j,k
			String i=elements[0];
			String j=elements[1];
			String k=elements[2];
			
			/*map输出的key为i,j,k。以(i,j,k)进行归约，key为同一个的(i,j,k)的会被分到reduce上*/
			Text keyMapOut = new Text(i + "," + j + "," +k);
			
			Text valueMapOut = null;
			
			if (file.equals("A.txt")) 			//A.txt时，map输出的value："a"标志,a的值
			{			
				String a=elements[elements.length-1];
				valueMapOut = new Text("a,"+a);
            }
			else if(file.equals("x.txt")) 		//x.txt时，map输出的value：x标志，x的值
			{		
				String x=elements[elements.length-1];
				valueMapOut = new Text("x,"+x);
			}
			else 								//b.txt时，map输出的value：b标志，b的值
			{								
				String b=elements[elements.length-1];
				valueMapOut = new Text("b,"+b);
			}
			
			//map输出的key为(i,j,k)，value为(标志,值)
			context.write(keyMapOut, valueMapOut);
		}
	}
	//reduce类
	public static class WeatherReducer extends Reducer<Text, Text, Text, DoubleWritable>{
		
		public static double sum=0;		
		
		@Override
		//reduce方法
		protected void reduce(Text key, Iterable<Text> valueSet, Context context)
				throws IOException, InterruptedException {
			ArrayList<Double> aList=new ArrayList<Double>();		//a的值的集合
			double x=0;
			double b=0;
			double temp=0;
			
			//遍历一个key对应的value集合
			for (Text value : valueSet) {
                String[] line = value.toString().split(",");	//以","进行分割
                String flag=line[0];		//flag为a或者x或者b
                String val=line[1];			//value为对应的值
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
			
			//reduce输出的key为(i,j,k)，value为中间结果
			context.write(key, new DoubleWritable(temp));
		}
		
		@Override
		//Reduce类的cleanup():在执行完毕Reduce任务后，进行相关变量或资源的释放工作。此方法被MapReduce框架仅且执行一次。
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
			double result=Math.pow(sum,0.5);
			//result:	0.4464126889370711
			//将最后的结果输出
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
		//输出输入文件路径
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
        
        //加载3个输入数据集文件
        FileInputFormat.setInputPaths(job, 
        		new Path(input1),new Path(input2),new Path(input3));
        Path outputPath = new Path(output);
        //删除之前的输出目录
        outputPath.getFileSystem(conf).delete(outputPath, true);
        //设置输出目录
        FileOutputFormat.setOutputPath(job, outputPath);
		
        System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
