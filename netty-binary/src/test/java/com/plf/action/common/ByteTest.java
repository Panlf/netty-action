package com.plf.action.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.WrappedByteBuf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author panlf
 * @date 2023/5/17
 */
public class ByteTest {

    @Test
    public void test001(){
        byte[] bytes = new byte[]{(byte) 0xef, (byte) 0xef};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println(Arrays.equals(byteBuffer.array(), bytes));
    }

    @Test
    public void test002(){
        byte[] bytes = new byte[]{(byte) 0x00, (byte) 0x04};
        System.out.println(ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort());
        //System.out.println(bytesToLong(bytes,0,false));
    }

    //short 2 个字节
    //int 4个字节
    //long 8个字节
    public static short bytesToLong(byte[] input, int offset, boolean littleEndian) {
        // 将byte[] 封装为 ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(input,offset,8);
        if(littleEndian){
            // ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
            // ByteBuffer 默认为大端(BIG_ENDIAN)模式
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buffer.getShort();
    }

    @Test
    public void test003(){


    }
}
