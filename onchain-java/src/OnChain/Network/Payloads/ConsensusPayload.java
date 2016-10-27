package OnChain.Network.Payloads;

import OnChain.*;
import OnChain.Core.Scripts.Script;
import OnChain.IO.*;
import OnChain.Network.*;

public class ConsensusPayload extends Inventory
{
    public int Version;
    public UInt256 PrevHash;
    public int Height;
    public short MinerIndex;
    public int Timestamp;
    public byte[] Data;
    public Script script;

    @Override
    public InventoryType inventoryType() {
        return InventoryType.Consensus;
    }

    @Override
    public void deserialize(BinaryReader reader)
    {
        deserializeUnsigned(reader);
        // TODO if (reader.read() != 1) throw new FormatException();
        // TODO
        //Script = reader.ReadSerializable<Script>();
    }

    @Override
    public void deserializeUnsigned(BinaryReader reader)
    {
        // TODO
//        Version = reader.ReadUInt32();
//        PrevHash = reader.ReadSerializable<UInt256>();
//        Height = reader.ReadUInt32();
//        MinerIndex = reader.ReadUInt16();
//        Timestamp = reader.ReadUInt32();
//        Data = reader.ReadVarBytes();
    }

    @Override
    public UInt160[] getScriptHashesForVerifying()
    {
        // TODO
//        if (Blockchain.Default == null)
//            throw new InvalidOperationException();
//        if (PrevHash != Blockchain.Default.CurrentBlockHash)
//            throw new InvalidOperationException();
//        ECPoint[] miners = Blockchain.Default.GetMiners();
//        if (miners.Length <= MinerIndex)
//            throw new InvalidOperationException();
//        return new[] { SignatureContract.Create(miners[MinerIndex]).ScriptHash };
        return new UInt160[1];
    }

    @Override
    public void serialize(BinaryWriter writer)
    {
        serializeUnsigned(writer);
        // TODO writer.write((byte)1); // TODO writer.Write(Script);
    }

    @Override
    public void serializeUnsigned(BinaryWriter writer)
    {
        // TODO
//        writer.Write(Version);
//        writer.Write(PrevHash);
//        writer.Write(Height);
//        writer.Write(MinerIndex);
//        writer.Write(Timestamp);
//        writer.WriteVarBytes(Data);
    }

    @Override
    public boolean verify()
    {
        // TODO
//        if (Blockchain.Default == null) return false;
//        if (!Blockchain.Default.Ability.HasFlag(BlockchainAbility.TransactionIndexes) || !Blockchain.Default.Ability.HasFlag(BlockchainAbility.UnspentIndexes))
//            return false;
//        if (PrevHash != Blockchain.Default.CurrentBlockHash)
//            return false;
//        if (Height != Blockchain.Default.Height + 1)
//            return false;
//        return this.VerifySignature();
        return false;
    }

}