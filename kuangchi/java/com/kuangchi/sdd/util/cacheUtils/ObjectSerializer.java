package com.kuangchi.sdd.util.cacheUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjectSerializer<T> {
	        public  byte[] serialize(T value) {  
	            if (value == null) {  
	                throw new NullPointerException("Can't serialize null");  
	            }  
	            byte[] rv=null;  
	            ByteArrayOutputStream bos = null;  
	            ObjectOutputStream os = null;  
	            try {  
	                bos = new ByteArrayOutputStream();  
	                os = new ObjectOutputStream(bos);  
	                os.writeObject(value);  
	                rv = bos.toByteArray();  
	            } catch (IOException e) {  
	                throw new IllegalArgumentException("Non-serializable object", e);  
	            } finally {  
	                 if (bos!=null) {
						try {
							bos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
	                 if (os!=null) {
						try {
							os.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
	            }  
	            return rv;  
	        }  
	        
	        
	        public  T deserialize(byte[] in) {  
	            ByteArrayInputStream bis = null;  
	            ObjectInputStream is = null;  
	            T t =null;
	            try {  
	                if(in != null) {  
	                    bis=new ByteArrayInputStream(in);  
	                    is=new ObjectInputStream(bis);  
	                    t = (T) is.readObject();      
	                    is.close();  
	                    bis.close();  
	                }  
	            } catch (IOException e) {  
	                e.printStackTrace();
	            } catch (ClassNotFoundException e) {  
	                e.printStackTrace();
	            } finally {  
	            	 if (bis!=null) {
							try {
								bis.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} 
		                 if (is!=null) {
							try {
								is.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
	            }  
	            return t;  
	        } 
	        
	        
	        
	        public static void main(String[] args) {
				List<String> list=new ArrayList<String>();
				list.add("1");
				list.add("2");
				ObjectSerializer<List<String>> os=new  ObjectSerializer<List<String>>();
				
				
				byte[] bbb=os.serialize(list);;
				
				System.out.println(bbb);
				
				List<String> lll=os.deserialize(bbb);
				System.out.println(lll);
			}
	        
	        
	        
	    }
	        
	        

