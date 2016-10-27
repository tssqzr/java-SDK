package OnChain.IO.Json;

import java.io.BufferedReader;
import java.io.IOException;

public class JNumber extends JObject
{
    private double _value;
    public double value() { return _value; }

    public JNumber(double val)
    {
        this._value = val;
    }

    @Override
    public boolean asBoolean()
    {
        if (_value == 0)
            return false;
        return true;
    }

// TODO
//    public override T AsEnum<T>(bool ignoreCase = false)
//    {
//        Type t = typeof(T);
//        if (!t.IsEnum)
//            throw new InvalidCastException();
//        if (t.GetEnumUnderlyingType() == typeof(byte))
//            return (T)Enum.ToObject(t, (byte)Value);
//        if (t.GetEnumUnderlyingType() == typeof(int))
//            return (T)Enum.ToObject(t, (int)Value);
//        if (t.GetEnumUnderlyingType() == typeof(long))
//            return (T)Enum.ToObject(t, (long)Value);
//        if (t.GetEnumUnderlyingType() == typeof(sbyte))
//            return (T)Enum.ToObject(t, (sbyte)Value);
//        if (t.GetEnumUnderlyingType() == typeof(short))
//            return (T)Enum.ToObject(t, (short)Value);
//        if (t.GetEnumUnderlyingType() == typeof(uint))
//            return (T)Enum.ToObject(t, (uint)Value);
//        if (t.GetEnumUnderlyingType() == typeof(ulong))
//            return (T)Enum.ToObject(t, (ulong)Value);
//        if (t.GetEnumUnderlyingType() == typeof(ushort))
//            return (T)Enum.ToObject(t, (ushort)Value);
//        throw new InvalidCastException();
//    }

    @Override
    public double asNumber()
    {
        return _value;
    }

    @Override
    public String asString()
    {
        return String.valueOf(_value);
    }

    @Override
    public boolean canConvertTo(Class<?> type)
    {
        if (type.equals(boolean.class))
            return true;
// TODO
//        if (type.IsEnum && Enum.IsDefined(type, Convert.ChangeType(Value, type.GetEnumUnderlyingType())))
//            return true;
        if (type.equals(double.class))
            return true;
        if (type.equals(String.class))
            return true;
        return false;
    }

    static JNumber parseNumber(BufferedReader reader) throws IOException
    {
        skipSpace(reader);
        StringBuilder sb = new StringBuilder();
        while (true)
        {
        	reader.mark(1);
            int c = reader.read();
            if (c >= '0' && c <= '9' || c == '.' || c == '-')
            {
                sb.append((char)c);
            }
            else
            {
            	reader.reset();
                break;
            }
        }
        return new JNumber(Double.parseDouble(sb.toString()));
    }

    @Override
    public String toString()
    {
        return asString();
    }
}
