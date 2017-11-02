package com.fatty.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil
{
    public static final Date NOW = getSysteCurTime();

    /**
     * 特定时间增加值
     *
     * @param type
     * @param value
     * @return
     */
    public static java.util.Date addSpecialCurTime(Date date, int type,
            int value)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (type)
        {
            case Calendar.DATE:// 增加天数
                cal.add(Calendar.DATE, value);
                break;
            case Calendar.HOUR:// 增加小时
                cal.add(Calendar.HOUR, value);
                break;
            case Calendar.MINUTE:// 增加分钟
                cal.add(Calendar.MINUTE, value);
                break;
            case Calendar.SECOND:// 增加秒
                cal.add(Calendar.SECOND, value);
                break;
            case Calendar.MILLISECOND:// 增加毫秒
                cal.add(Calendar.MILLISECOND, value);
                break;
            default:
                System.err.println("当前类型不存在！");
                break;
        }
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 当前系统时间增加值
     *
     * @param type
     * @param value
     * @return
     */
    public static java.util.Date addSystemCurTime(int type, int value)
    {
        Calendar cal = getCalendar();
        switch (type)
        {
            case Calendar.DATE:// 增加天数
                cal.add(Calendar.DATE, value);
                break;
            case Calendar.HOUR:// 增加小时
                cal.add(Calendar.HOUR, value);
                break;
            case Calendar.MINUTE:// 增加分钟
                cal.add(Calendar.MINUTE, value);
                break;
            case Calendar.SECOND:// 增加秒
                cal.add(Calendar.SECOND, value);
                break;
            case Calendar.MILLISECOND:// 增加毫秒
                cal.add(Calendar.MILLISECOND, value);
                break;
            default:
                System.err.println("当前类型不存在！");
                break;
        }
        return new java.util.Date(cal.getTimeInMillis());
    }

    public static int calcDistanceMillis(Date startTime, Date endTime)
    {
        long startSecond = getDateToSeconds(startTime);
        long endSecond = getDateToSeconds(endTime);
        return (int) (endSecond - startSecond) * 1000;
    }

    /**
     * 比较两个时间（不包括日期）的相隔
     *
     * @param startTime
     * @param endTime
     * @param type
     *            (hour,min,sec)
     * @return
     */
    public static int compareTimeSpan(Date startTime, Date endTime, String type)
    {

        Calendar start = new GregorianCalendar();
        start.setTime(startTime);

        Calendar end = new GregorianCalendar();
        end.setTime(endTime);

        if (type.equalsIgnoreCase("hour")) {
			return end.get(Calendar.HOUR_OF_DAY)
                    - start.get(Calendar.HOUR_OF_DAY);
		}

        if (type.equalsIgnoreCase("min"))
        {
            return (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY))
                    * 60
                    + (end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE));
        }

        if (type.equalsIgnoreCase("sec"))
        {
            return (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY))
                    * 60
                    * 60
                    + (end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE))
                    * 60
                    + (end.get(Calendar.SECOND) - start.get(Calendar.SECOND));
        }

        return (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY))
                * 60
                * 60
                * 1000
                + (end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE))
                * 60
                * 1000
                + (end.get(Calendar.SECOND) - start.get(Calendar.SECOND))
                * 1000;
    }

    /**
     * 比较日期是否同一天
     *
     * @param date
     * @return
     */
    public static boolean dateCompare(Date date)
    {
        java.util.Calendar now = getCalendar();
        java.util.Calendar other = getCalendar(date);
        return dateCompare(now, other) == 0 ? true : false;
    }

    /**
     * 返回两个日期相差天数
     *
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int dateCompare(java.util.Calendar startDate,
            java.util.Calendar endDate)
    {
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endDate.set(Calendar.HOUR_OF_DAY, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        int day = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / 3600 / 24);
        return day;
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static java.util.Calendar getCalendar()
    {
        java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
        nowCalendar.setTime(new java.util.Date());
        return nowCalendar;
    }

    public static double getLoginPersonsNumber(){
		//	F(t)=
		//	=1800-88*t/10 		(0=<t<120)
		//	=800-8*(t-120)/10	(120=<t<360)
		//	=600				(360<=t<480)
		//	=600+17*(t-480)/10	(480<=t<780)
		//	=1100+40*(t-780)/10	(780<=t<1280)
		//	=3000				(1280<=t<1320)
		//	=3000-100*(t-1320)/10	(1320<=t<1440)
		//
		//在线人数显示=int（f（t）+100*rand（0，1））*系数 + 实际在线人数
		Calendar calendar = TimeUtil.getCalendar();
		int minutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
		double ft = 0 ;
		if(minutes >= 0 && minutes < 120){
			ft = 1800-88*minutes/10;
		}else if(minutes >= 120 && minutes < 360){
			ft = 800-8*(minutes-120)/10;
		}else if(minutes >= 360 && minutes < 480){
			ft = 600;
		}else if(minutes >= 480 && minutes < 780){
			ft = 600+17*(minutes-480)/10;
		}else if(minutes >= 780 && minutes < 1280){
			ft = 1100+40*(minutes-780)/10;
		}else if(minutes >= 1280 && minutes < 1320){
			ft = 3000;
		}else if(minutes >= 1320 && minutes < 1440){
			ft = 3000-100*(minutes-1320)/10;
		}
		return ft;
    }
    
    /**
     * 获取系统时间
     *
     * @return
     */
    public static Calendar getCalendar(Date date)
    {
        java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
        nowCalendar.setTime(date);
        return nowCalendar;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateFormat(Date date, String format)
    {
        SimpleDateFormat dateFm = new SimpleDateFormat(format);
        return dateFm.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String getDateFormat(java.util.Date date)
    {
        return getDateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取指定日期距1970年1月1日总秒
     *
     * @param date
     * @return
     */
    public static long getDateToSeconds(Date date)
    {
        return getCalendar(date).getTimeInMillis() / 1000;
    }

    /**
     * 获取一周的第几天
     */
    public static int getDayOfWeek()
    {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getDaySecond(){
    	return 24 * 60 * 60;
    }

    /**
     * 获取默认日期2000-01-01
     *
     * @return 返回默认起始时间
     */
    public static java.sql.Timestamp getDefaultDate()
    {
        java.util.Date defaultDate = null;
        try
        {
            defaultDate = (java.util.Date) new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss").parseObject("2000-01-01 00:00:00");

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(defaultDate.getTime());
    }

    /**
     * 获取指定日期格式
     *
     * @return 返回日期格式变量
     */
    public static java.sql.Timestamp getDefaultDate(String time)
    {
        java.util.Date tempDate = null;
        try
        {
            tempDate = (java.util.Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(time);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(tempDate.getTime());
    }

    /**
     * 获取默认目上限日期2999-01-01
     *
     * @return 返回默认上限时间
     */
    public static java.sql.Timestamp getDefaultMaxDate()
    {
        java.util.Date defaultDate = null;
        try
        {
            defaultDate = (java.util.Date) new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss").parseObject("2999-01-01 00:00:00");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(defaultDate.getTime());
    }

    /**
     * 提供给 flash xml 的日期格式
     *
     * @param date
     * @return
     */
    public static String getFlashDate(Date date)
    {
        return getDateFormat(date, "yyyy-MM-dd") + "T"
                + getDateFormat(date, "HH:mm:ss");
    }

    /**
     * 用于保存玩家图片的格式 userId_yyyyMMddHHmmss
     *
     * @param date
     * @return
     */
    public static String getImageName()
    {
        return getDateFormat(getSysteCurTime(), "yyyyMMddHHmmss");
    }

    /**
     * 指定的毫秒long值转成Timestamp类型
     *
     * @param value
     * @return
     */
    public static java.sql.Timestamp getMillisToDate(long value)
    {
        return new java.sql.Timestamp(value);
    }


    /**
     * 返回日期类型的字符串
     *
     * @return
     */
    public static String getSimpleDate()
    {
        return getSimpleDate(new Date());
    }

    public static String getSimpleDate(Date date)
    {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 获取系统距1970年1月1日总秒
     *
     * @return
     */
    public static long getSysCurSeconds()
    {
        return getCalendar().getTimeInMillis() / 1000;
    }

    /**
     * 获取系统距1970年1月1日总毫秒
     *
     * @return
     */
    public static long getSysCurTimeMillis()
    {
        return getCalendar().getTimeInMillis();
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static Timestamp getSysteCurTime()
    {
        Timestamp ts = new Timestamp(getCalendar().getTimeInMillis());
        return ts;
    }

    /**
     * 间隔时间以小时为单位
     *
     * @param startDate
     * @param interval
     * @return
     */
    @SuppressWarnings("static-access")
    public static boolean isInterval(Date startDate, int interval)
    {
        // 开始日期
        java.util.Calendar startCalendar = java.util.Calendar.getInstance();
        startCalendar.setTime(startDate);
        // 结束日期
        java.util.Calendar endCalendar = getCalendar();
        if (dateCompare(startCalendar, endCalendar) == 0)
        {
            if (endCalendar.get(endCalendar.HOUR_OF_DAY) / interval == startCalendar.get(startCalendar.HOUR_OF_DAY)
                    / interval)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回两个日期相隔的（小时，分钟，秒）
     *
     * @param startTime
     * @param endTime
     * @param type
     *            [day,hour,min,sec]
     * @return
     */
    public static int timeSpan(Date startTime, Date endTime, String type)
    {

        long span = endTime.getTime() - startTime.getTime();

        if (type.equalsIgnoreCase("day")) {
			return (int) (span / (24 * 60 * 60 * 1000));
		} else if (type.equalsIgnoreCase("hour")) {
			return (int) (span / (60 * 60 * 1000));
		} else if (type.equalsIgnoreCase("min")) {
			return (int) (span / (60 * 1000));
		} else if (type.equalsIgnoreCase("sec")) {
			return (int) (span / 1000);
		} else {
			return (int) span;
		}
    }

    /**
     * 返回两个日期相隔的（小时，分钟，秒）
     *
     * @param startTime
     * @param endTime
     * @param type
     *            类型：hour、day、min、sec
     * @return
     */
    public static int timeSpan(Timestamp startTime, Timestamp endTime,
            String type)
    {

        long span = endTime.getTime() - startTime.getTime();

        if (type.equalsIgnoreCase("day")) {
			return (int) (span / (24 * 60 * 60 * 1000));
		} else if (type.equalsIgnoreCase("hour")) {
			return (int) (span / (60 * 60 * 1000));
		} else if (type.equalsIgnoreCase("min")) {
			return (int) (span / (60 * 1000));
		} else if (type.equalsIgnoreCase("sec")) {
			return (int) (span / 1000);
		} else {
			return (int) span;
		}
    }

    public static int timeToFrame(int secondTime)
    {
        return (secondTime * 25) / 1000;
    }
}
