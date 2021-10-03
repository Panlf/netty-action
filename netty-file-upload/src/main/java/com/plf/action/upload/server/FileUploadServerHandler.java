package com.plf.action.upload.server;

import com.plf.action.upload.bean.UploadFile;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author panlf
 * @date 2021/10/3
 */
public class FileUploadServerHandler extends ChannelHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    private String fileDir="D:\\TempData\\copyTemp";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof UploadFile){
            UploadFile uploadFile = (UploadFile) msg;
            byte[] bytes = uploadFile.getBytes();
            byteRead = uploadFile.getEndPosition();
            String fileMd5 = uploadFile.getFileMd5();
            String path = fileDir + File.separator + fileMd5;
            File file = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start+byteRead;
            if(byteRead > 0){
                ctx.writeAndFlush(start);
            }else{
                randomAccessFile.close();
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
