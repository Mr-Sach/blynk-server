package cc.blynk.server.application.handlers.main.auth;

import cc.blynk.server.core.dao.TokenManager;
import cc.blynk.server.core.dao.UserDao;
import cc.blynk.server.core.protocol.model.messages.appllication.RegisterMessage;
import cc.blynk.server.workers.timer.TimerWorker;
import cc.blynk.utils.AppNameUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyShort;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 10.08.15.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class RegisterHandlerTest {

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private UserDao userDao;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private TimerWorker timerWorker;

    @Mock
    private ByteBufAllocator allocator;

    @Mock
    private ByteBuf byteBuf;


    @Test
    public void testRegisterOk() throws Exception {
        RegisterHandler registerHandler = new RegisterHandler(userDao, tokenManager, timerWorker, null);

        String userName = "test@gmail.com";

        when(ctx.alloc()).thenReturn(allocator);
        when(allocator.ioBuffer(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeByte(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);

        when(userDao.isUserExists(userName, AppNameUtil.BLYNK)).thenReturn(false);
        registerHandler.channelRead0(ctx, new RegisterMessage(1, userName + "\0" + "1"));

        verify(userDao).add(eq(userName), eq("1"), eq(AppNameUtil.BLYNK));
    }

    @Test
    public void testRegisterOk2() throws Exception {
        RegisterHandler registerHandler = new RegisterHandler(userDao, tokenManager, timerWorker, null);

        String userName = "test@gmail.com";

        when(ctx.alloc()).thenReturn(allocator);
        when(allocator.ioBuffer(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeByte(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);

        when(userDao.isUserExists(userName, AppNameUtil.BLYNK)).thenReturn(false);
        registerHandler.channelRead0(ctx, new RegisterMessage(1, userName + "\0" + "1"));

        verify(userDao).add(eq(userName), eq("1"), eq(AppNameUtil.BLYNK));
    }

    @Test
    public void testAllowedUsersSingleUserWork() throws Exception {
        RegisterHandler registerHandler = new RegisterHandler(userDao, tokenManager, timerWorker, new String[] {"test@gmail.com"});

        String userName = "test@gmail.com";

        when(ctx.alloc()).thenReturn(allocator);
        when(allocator.ioBuffer(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeByte(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);

        when(userDao.isUserExists(userName, AppNameUtil.BLYNK)).thenReturn(false);
        registerHandler.channelRead0(ctx, new RegisterMessage(1, userName + "\0" + "1"));

        verify(userDao).add(eq(userName), eq("1"), eq(AppNameUtil.BLYNK));
    }

    @Test
    public void testAllowedUsersSingleUserNotWork() throws Exception {
        RegisterHandler registerHandler = new RegisterHandler(userDao, tokenManager, timerWorker, new String[] {"test@gmail.com"});

        String email = "test2@gmail.com";

        when(ctx.alloc()).thenReturn(allocator);
        when(allocator.ioBuffer(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeByte(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);

        when(userDao.isUserExists(email, AppNameUtil.BLYNK)).thenReturn(false);
        registerHandler.channelRead0(ctx, new RegisterMessage(1, email + "\0" + "1"));

        verify(userDao, times(0)).add(eq(email), eq("1"), eq(AppNameUtil.BLYNK));
        //verify(ctx).writeAndFlush(eq(new ResponseMessage(1, NOT_ALLOWED)), any());
    }

    @Test
    public void testAllowedUsersSingleUserWork2() throws Exception {
        RegisterHandler registerHandler = new RegisterHandler(userDao, tokenManager, timerWorker, new String[] {"test@gmail.com", "test2@gmail.com"});

        String userName = "test2@gmail.com";

        when(ctx.alloc()).thenReturn(allocator);
        when(allocator.ioBuffer(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeByte(anyInt())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);
        when(byteBuf.writeShort(anyShort())).thenReturn(byteBuf);

        when(userDao.isUserExists(userName, AppNameUtil.BLYNK)).thenReturn(false);
        registerHandler.channelRead0(ctx, new RegisterMessage(1, userName + "\0" + "1"));

        verify(userDao).add(eq(userName), eq("1"), eq(AppNameUtil.BLYNK));
    }

}
