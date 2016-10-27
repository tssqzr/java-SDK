package OnChain.Network;

import OnChain.IO.*;

class Message implements Serializable
{
//#if TESTNET
//    public const uint Magic = 0x74746e41;
//#else
//    public const uint Magic = 0x00746e41;
//#endif
    // TODO
    public static final int Magic = 0x74746e41;
    
    public String Command;
    public int Checksum;
    public byte[] Payload;

    private Message(String cmd, int chks, byte[] pl) {
        Command = cmd;
        Checksum = chks;
        Payload = pl;
    }

    // TODO
//    public static Message Create(string command, ISerializable payload = null)
//    {
//        return Create(command, payload == null ? new byte[0] : payload.ToArray());
//    }

    public static Message Create(String command, byte[] payload)
    {
        return new Message(command, GetChecksum(payload), payload);
    }

    // TODO
    @Override
    public void deserialize(BinaryReader reader)
    {
//        if (reader.ReadUInt32() != Magic)
//            throw new FormatException();
//        this.Command = reader.ReadFixedString(12);
//        uint length = reader.ReadUInt32();
//        if (length > 0x02000000)
//            throw new FormatException();
//        this.Checksum = reader.ReadUInt32();
//        this.Payload = reader.ReadBytes((int)length);
//        if (GetChecksum(Payload) != Checksum)
//            throw new FormatException();
    }

    private static int GetChecksum(byte[] value)
    {
        // TODO
        //return BitConverter.ToUInt32(value.Sha256().Sha256(), 0);
        return 0;
    }

    // TODO
    @Override
    public void serialize(BinaryWriter writer)
    {
//        writer.Write(Magic);
//        writer.WriteFixedString(Command, 12);
//        writer.Write(Payload.Length);
//        writer.Write(Checksum);
//        writer.Write(Payload);
    }
}