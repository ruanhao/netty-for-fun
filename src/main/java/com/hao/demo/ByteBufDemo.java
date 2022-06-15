package com.hao.demo;

import com.hao.util.ByteBufHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.IllegalReferenceCountException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufDemo {

    private static void compositeDemo() {
        ByteBuf buf1 = Unpooled.copiedBuffer(new byte[] {1, 2, 3});
        ByteBuf buf2 = Unpooled.copiedBuffer(new byte[] {4, 5, 6});
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        byteBuf.addComponents(true, buf1, buf2);
        ByteBufHelper.printAll(byteBuf);

    }

    private static void wrappedDemo() {
        ByteBuf buf1 = Unpooled.copiedBuffer(new byte[] {1, 2, 3});
        ByteBuf buf2 = Unpooled.copiedBuffer(new byte[] {4, 5, 6});
        ByteBuf byteBuf = Unpooled.wrappedBuffer(buf1, buf2);
        ByteBufHelper.printAll(byteBuf);

    }

    private static void sliceDemo() {
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        ByteBuf s1 = byteBuf.slice(0, 5);
        ByteBuf s2 = byteBuf.slice(5, 5);

        ByteBufHelper.printAll(byteBuf);
        ByteBufHelper.printAll(s1);
        ByteBufHelper.printAll(s2);

        s1.setByte(3, 'x');
        assert 'x' == byteBuf.getByte(3);
        assert 5 == s1.maxCapacity();
        try {
            s1.writeByte('x');
        } catch (IndexOutOfBoundsException e) {
            log.error("slice is read-only: {}", e.getMessage());
        }
        ByteBufHelper.printAll(byteBuf);

        byteBuf.release();
        try {
            ByteBufHelper.printAll(s1);
        } catch (IllegalReferenceCountException e) {
            log.error("Cannot use slice after origin buf is released: {}", e.getMessage());
        }

    }

    public static void main(String[] args) {
        // sliceDemo();
        // compositeDemo();
        wrappedDemo();
    }
}
