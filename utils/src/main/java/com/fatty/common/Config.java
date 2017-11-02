package com.fatty.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 配置文件管理模块
 *
 */
public class Config
{

    private static Properties properties = null;

    private static ReadWriteLock lock=new ReentrantReadWriteLock(true);

    private static String Path;

    /**
     * 检查值是否合法
     *
     * @param key
     * @return
     */
    public static boolean checkValue(String key)
    {
        String ips = Config.getString("AdminIP");
        if (ips != null && key!=null && !key.isEmpty())
        {
            for (String s : ips.split("\\|"))
            {
                if (s.equals(key)) {
					return true;
				}
            }
        }
        return true;
    }

    /**
     * 读Int值
     * @param key
     * @return
     */
    public static int getInteger(String key)
    {
    	return Integer.parseInt(getString(key));
    }
    
    public static int getInteger(String key,int defaultValue)
    {
    	String value = getString(key);
    	return value == null ? defaultValue : Integer.parseInt(value);
    }

    /**
     * 读String值
     *
     * @param key
     * @return
     */
    public static String getString(String key)
    {
        if (properties == null)
        {
            return null;
        }
        String value = properties.getProperty(key);
        if(value != null)
        {
	        try
	        {
	            return new String(value.getBytes("ISO8859-1"), "utf-8");
	        }
	        catch (UnsupportedEncodingException e)
	        {
	            e.printStackTrace();
	            return null;
	        }
        }
        return null;
    }
    
    public static String getString(String key,String defaultValue)
    {
    	String value = getString(key);
    	return value == null ? defaultValue : value;
    }

    public static boolean initConfig(String path)
    {
        if (path == null || path.equals(""))
        {
            return false;
        }

        Path=path;

        if (properties == null)
        {
            return loadProperties(path);
        }
        return true;
    }

    private static boolean loadProperties(String path)
    {
        try
        {
            lock.writeLock().lock();
            properties = new Properties();
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream(path));
            properties.load(inputStream);
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    public static boolean refreshProperties()
    {
        if(Path==null ||Path.isEmpty()) {
			return false;
		}

        try
        {
            Properties temp = new Properties();
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream(Path));
            temp.load(inputStream);

            lock.writeLock().lock();
            properties=temp;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        return true;
    }

    /**
     * 设置属性值
     * @param key 属性字段
     * @param value 属性值
     * @return
     */
    public static Object setValue(String key, String value)
    {
    	return properties.put(key, value);
    }
}
