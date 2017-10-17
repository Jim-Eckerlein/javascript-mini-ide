package io.jimeckerlein.jsshell.editor;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ReallocatingIntBuffer {
    
    /**
     * The underlying byte buffer
     */
    private ByteBuffer mByteBuffer;
    
    /**
     * An integer buffer view, since this buffers only uses and exposes integers
     */
    private IntBuffer mBuffer;
    
    /**
     * The memory size in bytes the buffer was created with.
     * On reallocation the buffer is extended by that size.
     */
    private final int mInitialCapacity;
    
    /**
     * Becomes true when calling <code>get()</code>.
     * Each change between putting or getting resets the read/write position.
     */
    private boolean mReading = false;
    
    private int mMaxWritePosition;
    
    public ReallocatingIntBuffer(int initialIntegerCapacity) {
        mInitialCapacity = initialIntegerCapacity * 4;
        mByteBuffer = ByteBuffer.allocateDirect(mInitialCapacity);
        mBuffer = mByteBuffer.asIntBuffer();
    }
    
    /**
     * Appends i to the buffer.
     * In case of insufficient memory, buffer is reallocated.
     * @param i Int to be added.
     */
    public void put(int i) {
        if(mReading) {
            throw new IllegalStateException("Cannot put int of writing has not been started");
        }
        
        if(mBuffer.hasRemaining()) {
            mBuffer.put(i);
        }
        
        else {
            int position = mBuffer.position();
            ByteBuffer newBuffer = ByteBuffer.allocateDirect(mByteBuffer.capacity() + mInitialCapacity);
            newBuffer.put(mByteBuffer);
            mByteBuffer = newBuffer;
            mByteBuffer.rewind();
            mBuffer = mByteBuffer.asIntBuffer();
            mBuffer.position(position);
            mBuffer.put(i);
        }
    }
    
    /**
     * Returns the next int.
     * May throw an <code>BufferUnderflowException</code> if reading out of bounds.
     * @return Next int.
     */
    public int get() {
        if(!mReading) {
            throw new IllegalStateException("Cannot get int of reading has not been started");
        }
        return mBuffer.get();
    }
    
    public boolean isReading() {
        return mReading;
    }
    
    public int getCapacity() {
        return mBuffer.capacity();
    }
    
    public void startReading() {
        mMaxWritePosition = mBuffer.position();
        mBuffer.rewind();
        mReading = true;
    }
    
    public void startWriting() {
        mBuffer.rewind();
        mReading = false;
        mMaxWritePosition = 0;
    }
    
    public boolean hasRemaining() {
        if(!mReading) {
            throw new IllegalStateException("Cannot check for remaining ints when writing");
        }
        return mBuffer.position() < mMaxWritePosition;
    }
    
    @Override
    public String toString() {
        int position = mBuffer.position();
        mBuffer.rewind();
        StringBuilder sb = new StringBuilder("Buffer ");
        sb.append(getCapacity()).append(": [ ");
        while(mBuffer.hasRemaining()) {
            sb.append(mBuffer.get()).append(' ');
        }
        mBuffer.position(position);
        sb.append(']');
        return sb.toString();
    }
}
