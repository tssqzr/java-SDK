package OnChain.Core;

import java.io.*;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import OnChain.*;
import OnChain.Core.Scripts.*;
import OnChain.Cryptography.MerkleTree;
import OnChain.IO.*;
import OnChain.IO.Serializable;
import OnChain.IO.Json.*;
import OnChain.Network.*;
import OnChain.Wallets.Wallet;

/**
 *  区块或区块头
 */
public class Block extends Inventory
{
    /**
     *  区块版本
     */
    public int version; // unsigned int
    /**
     *  前一个区块的散列值
     */
    public UInt256 prevBlock;
    /**
     *  该区块中所有交易的Merkle树的根
     */
    public UInt256 merkleRoot;
    /**
     *  时间戳
     */
    public int timestamp; // unsigned int
    /**
     *  区块高度
     */
    public int height; // unsigned int
    /**
     *  随机数
     */
    public long nonce; // unsigned long
    /**
     *  下一个区块的记账合约的散列值
     */
    public UInt160 nextMiner;
    /**
     *  用于验证该区块的脚本
     */
    public Script script;
    /**
     *  交易列表，当列表中交易的数量为0时，该Block对象表示一个区块头
     */
    public Transaction[] transactions;

// TODO
//    [NonSerialized]
    private Block _header = null;
    /**
     *  该区块的区块头
     */
    public Block header()
    {
        if (isHeader()) return this;
        if (_header == null)
        {
            _header = new Block();
            _header.prevBlock = prevBlock;
            _header.merkleRoot = this.merkleRoot;
            _header.timestamp = this.timestamp;
            _header.height = this.height;
            _header.nonce = this.nonce;
            _header.nextMiner = this.nextMiner;
            _header.script = this.script;
            _header.transactions = new Transaction[0];
        }
        return _header;
    }

    /**
     *  资产清单的类型
     */
    @Override
	public InventoryType inventoryType() { return InventoryType.Block; }

    /**
     *  返回当前Block对象是否为区块头
     */
    public boolean isHeader() { return transactions.length == 0; }

    public static Fixed8 calculateNetFee(Stream<Transaction> transactions)
    {
    	//TODO
//        Transaction[] ts = transactions.Where(p => p.Type != TransactionType.MinerTransaction && p.Type != TransactionType.ClaimTransaction).ToArray();
//        Fixed8 amount_in = ts.SelectMany(p => p.References.Values.Where(o => o.AssetId == Blockchain.AntCoin.Hash)).Sum(p => p.Value);
//        Fixed8 amount_out = ts.SelectMany(p => p.Outputs.Where(o => o.AssetId == Blockchain.AntCoin.Hash)).Sum(p => p.Value);
//        Fixed8 amount_sysfee = ts.Sum(p => p.SystemFee);
//        return amount_in - amount_out - amount_sysfee;
        return new Fixed8(0);
    }

    /**
     *  反序列化
     *  <param name="reader">数据来源</param>
     * @throws IOException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    @Override public void deserialize(BinaryReader reader) throws IOException
    {
        deserializeUnsigned(reader);
        if (reader.readByte() != 1) throw new IOException();
        try
        {
			script = reader.readSerializable(Script.class);
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
        	throw new IOException(ex);
		}
        transactions = new Transaction[(int) reader.readVarInt(0x10000000)];
        for (int i = 0; i < transactions.length; i++)
        {
            transactions[i] = Transaction.deserializeFrom(reader);
        }
        if (transactions.length > 0)
        {
            if (transactions[0].type != TransactionType.MinerTransaction || Arrays.stream(transactions).skip(1).anyMatch(p -> p.type == TransactionType.MinerTransaction))
                throw new IOException();
            if (!merkleRoot.equals(MerkleTree.computeRoot(Arrays.stream(transactions).map(p -> p.hash()).toArray(UInt256[]::new))))
                throw new IOException();
        }
    }

    @Override public void deserializeUnsigned(BinaryReader reader) throws IOException
    {
        try
        {
            version = reader.readInt();
            prevBlock = reader.readSerializable(UInt256.class);
            merkleRoot = reader.readSerializable(UInt256.class);
            timestamp = reader.readInt();
            height = reader.readInt();
            nonce = reader.readLong();
			nextMiner = reader.readSerializable(UInt160.class);
	        transactions = new Transaction[0];
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
        	throw new IOException(ex);
		}
    }

    /**
     *  比较当前区块与指定区块是否相等
     *  <param name="obj">要比较的区块</param>
     *  <returns>返回对象是否相等</returns>
     */
    @Override public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof Block)) return false;
        return this.hash().equals(((Block) obj).hash());
    }
    
    public static Block fromTrimmedData(byte[] data, int index) throws IOException
    {
    	return fromTrimmedData(data, index, null);
    }

    public static Block fromTrimmedData(byte[] data, int index, Function<UInt256, Transaction> txSelector) throws IOException
    {
        Block block = new Block();
        try (ByteArrayInputStream ms = new ByteArrayInputStream(data, index, data.length - index))
        {
	        try (BinaryReader reader = new BinaryReader(ms))
	        {
	        	block.deserializeUnsigned(reader);
	        	reader.readByte(); block.script = reader.readSerializable(Script.class);
	        	if (txSelector == null)
	        	{
	        		block.transactions = new Transaction[0];
	        	}
	        	else
	        	{
		        	block.transactions = new Transaction[(int)reader.readVarInt(0x10000000)];
		        	for (int i = 0; i < block.transactions.length; i++)
		        	{
		        		block.transactions[i] = txSelector.apply(reader.readSerializable(UInt256.class));
		        	}
	        	}
	        }
	        catch (InstantiationException | IllegalAccessException ex)
	        {
				throw new IOException(ex);
			}
        }
        return block;
    }

    /**
     *  获得区块的HashCode
     *  <returns>返回区块的HashCode</returns>
     */
    @Override public int hashCode()
    {
        return hash().hashCode();
    }

    @Override public UInt160[] getScriptHashesForVerifying()
    {
        if (prevBlock.equals(UInt256.ZERO))
            return new UInt160[] { Script.toScriptHash(script.redeemScript) };
        Block prev_header;
		try
		{
			prev_header = Blockchain.current().getHeader(prevBlock);
		}
		catch (Exception ex)
		{
			throw new IllegalStateException(ex);
		}
        if (prev_header == null) throw new IllegalStateException();
        return new UInt160[] { prev_header.nextMiner };
    }

    /**
     *  根据区块中所有交易的Hash生成MerkleRoot
     */
    public void rebuildMerkleRoot()
    {
        merkleRoot = MerkleTree.computeRoot(Arrays.stream(transactions).map(p -> p.hash()).toArray(UInt256[]::new));
    }

    /**
     *  序列化
     *  <param name="writer">存放序列化后的数据</param>
     * @throws IOException 
     */
    @Override public void serialize(BinaryWriter writer) throws IOException
    {
        serializeUnsigned(writer);
        writer.writeByte((byte)1); writer.writeSerializable(script);
        writer.writeSerializableArray(transactions);
    }

    @Override public void serializeUnsigned(BinaryWriter writer) throws IOException
    {
        writer.writeInt(version);
        writer.writeSerializable(prevBlock);
        writer.writeSerializable(merkleRoot);
        writer.writeInt(timestamp);
        writer.writeInt(height);
        writer.writeLong(nonce);
        writer.writeSerializable(nextMiner);
    }

    /**
     *  变成json对象
     *  <returns>返回json对象</returns>
     */
    public JObject json()
    {
        JObject json = new JObject();
        json.set("hash", new JString(hash().toString()));
        json.set("version", new JNumber(Integer.toUnsignedLong(version)));
        json.set("previousblockhash", new JString(prevBlock.toString()));
        json.set("merkleroot", new JString(merkleRoot.toString()));
        json.set("time", new JNumber(timestamp));
        json.set("height", new JNumber(Integer.toUnsignedLong(height)));
        json.set("nonce", new JString(Long.toHexString(nonce)));
        json.set("nextminer", new JString(Wallet.toAddress(nextMiner)));
        json.set("script", script.json());
        json.set("tx", new JArray(Arrays.stream(transactions).map(p -> p.json()).toArray(JObject[]::new)));
        return json;
    }

    /**
     *  把区块对象变为只包含区块头和交易Hash的字节数组，去除交易数据
     *  <returns>返回只包含区块头和交易Hash的字节数组</returns>
     */
    public byte[] trim()
    {
        try (ByteArrayOutputStream ms = new ByteArrayOutputStream())
        {
	        try (BinaryWriter writer = new BinaryWriter(ms))
	        {
	            serializeUnsigned(writer);
	            writer.writeByte((byte)1); writer.writeSerializable(script);
	            writer.writeSerializableArray(Arrays.stream(transactions).map(p -> p.hash()).toArray(Serializable[]::new));
	            writer.flush();
	            return ms.toByteArray();
	        }
        }
        catch (IOException ex)
        {
        	throw new UnsupportedOperationException(ex);
		}
    }

    /**
     *  验证该区块头是否合法
     *  <returns>返回该区块头的合法性，返回true即为合法，否则，非法。</returns>
     */
    @Override public boolean verify()
    {
        return verify(false);
    }

    /**
     *  验证该区块头是否合法
     *  <param name="completely">是否同时验证区块中的每一笔交易</param>
     *  <returns>返回该区块头的合法性，返回true即为合法，否则，非法。</returns>
     */
    public boolean verify(boolean completely)
    {
    	//TODO
//        if (Hash == Blockchain.GenesisBlock.Hash) return true;
//        if (Blockchain.Default.ContainsBlock(Hash)) return true;
//        if (completely && IsHeader) return false;
//        if (!Blockchain.Default.Ability.HasFlag(BlockchainAbility.TransactionIndexes) || !Blockchain.Default.Ability.HasFlag(BlockchainAbility.UnspentIndexes))
//            return false;
//        Block prev_header = Blockchain.Default.GetHeader(PrevBlock);
//        if (prev_header == null) return false;
//        if (prev_header.Height + 1 != Height) return false;
//        if (prev_header.Timestamp >= Timestamp) return false;
//        if (!this.VerifySignature()) return false;
//        if (completely)
//        {
//            if (NextMiner != Blockchain.GetMinerAddress(Blockchain.Default.GetMiners(Transactions).ToArray()))
//                return false;
//            foreach (Transaction tx in Transactions)
//                if (!tx.Verify()) return false;
//            Transaction tx_gen = Transactions.FirstOrDefault(p => p.Type == TransactionType.MinerTransaction);
//            if (tx_gen?.Outputs.Sum(p => p.Value) != CalculateNetFee(Transactions)) return false;
//        }
        return false;
    }
}