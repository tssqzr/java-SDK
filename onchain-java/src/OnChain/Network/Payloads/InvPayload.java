package OnChain.Network.Payloads;

import OnChain.UInt256;
import OnChain.IO.*;
import OnChain.Network.InventoryType;

public class InvPayload implements Serializable
{
    public InventoryVector[] Inventories;

    private InvPayload(InventoryVector[] vcts)
    {
        Inventories = vcts;
    }
    
    public static InvPayload Create(InventoryVector[] vectors)
    {
        return new InvPayload(vectors);
    }

    public static InvPayload Create(InventoryType type, UInt256[] hashes)
    {
        return new InvPayload(null);
        // TODO
//        {
//            Inventories = hashes.Select(p => new InventoryVector { Type = type, Hash = p }).ToArray()
//        };
    }

    @Override
    public void deserialize(BinaryReader reader)
    {
        //TODO
        //Inventories = reader.ReadSerializableArray<InventoryVector>();
    }

    @Override
    public void serialize(BinaryWriter writer)
    {
        // TODO
        //writer.Write(Inventories);
    }
}
