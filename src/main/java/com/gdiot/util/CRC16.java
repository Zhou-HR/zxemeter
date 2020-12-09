package com.gdiot.util;

/**
 * @author lwt
 * @date 2018-06-26
 *
 * CRC16校验码计算
 * <p>
 * (1)．预置1个16位的寄存器为十六进制FFFF（即全为1），称此寄存器为CRC寄存器；
 * (2)．把第一个8位二进制数据（既通讯信息帧的第一个字节）与16位的CRC寄存器的低
 * 8位相异或，把结果放于CRC寄存器；
 * (3)．把CRC寄存器的内容右移一位（朝低位）用0填补最高位，并检查右移后的移出位；
 * (4)．如果移出位为0：重复第3步（再次右移一位）；如果移出位为1：CRC寄存器与多项式A001（1010 0000 0000 0001）进行异或；
 * (5)．重复步骤3和4，直到右移8次，这样整个8位数据全部进行了处理；
 * (6)．重复步骤2到步骤5，进行通讯信息帧下一个字节的处理；
 * (7)．将该通讯信息帧所有字节按上述步骤计算完成后，得到的16位CRC寄存器的高、低
 * 字节进行交换；
 * (8)．最后得到的CRC寄存器内容即为CRC16码。(注意得到的CRC码即为低前高后顺序)
 */
public class CRC16 {
	/**
     * 计算CRC16校验码
     *
     * @param data 需要校验的字符串
     * @return 校验码
     */
    public static String getCRC(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "00";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        String sHex = getCS(para);
        if(sHex.length() == 1) {//长度为1位时，高位补0
        	return "0"+sHex;
        }
        return sHex;
    }
	public static String getCS(byte[] Data)
	{
		int Num = Data.length;
    	int i;
    	int CRC = 0x00;
		for (i=0;i<Num;i++)	CRC += Data[i];
		CRC = ~CRC;
		CRC += 0x33;
		CRC = (CRC & 0xFF);
		String strHex = Integer.toHexString(CRC);
//		System.out.println(CRC + " [十进制]---->[十六进制] " + strHex);
		return strHex;
	}
 
  
	/****************************************************************************
	** 函数名称: UARTcmdCheck
	** 函数描述: 计算校验位
	** 输入参数: *Data 输入数组			Num 数组长度
	** 输出参数: 校验位
	*****************************************************************************/

	public int getCS(char[] Data)
	{
		int Num = Data.length;
    	int i;
    	int CRC = 0x00;
		for (i=0;i<Num;i++)	CRC += Data[i];
		CRC = ~CRC;
		CRC += 0x33;
		//CRC = (CRC & 0xFF);
		return CRC;
	}
	
	public static String getCRC8(String data) {
//    	System.out.printf("-------data="+data + "\n");
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "00";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
//            System.out.printf("-------para[i]="+para[i] + "\n");
        }
        String sHex = getCS8(para);
        System.out.printf("-------sHex="+sHex + "\n");
        if(sHex.length() == 1) {//长度为1位时，高位补0
        	return "0"+sHex;
        }
        return sHex;
    }
    
	public static String getCS8(byte[] Data){
		int Num = Data.length;
    	int i;
    	int CRC = 0x00;
		for (i=0;i<Num;i++) {
			CRC += Data[i] ;
		}
		CRC = (CRC & 0xFF);
		String strHex = Integer.toHexString(CRC);
		System.out.println("校验和："+CRC + " [十进制]---->[十六进制] " + strHex);
		return strHex;
	}
	
}
