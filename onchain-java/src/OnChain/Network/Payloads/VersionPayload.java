package OnChain.Network.Payloads;

import OnChain.IO.*;

public class VersionPayload implements Serializable
{
    public int Version;
    public long Services;
    public int Timestamp;
    public short Port;
    public int Nonce;
    public String UserAgent;
    public int StartHeight;

    private VersionPayload(int port, int nonce, String userAgent)
    {
        // TODO
//        Version = LocalNode.PROTOCOL_VERSION,
//        Services = NetworkAddressWithTime.NODE_NETWORK,
//        Timestamp = DateTime.Now.ToTimestamp(),
//        Port = (ushort)port,
//        Nonce = nonce,
//        UserAgent = userAgent,
//        StartHeight = Blockchain.Default?.Height ?? 0
    }

    public static VersionPayload Create(int port, int nonce, String userAgent)
    {
        return new VersionPayload(port, nonce, userAgent);
    }

    @Override
    public void deserialize(BinaryReader reader)
    {
        // TODO
//        Version = reader.ReadUInt32();
//        Services = reader.ReadUInt64();
//        Timestamp = reader.ReadUInt32();
//        Port = reader.ReadUInt16();
//        Nonce = reader.ReadUInt32();
//        UserAgent = reader.ReadVarString();
//        StartHeight = reader.ReadUInt32();
    }

    @Override
    public void serialize(BinaryWriter writer)
    {
        // TODO
//        writer.Write(Version);
//        writer.Write(Services);
//        writer.Write(Timestamp);
//        writer.Write(Port);
//        writer.Write(Nonce);
//        writer.WriteVarString(UserAgent);
//        writer.Write(StartHeight);
    }
}
