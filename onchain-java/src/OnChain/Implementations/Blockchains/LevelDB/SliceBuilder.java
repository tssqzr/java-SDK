package OnChain.Implementations.Blockchains.LevelDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import OnChain.UIntBase;

public class SliceBuilder
{
    private ByteArrayOutputStream data = new ByteArrayOutputStream();

    private SliceBuilder()
    {
    }

    public SliceBuilder Add(byte value)
    {
        data.write(value);
        return this;
    }

    public SliceBuilder Add(short value)
    {
        // TODO
        //data.AddRange(BitConverter.GetBytes(value));
        return this;
    }

    public SliceBuilder Add(int value)
    {
        //data.AddRange(BitConverter.GetBytes(value));
        return this;
    }

    public SliceBuilder Add(long value)
    {
        //data.AddRange(BitConverter.GetBytes(value));
        return this;
    }

    public SliceBuilder Add(byte[] value)
    {
        try {
            data.write(value);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public SliceBuilder Add(String value)
    {
        try {
            Add(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public SliceBuilder Add(UIntBase value)
    {
        Add(value.toArray());
        return this;
    }

    public static SliceBuilder Begin()
    {
        return new SliceBuilder();
    }

//    public static SliceBuilder Begin(DataEntryPrefix prefix)
//    {
//        return new SliceBuilder().Add((byte)prefix);
//    }
//
//    public static implicit operator Slice(SliceBuilder value)
//    {
//        return value.data.ToArray();
//    }
}