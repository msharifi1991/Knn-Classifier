import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class KnnClassifer {
	 static int j=0 , row_num=0 , t=0 , u=0 ,o=0;
	 int train_row_num;
	 static String str, str2;
	 float np=0, fp=0, tp=0 ,relevant=0, retrieved=1 ;
	 static float accuracy=0;
	 float precision=0, recall=0, f_measure=0;
	 float[][] fold1 = new float[500][7] ;
	 float[][] fold2 = new float[500][7];
	 float[][] fold3 = new float[500][7];
	 float[][] train = new float[1500][7];
	 float[][] train1= new float[1000][7];
	 float[][] train2= new float[1000][7];
	 float[][] train3= new float[1000][7];
	 static int test_row_num=0, test_rows_numbers=0;
	 float rrr[];
	 float distance[];
	 float test[][] = new float[140][7];
	 float[] revd ;
	 float sub[]; 
	 float math[];
	 float[] list;
	 float[] list2;
	 float[] test_array = new float[6];
	 float res = 0;
	 static String s = "E:/Eclips/K-NN_Classifier/output/test-labeled-";
	 String txt0="nn-r.arff";
	 String txt1="nn-s.arff";
	 String txt2="nn-d.arff";
	 reverseDist_value[] rev; 
	 reverseDist_value[] rev_sort_test;
		
	 
	KnnClassifer(){
		   Scanner console = new Scanner(System.in);
		   System.out.print("enter the address of training data file please:");
		   String train_doc = console.next();
		  System.out.print("enter the address of test data file please:");
		  String test_doc = console.next();
	   try{
	         InputStream is = new FileInputStream(train_doc);
	         InputStreamReader instrm = new InputStreamReader(is);
	         BufferedReader br = new BufferedReader(instrm);
	         String strLine;
	         String strLine2;
	         InputStream is2= new FileInputStream(test_doc);
	         InputStreamReader instrm2 = new InputStreamReader(is2);
	         BufferedReader br2 = new BufferedReader(instrm2);
	         
	       for(int i=0; i<1500; i++)
	         {strLine = br.readLine();
	         fold(strLine);}
	        
	       for(int k=1;k<=15;k++){
	       	 String k2 = Float.toString(k);
	       	 String file_name = s.concat(k2).concat(txt0);
	       	 BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file_name), true));
	     
	    	 for(test_row_num=0;test_row_num<140;test_row_num++) {//TODO
	        	strLine2 = br2.readLine();
	        	if(strLine2!=null){testProcess(k,strLine2,train);
	        	float lable2 = find_max(list2);
	        	
	        	String test_class = null; 
	        	if(lable2==1)test_class=",acc";
	        	if(lable2==2)test_class=",unacc";
	        	if(lable2==3)test_class=",good";
	        	if(lable2==4)test_class=",vgood";
	   
	        	String file_line = strLine2.concat(test_class);
	             bw.write(file_line);  
	             bw.newLine();
	        	}
	            }
	        	 bw.close();
	            } 

	         for(int k=1;k<=15;k++){
	    	 
	        	 precision = Process(k,fold1, merge(fold2, fold3, train1),1) + Process(k,fold2, merge(fold1, fold3, train2),1)+Process(k,fold3, merge(fold1, fold2, train3),1);
	        	 recall = Process(k,fold1, merge(fold2, fold3, train1),2) + Process(k,fold2, merge(fold1, fold3, train2),2)+Process(k,fold3, merge(fold1, fold2, train3),2);
	        	 accuracy = Process(k,fold1, merge(fold2, fold3, train1),3) + Process(k,fold2, merge(fold1, fold3, train2),3)+Process(k,fold3, merge(fold1, fold2, train3),3);
	        	 System.out.println("k:" + k + "   ,precision: " + (precision/3)*100 + "%" + "  ,recall: " + (recall/3)*100+"%" +"  ,accuracy:"+(accuracy)/3*100+"%"  + "  ,f_measure: " + (((recall*precision)/((recall+precision)/2))/3)*100 + "%");
	        
	         	}
	      	 	}
	        
	      catch(Exception e){
	    	  e.printStackTrace();
	      		}
				}	
   
	private void testProcess(int k, String strLine2, float[][] train4) { //TODO
			
			int q=0;
			StringTokenizer st2 = new StringTokenizer(strLine2, ",");
			String line2[] = new String[7];
			
  		 while(st2.hasMoreTokens()){ 
  			 str = st2.nextToken();
  		 
  		 if(q<6){ 
  		     line2[q] = str;
  		     if (! (line2[q].equals(null))){
  		     test[test_row_num][q] = Float.parseFloat(line2[q]);
  		 	 test_array[q] = Float.parseFloat(line2[q]);
			  }
  		      }
  		 
  		 if(q==6)
  			 test[test_row_num][q] = 0;
  		 
  		    q++;
  		     }
  		 	rev_sort_test = sort(reverseDist(train4, test_array , 0), reverseDist(train4, test_array , 0).length);
  		 	list2 = new float[15];	  
  		 	rrr = new float[k];
  		 	for(int c=0;c<k;c++){
  		 		rrr[c]  = rev_sort_test[c].rev_dist;
  		 		list2[c] = rev_sort_test[c].lable;
  		 	}
  			}
     
	public static void main(String[] args) throws Exception {
    	 KnnClassifer kn = new KnnClassifer();
   	     System.out.println("Files created successfully!");
    		}			 
	 
    private static float[][] merge(float[][] fold22, float[][] fold32, float[][] merge_train) {
    	 for(int m=0; m<500; m++){
    		 for(int k=0;k<7;k++){
    		 merge_train[m][k] = fold22[m][k];
    		 merge_train[500+m][k]=fold32[m][k];}
    	 	}
		return merge_train;
			}
    
	private void fold(String strLine) {
    	 j=0;
    	 StringTokenizer st = new StringTokenizer(strLine, ","); 
 		 String line[] = new String[7];
 	  while(st.hasMoreTokens()){ 
 		 str = st.nextToken();
 		 line[j] = str;
 	  if(j<6) {
 		 train[train_row_num][j]=Float.parseFloat(line[j]);
 	    
 		 if(t==0)
 		  fold1[row_num][j] = Float.parseFloat(line[j]);
 		 else if(t==1)
 		  fold2[row_num][j] = Float.parseFloat(line[j]);
 		 else if(t==2)
 		  fold3[row_num][j] = Float.parseFloat(line[j]);
 		 	}
 		 
 	  if(j==6){
 		  if(line[6].equals("acc"))  
 		   train[train_row_num][6]=1;
 		  else if(line[6].equals("unacc"))
 		   train[train_row_num][6]=2;
 		  else if(line[6].equals("good"))
 		   train[train_row_num][6]=3;
 		  else if(line[6].equals("vgood"))
 		   train[train_row_num][6]=4;
 		 	
 		  if(t==0){
 	 		if(line[6].equals("acc"))  
 	 		  fold1[row_num][6]=1;
 	 		else if(line[6].equals("unacc"))
 	 		  fold1[row_num][6]=2;
 	 		else if(line[6].equals("good"))
 	 		  fold1[row_num][6]=3;
 	 		else if(line[6].equals("vgood"))
 	 		  fold1[row_num][6]=4;}
 		   
 		  else if(t==1){
 	  		  if(line[6].equals("acc"))             
 	  		   fold2[row_num][6]=1;
 	  		 else if(line[6].equals("unacc"))
 	  		   fold2[row_num][6]=2;
 	  		 else if(line[6].equals("good"))
 	  		   fold2[row_num][6]=3;
 	  		 else if(line[6].equals("vgood"))
 	  		   fold2[row_num][6]=4;}
 		  
 		 else if(t==2){
 	  	 	  if(line[6].equals("acc"))  
 	  		   fold3[row_num][6]=1;
 	  		  else if(line[6].equals("unacc"))
 	  		   fold3[row_num][6]=2;
 	  		  else if(line[6].equals("good"))
 	  		   fold3[row_num][6]=3;
 	  		  else if(line[6].equals("vgood"))
 	  		   fold3[row_num][6]=4;}	  }
 		 	 
 	  			j++;}
 		 	  
 	  		train_row_num++;
 	  		
 	  		if(row_num < 499)
 	  			row_num++;
 	  		else if(row_num == 499)
 	  		{row_num = 0;
 			t++;}
				}
	
	private float Process(int k, float test[][], float[][] train, int e) {//TODO
		
			float r=0;
			float[] test_rows = new float[7];
		for(int i=0;i<test.length;i++){
			for(int j=0;j<7;j++){
			test_rows[j] = test[i][j];
			
			}
		
			r = test_rows[6];
			rev = sort(reverseDist(train, test_rows , r), reverseDist(train, test_rows , r).length);  
			list = new float[k];	  
		for(int c=0;c<k;c++){
			list[c] = rev[c].lable;
		    }
		    
		 if(find_max(list)==r)
		   {relevant++;
		   	if(find_max(list)!=0)
		   	tp++;}
		  else if(find_max(list) != r && find_max(list)!=0)fp++; 
				 retrieved = fp+tp;
				 
		}
		if(e==1)
			return evaluatePrec(tp, retrieved);
		else if(e==2)
			return evaluateRecall(tp, relevant);
		else if(e==3)
			return evaluateAcc(tp, fp, retrieved);
		else return 0;
			}
	
	private void knn(reverseDist_value list[], int k) {
		float[] k_array = new float[k];
		for(int i=0;i<k;i++)
			k_array[i] = list[i].rev_dist;
		System.out.println(find_max(k_array)); 
			}
	
	private float evaluatePrec(float tp, float total) {
	precision = (tp/total);
		return precision; 
			}
	
	private float evaluateRecall(float tp, float total) {
		recall = (tp/total);
		return recall; 
			}
	
	private static float evaluateAcc(float tp2, float fp2, float total) { 
		accuracy = (tp2/total)+(fp2/total);
		return accuracy;
			}
	
	private float find_max(float[] list) {
			float max = 0, max1=0,max2=0;
			float acc_num = 0 , unacc_num=0, good_num=0, vgood_num=0;
		for(int i=1;i<list.length;i++)
		   {if(list[i]==1)
			acc_num++;
		   else if(list[i] == 2)
			unacc_num++;
		   else if(list[i] == 3)
			good_num++;
		   else if(list[i] == 4)
			vgood_num++;
			}
		
		if(acc_num>unacc_num)max=acc_num; else max=unacc_num;
		if(good_num>vgood_num)max1=good_num; else max1=vgood_num;
		if (max1>max)max=max1;
		if(max==acc_num)max2=1;
		if(max==unacc_num)max2=2;
		if(max==good_num)max2=3;
		if(max==vgood_num)max2=4;
		return max2;
	}
	
	private reverseDist_value[] sort(reverseDist_value[] rev, int n) {
		  int i, j=0;
		  float y=0; 
		  for(i = 0; i < n; i++){
		  for(j = 1; j < (n-i); j++){ 
		  if(rev[j-1].rev_dist < rev[j].rev_dist){
		  y = rev[j-1].rev_dist;
		  rev[j-1].rev_dist=rev[j].rev_dist;
		  rev[j].rev_dist=y;
		  }
		  }
		  } 
		return rev; 
	} 
	private int class_num(int z, float[][] train) {
		int count = 0;
		for(int l=0;l<1000;l++){
			if(train[l][6] == z)
				count++;}
		return count;
	}
		private void nDist(float[][] train, float[] test_rows, float r) {
			float distance3[] = new float[train.length];
			float[] ndist = new float [train.length];
			float sub[] = new float[6]; 
			float math[] = new float[6];
			float res = 0;
			nDist[] rev = new nDist[train.length];
			for(int i=0;i<train.length;i++){
				rev[i] = new nDist();
				for(int j=0;j<6;j++){  
					sub[j] =test_rows[j] - train[i][j];
				math[j]=(float) Math.pow((sub[j]), 2);
				res = math[0]+math[1]+math[2]+math[3]+math[4]+math[5]; 
				}
				distance3[i] = (float) (Math.sqrt(res));
				ndist[i]=1-distance3[i];
			}
	} 
		private sqReverseDist[] sqReverseDist(float[][] train, float[] test_rows, float r) {
			float distance2[] = new float[train.length];
			float[] revd = new float [train.length];
			float[] sqrevd = new float [train.length];
			float sub[] = new float[6]; 
			float math[] = new float[6];
			float res = 0;
			sqReverseDist[] sqrev = new sqReverseDist[train.length];
			for(int i=0;i<train.length;i++){
				sqrev[i] = new sqReverseDist();
				for(int j=0;j<6;j++){  
					sub[j] =test_rows[j] - train[i][j];
				math[j]=(float) Math.pow((sub[j]), 2);
				res = math[0]+math[1]+math[2]+math[3]+math[4]+math[5]; 
				} 
				distance2[i] = (float) (Math.sqrt(res)); 
				revd[i] = 1/distance2[i];
				sqrevd[i] = (float) Math.pow(revd[i], 2);
				sqrev[i].lable = r;
				sqrev[i].dist =  distance2[i];
				//sqrev[i].sq_rev_dist = (float) Math.pow(sqrevd[i],2);
			}	
			return sqrev;
		}
	private reverseDist_value[] reverseDist(float[][] train, float[] test_rows, float r) {
		float distance[] = new float[train.length]; 
		float[] revd = new float [train.length];
		float sub[] = new float[6]; 
		float math[] = new float[6];
		float res = 0;
		reverseDist_value[] rev = new reverseDist_value[train.length];
		for(int i=0;i<train.length;i++){
			rev[i] = new reverseDist_value();
			for(int j=0;j<6;j++){ 
				sub[j] = test_rows[j] - train[i][j];
			math[j]=(float) Math.pow((sub[j]), 2);
			res = math[0]+math[1]+math[2]+math[3]+math[4]+math[5];
			}
			distance[i] = (float) (Math.sqrt(res)); 
			revd[i] = 1/distance[i];
			rev[i].lable = train[i][6];
			rev[i].dist =  distance[i];
			rev[i].rev_dist =revd[i];
		}	
		return rev;
	}
	
	}
