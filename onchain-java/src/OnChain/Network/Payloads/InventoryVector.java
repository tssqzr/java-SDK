package OnChain.Network.Payloads;

import OnChain.UInt256;
import OnChain.IO.*;
import OnChain.Network.InventoryType;

public class InventoryVector implements Serializable
{
    public InventoryType Type;
    public UInt256 Hash;

    @Override
    public void deserialize(BinaryReader reader)
    {
        // TODO
//        Type = (InventoryType)reader.ReadUInt32();
//        if (!Enum.IsDefined(typeof(InventoryType), Type))
//            throw new FormatException();
//        Hash = reader.ReadSerializable<UInt256>();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof InventoryVector)) return false;
        if (this == obj) return true;
        return Hash == ((InventoryVector) obj).Hash;
    }

    @Override
    public int hashCode()
    {
        return Hash.hashCode();
    }

    @Override
    public void serialize(BinaryWriter writer)
    {
//        writer.Write((uint)Type);
//        writer.Write(Hash);
    }
}