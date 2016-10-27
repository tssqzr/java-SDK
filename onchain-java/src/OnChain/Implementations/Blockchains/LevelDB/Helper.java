package OnChain.Implementations.Blockchains.LevelDB;

import java.util.ArrayList;
import java.util.List;

class Helper
{
    // TODO
//    public static Iterable<T> Find<T>(this DB db, ReadOptions options, Slice prefix, Func<Slice, Slice, T> resultSelector)
//    {
//        using (Iterator it = db.NewIterator(options))
//        {
//            for (it.Seek(prefix); it.Valid(); it.Next())
//            {
//                Slice key = it.Key();
//                byte[] x = key.ToArray();
//                byte[] y = prefix.ToArray();
//                if (x.Length < y.Length) break;
//                if (!x.Take(y.Length).SequenceEqual(y)) break;
//                yield return resultSelector(key, it.Value());
//            }
//        }
//    }

    public static short[] getUInt16Array(byte[] source)
    {
        if (source == null) throw new NullPointerException("param source is null");
        int rem = source.length % 2;
        int size = source.length / 2;
        if (rem != 0) throw new IllegalArgumentException();
        short[] dst = new short[size];
        //System.arraycopy(source, 0, dst, 0, source.length);
        for (int i = 0; i < dst.length; i++) {
            int low = source[i + i] & 0xFF;
            int high = source[i + i + 1] & 0xFF;
            int value = (low << 8) | high;
            dst[i] = (short) value;
        }
        return dst;
    }

    // TODO range of ushort is up to 65535, but short is up to 32767.
    public static Iterable<Short> range(short start, int count)
    {
        if (count < 0 || start + count > Short.MAX_VALUE)
            throw new IllegalArgumentException();
        List<Short> shorts = new ArrayList<Short>();
        for (short i = 0; i < count; i++)
            shorts.add((short)(i + start));
        return shorts;
    }

    public static byte[] toByteArray(short[] source)
    {
        byte[] dst = new byte[source.length * 2];
        //Buffer.BlockCopy(src, 0, dst, 0, dst.Length);
        for (int i = 0; i < source.length; i++) {
            byte low = (byte)((source[i] & 0xFF00) >> 8);
            byte high = (byte)(source[i] & 0xFF);
            dst[i + i] = low;
            dst[i + i + 1] = high;
        }
        return dst;
    }
}