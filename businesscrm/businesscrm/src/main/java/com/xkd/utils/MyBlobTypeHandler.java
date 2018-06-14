package com.xkd.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class MyBlobTypeHandler extends BaseTypeHandler<String>  
	{  
	  
	    private static final Log LOGGER = LogFactory  
	            .getLog(MyBlobTypeHandler.class);  
	    // 指定字符集  
	    private static final String DEFAULT_CHARSET = "utf-8";  
	  
	    @Override  
	    public void setNonNullParameter(PreparedStatement ps, int i,  
	            String parameter, JdbcType jdbcType) throws SQLException  
	    {  
	        ByteArrayInputStream bis;  
	        try  
	        {  
	            // 把String转化成byte流  
	            bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));  
	        } catch (UnsupportedEncodingException e)  
	        {  
	            throw new RuntimeException("Blob Encoding Error!");  
	        }  
	        ps.setBinaryStream(i, bis, parameter.length());  
	    }  
	  
	    @Override  
	    public String getNullableResult(ResultSet rs, String columnName)  
	            throws SQLException  
	    {  
	        Blob blob = rs.getBlob(columnName);  
	        byte[] returnValue = null;  
	        String result = null;  
	        if (null != blob)  
	        {  
	            returnValue = blob.getBytes(1, (int) blob.length());  
	        }  
	        try  
	        {  
	            if (null != returnValue)  
	            {  
	                // 把byte转化成string  
	                result = new String(returnValue, DEFAULT_CHARSET);  
	            }  
	        } catch (UnsupportedEncodingException e)  
	        {  
	            throw new RuntimeException("Blob Encoding Error!");  
	        }  
	        return result;  
	    }  
	  
	    @Override  
	    public String getNullableResult(CallableStatement cs, int columnIndex)  
	            throws SQLException  
	    {  
	        Blob blob = cs.getBlob(columnIndex);  
	        byte[] returnValue = null;  
	        String result = null;  
	        if (null != blob)  
	        {  
	            returnValue = blob.getBytes(1, (int) blob.length());  
	        }  
	        try  
	        {  
	            if (null != returnValue)  
	            {  
	                result = new String(returnValue, DEFAULT_CHARSET);  
	            }  
	        } catch (UnsupportedEncodingException e)  
	        {  
	            throw new RuntimeException("Blob Encoding Error!");  
	        }  
	        return result;  
	    }  
	  
	    /** 
	     * @Description: 
	     *  
	     * @param arg0 
	     * @param arg1 
	     * @return 
	     * @throws SQLException 
	     *  
	     * @see BaseTypeHandler#getNullableResult(ResultSet,
	     *      int) 
	     *  
	     */  
	  
	    @Override  
	    public String getNullableResult(ResultSet rs, int columnName)  
	            throws SQLException  
	    {  
	        String result = null;  
	        Blob blob = rs.getBlob(columnName);  
	        byte[] returnValue = null;  
	        if (null != blob)  
	        {  
	            returnValue = blob.getBytes(1, (int) blob.length());  
	        }  
	        try  
	        {  
	            // 把byte转化成string  
	            if (null != returnValue)  
	            {  
	                result = new String(returnValue, DEFAULT_CHARSET);  
	            }  
	        } catch (UnsupportedEncodingException e)  
	        {  
	            throw new RuntimeException("Blob Encoding Error!");  
	        }  
	        return result;  
	  
	    }

	}  

