package com.kuangchi.sdd.util.commonUtil;

import java.text.NumberFormat;
import java.util.Random;

public class NumberUtil
{
    public static final NumberFormat NUMBER_FORMATTER = NumberFormat
            .getNumberInstance();

    public static final NumberFormat INTEGER_FORMATTER = NumberFormat
            .getNumberInstance();

    private static Random randGen = new Random(System.currentTimeMillis());

    public static String getNumberString(Object number)
    {
        try
        {
            return NUMBER_FORMATTER.format(number);
        }
        catch (Exception exception)
        {
            return null;
        }
    }

    public static String getIntegerString(Object number)
    {
        try
        {
            if (number instanceof Number)
                return INTEGER_FORMATTER.format(number);

            long l = Long.parseLong(number.toString());
            return INTEGER_FORMATTER.format(l);
        }
        catch (Exception exception)
        {
            return null;
        }
    }

    public static int randomInt(int length)
    {
        if ((length < 1) && (length > 9))
            throw new ArithmeticException(
                    "the length of random int must be between 0 and 9");

        int sum = 0;
        int n = 1;
        int r = 0;
        for (int i = 1; i < length; ++i)
        {
            r = randGen.nextInt(10);
            sum = sum + r * n;
            n = n * 10;
        }

        r = 1 + randGen.nextInt(9);
        sum = sum + r * n;
        return sum;
    }

    /**
     * 
     * <p>Discription:[随机生成十六进制字符,设为三组，避免在高并发情况下随机数过于集中]</p>
     * @return
     * @author:[姚宇]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String randomHex1()
    {
        return Integer.toHexString(randomInt(1) * 5 % 16);
    }

    public static String randomHex2()
    {
        return Integer.toHexString(randomInt(1) * 10 % 16);
    }

    public static String randomHex3()
    {
        return Integer.toHexString(randomInt(1) * 15 % 16);
    }
    
}