package OnChain.Network.Payloads;

import OnChain.Core.Block;
import OnChain.IO.*;

public class HeadersPayload implements Serializable
{
    public Block[] Headers;
    
    private HeadersPayload(Block[] hds)
    {
        Headers = hds;
    }

    public static HeadersPayload Create(Block[] headers)
    {
        return new HeadersPayload(headers);
    }

    @Override
    public void deserialize(BinaryReader reader)
    {
        // TODO
//        Headers = reader.ReadSerializableArray<Block>();
//        if (Headers.Any(p => !p.IsHeader))
//            throw new FormatException();
    }

    @Override
    public void serialize(BinaryWriter writer)
    {
        //writer.Write(Headers);
    }
}
