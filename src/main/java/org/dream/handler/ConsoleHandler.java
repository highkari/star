package org.dream.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.dream.model.Player;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhengyong on 16/10/21.
 */
public class ConsoleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        System.out.println(msg);
        Player.AddressBook.Builder a = Player.AddressBook.newBuilder();
        a.addPeople(Player.Person.newBuilder());
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}