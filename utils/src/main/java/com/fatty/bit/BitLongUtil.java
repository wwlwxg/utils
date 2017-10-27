package com.fatty.bit;

/**
 * long的bit操作
 */
public class BitLongUtil {
	public static long setBitValue(long flag, int position, int value){
		long tmp =  (1 <<(position -1));
		if(value == 1){
			flag |= tmp;
		}else if(value == 0){
			tmp = tmp ^ Long.MAX_VALUE;
			flag &= tmp;
		}

		return flag;
	}


	public static long getBitValue(long flag, int position){
		return ((1 << (position - 1)) & flag)  > 0 ? 1 :0;
	}

	/**
	 * 计算bit中有多少1
	 * @param flag
	 * @return
	 */
	public static int bitCount(long flag) {
		return Long.bitCount(flag);
	}
}
