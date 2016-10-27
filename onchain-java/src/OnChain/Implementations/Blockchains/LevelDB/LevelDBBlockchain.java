package OnChain.Implementations.Blockchains.LevelDB;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import OnChain.*;
import OnChain.Core.*;
import OnChain.IO.Caching.*;

public class LevelDBBlockchain extends Blockchain
{
//    private DB db;
    private Thread thread_persistence;
    private Tree<UInt256, Block> header_chain = new Tree<UInt256, Block>(GENESIS_BLOCK.hash(), GENESIS_BLOCK);
    private List<UInt256> header_index = new ArrayList<UInt256>();
    private Map<UInt256, Block> block_cache = new HashMap<UInt256, Block>();
    private UInt256 current_block_hash = GENESIS_BLOCK.hash();
    private UInt256 current_header_hash = GENESIS_BLOCK.hash();
    private int current_block_height = 0;
    private int stored_header_count = 0;
    // TODO
    private /*AutoResetEvent*/ Object new_block_event = new Object();//AutoResetEvent(false);
    private boolean disposed = false;

    @Override
    public EnumSet<BlockchainAbility> ability() { return BlockchainAbility.All;}
    @Override 
    public UInt256 currentBlockHash() { return current_block_hash; }
    @Override 
    public UInt256 currentHeaderHash() { return current_header_hash; }
    @Override 
    public int headerHeight() { return header_chain.get(current_header_hash).height; }
    @Override 
    public int height() { return current_block_height; }
    @Override
    public boolean isReadOnly() { return false; }
    private boolean _verifyBlocks = true;
    public boolean needVerifyBlocks() { return _verifyBlocks; }
    public void setVerifyBlocks(boolean value) { _verifyBlocks = value; }

    public LevelDBBlockchain(String path)
    {
        header_index.add(GENESIS_BLOCK.hash());
//        Version version;
//        Slice value;
//        db = DB.Open(path, new Options { CreateIfMissing = true });
//        if (db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.CFG_Version), out value) && Version.TryParse(value.ToString(), out version) && version >= Version.Parse("0.6.6043.32131"))
//        {
//            ReadOptions options = new ReadOptions { FillCache = false };
//            value = db.Get(options, SliceBuilder.Begin(DataEntryPrefix.SYS_CurrentBlock));
//            this.current_block_hash = new UInt256(value.ToArray().Take(32).ToArray());
//            this.current_block_height = BitConverter.ToUInt32(value.ToArray(), 32);
//            for (Block header : db.Find(options, SliceBuilder.Begin(DataEntryPrefix.DATA_HeaderList), (k, v) =>
//            {
//                using (MemoryStream ms = new MemoryStream(v.ToArray(), false))
//                using (BinaryReader r = new BinaryReader(ms))
//                {
//                    return new
//                    {
//                        Index = BitConverter.ToUInt32(k.ToArray(), 1),
//                        Headers = r.ReadSerializableArray<Block>()
//                    };
//                }
//            }).OrderBy(p => p.Index).SelectMany(p => p.Headers).ToArray())
//            {
//                if (header.hash() != GENESIS_BLOCK.hash())
//                {
//                    header_chain.add(header.hash(), header, header.prevBlock);
//                    header_index.add(header.hash());
//                }
//                stored_header_count++;
//            }
//            if (stored_header_count == 0)
//            {
//                Map<UInt256, Block> table = db.Find(options, SliceBuilder.Begin(DataEntryPrefix.DATA_Block), (k, v) => Block.FromTrimmedData(v.ToArray(), sizeof(long))).ToDictionary(p => p.prevBlock);
//                for (UInt256 hash = GENESIS_BLOCK.hash(); hash != current_block_hash;)
//                {
//                    Block header = table[hash];
//                    header_chain.add(header.hash(), header, header.prevBlock);
//                    header_index.add(header.hash());
//                    hash = header.hash();
//                }
//            }
//            else if (current_block_height >= stored_header_count)
//            {
//                List<Block> list = new List<Block>();
//                for (UInt256 hash = current_block_hash; hash != header_index[(int)stored_header_count - 1];)
//                {
//                    Block header = Block.FromTrimmedData(db.Get(options, SliceBuilder.Begin(DataEntryPrefix.DATA_Block).add(hash)).ToArray(), sizeof(long));
//                    list.add(header);
//                    header_index.Insert((int)stored_header_count, hash);
//                    hash = header.prevBlock;
//                }
//                for (int i = list.Count - 1; i >= 0; i--)
//                {
//                    header_chain.add(list[i].hash(), list[i], list[i].prevBlock);
//                }
//            }
//            this.current_header_hash = header_index[header_index.Count - 1];
//        }
//        else
//        {
//            WriteBatch batch = new WriteBatch();
//            ReadOptions options = new ReadOptions { FillCache = false };
//            using (Iterator it = db.NewIterator(options))
//            {
//                for (it.SeekToFirst(); it.Valid(); it.Next())
//                {
//                    batch.Delete(it.Key());
//                }
//            }
//            db.Write(WriteOptions.Default, batch);
//            Persist(GENESIS_BLOCK);
//            db.Put(WriteOptions.Default, SliceBuilder.Begin(DataEntryPrefix.CFG_Version), Assembly.GetExecutingAssembly().GetName().Version.ToString());
//        }
//        thread_persistence = new Thread(PersistBlocks);
//        thread_persistence.Name = "LevelDBBlockchain.PersistBlocks";
//        thread_persistence.Start();
//        AppDomain.CurrentDomain.ProcessExit += CurrentDomain_ProcessExit;
    }

    @Override
    protected boolean addBlock(Block block)
    {
        synchronized (block_cache)
        {
            if (!block_cache.containsKey(block.hash()))
            {
                block_cache.put(block.hash(), block);
            }
        }
        synchronized (header_chain)
        {
            if (header_chain.get(block.prevBlock) == null) return false;
            if (header_chain.get(block.hash()) == null)
            {
                if (needVerifyBlocks() && !block.verify()) return false;
                header_chain.Add(block.hash(), block.header(), block.prevBlock);
                OnAddHeader(block);
            }
            if (header_chain.get(block.hash()) != null)
            {
                // TODO new_block_event.Set();
            }
        }
        return true;
    }

    @Override 
    protected void addHeaders(Iterable<Block> headers)
    {
        synchronized (header_chain)
        {
            for (Block header : headers)
            {
                if (header_chain.get(header.prevBlock) != null) break;
                if (header_chain.get(header.hash()) == null) continue;
                if (needVerifyBlocks() && !header.verify()) break;
                header_chain.Add(header.hash(), header, header.prevBlock);
                OnAddHeader(header);
            }
        }
    }

    @Override 
    public boolean containsAsset(UInt256 hash)
    {
//        if (base.ContainsAsset(hash)) return true;
//        Slice value;
//        return db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Asset).add(hash), out value);
        return false;
    }

    @Override 
    public boolean containsBlock(UInt256 hash)
    {
//        TreeNode<Block> node, i;
//        synchronized (header_chain)
//        {
//            if (!header_chain.get(hash)) return false;
//            node = header_chain.Nodes[hash];
//            i = header_chain.Nodes[current_block_hash];
//        }
//        if (i.Height < node.Height) return false;
//        while (i.Height > node.Height)
//            i = i.Parent;
//        return i == node;
        return false;
    }

    @Override 
    public boolean containsTransaction(UInt256 hash)
    {
        return false;
//        if (base.ContainsTransaction(hash)) return true;
//        Slice value;
//        return db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.DATA_Transaction).add(hash), out value);
    }

    @Override 
    public boolean containsUnspent(UInt256 hash, int index)
    {
        return false;
//        Slice value;
//        if (!db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(hash), out value))
//            return false;
//        return value.ToArray().GetUInt16Array().Contains(index);
    }

    private void CurrentDomain_ProcessExit(Object sender, /*EventArgs*/ Object e)
    {
        close();
    }

    @Override 
    public void close() // Dispose()
    {
        disposed = true;
//        new_block_event.Set();
//        AppDomain.CurrentDomain.ProcessExit -= CurrentDomain_ProcessExit;
//        if (!thread_persistence.ThreadState.HasFlag(ThreadState.Unstarted))
//            thread_persistence.Join();
//        new_block_event.Dispose();
//        if (db != null)
//        {
//            db.Dispose();
//            db = null;
//        }
    }

    @Override 
    public Stream<RegisterTransaction> getAssets()
    {
        return null;
//        yield return AntCoin;
//        ReadOptions options = new ReadOptions();
//        using (options.Snapshot = db.GetSnapshot())
//        {
//            int height;
//            for (Slice key : db.Find(options, SliceBuilder.Begin(DataEntryPrefix.IX_Asset), (k, v) => k))
//            {
//                UInt256 hash = new UInt256(key.ToArray().Skip(1).ToArray());
//                yield return (RegisterTransaction)GetTransaction(options, hash, out height);
//            }
//        }
    }

    @Override
    public Block getBlock(UInt256 hash)
    {
        return null;
//        Block block = base.GetBlock(hash);
//        if (block == null)
//        {
//            block = GetBlockInternal(ReadOptions.Default, hash);
//        }
//        return block;
    }

    @Override 
    public UInt256 getBlockHash(int height)
    {
        return null;
//        UInt256 hash = base.GetBlockHash(height);
//        if (hash != null) return hash;
//        if (current_block_height < height) return null;
//        synchronized (header_chain)
//        {
//            if (header_index.Count <= height) return null;
//            return header_index[(int)height];
//        }
    }

    private Block GetBlockInternal(/*ReadOptions*/ Object options, UInt256 hash)
    {
        return null;
//        Slice value;
//        if (!db.TryGet(options, SliceBuilder.Begin(DataEntryPrefix.DATA_Block).add(hash), out value))
//            return null;
//        int height;
//        return Block.FromTrimmedData(value.ToArray(), sizeof(long), p => GetTransaction(options, p, out height));
    }

    @Override 
    public Stream<EnrollmentTransaction> getEnrollments(Stream<Transaction> others)
    {
        return null;
//        ReadOptions options = new ReadOptions();
//        using (options.Snapshot = db.GetSnapshot())
//        {
//            int height;
//            for (Slice key : db.Find(options, SliceBuilder.Begin(DataEntryPrefix.IX_Enrollment), (k, v) => k))
//            {
//                UInt256 hash = new UInt256(key.ToArray().Skip(1).Take(32).ToArray());
//                if (others.SelectMany(p => p.GetAllInputs()).Any(p => p.PrevHash == hash && p.PrevIndex == 0))
//                    continue;
//                yield return (EnrollmentTransaction)GetTransaction(options, hash, out height);
//            }
//        }
//        for (EnrollmentTransaction tx : others.OfType<EnrollmentTransaction>())
//        {
//            yield return tx;
//        }
    }

    @Override 
    public Block getHeader(int height)
    {
        synchronized (header_chain)
        {
//            if (header_index.Count <= height) return null;
//            return header_chain[header_index[(int)height]];
        }
        return null;
    }

    @Override 
    public Block getHeader(UInt256 hash)
    {
        synchronized (header_chain)
        {
//            if (!header_chain.get(hash)) return null;
//            return header_chain[hash];
        }
        return null;
    }

    @Override 
    public UInt256[] getLeafHeaderHashes()
    {
        synchronized (header_chain)
        {
            //return header_chain.Leaves.Select(p => p.Item.hash()).ToArray();
        }
        return null;
    }

    @Override 
    public Block getNextBlock(UInt256 hash)
    {
        //return GetBlockInternal(ReadOptions.Default, GetNextBlockHash(hash));
        return null;
    }

    @Override 
    public UInt256 getNextBlockHash(UInt256 hash)
    {
        synchronized (header_chain)
        {
//            if (!header_chain.get(hash)) return null;
//            int height = header_chain.Nodes[hash].Height;
//            if (hash != header_index[(int)height]) return null;
//            if (header_index.Count <= height + 1) return null;
//            return header_chain[header_index[(int)height + 1]].hash();
        }
        return null;
    }

    @Override 
    public Fixed8 getQuantityIssued(UInt256 asset_id)
    {
//        Slice quantity = 0L;
//        db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.ST_QuantityIssued).add(asset_id), out quantity);
//        return new Fixed8(quantity.ToInt64());
        return null;
    }

    @Override 
    public long getSysFeeAmount(UInt256 hash)
    {
//        Slice value;
//        if (!db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.DATA_Block).add(hash), out value))
//            return -1;
//        return BitConverter.ToInt64(value.ToArray(), 0);
        return 0;
    }

    @Override 
    public Transaction getTransaction(UInt256 hash, Out<Integer> height)
    {
//        Transaction tx = base.GetTransaction(hash, out height);
//        if (tx == null)
//        {
//            tx = GetTransaction(ReadOptions.Default, hash, out height);
//        }
//        return tx;
        return null;
    }

    private Transaction GetTransaction(/*ReadOptions*/ Object options, UInt256 hash, Out<Integer> height)
    {
//        Slice value;
//        if (db.TryGet(options, SliceBuilder.Begin(DataEntryPrefix.DATA_Transaction).add(hash), out value))
//        {
//            byte[] data = value.ToArray();
//            height = BitConverter.ToInt32(data, 0);
//            return Transaction.DeserializeFrom(data, sizeof(int));
//        }
//        else
//        {
//            height = -1;
//            return null;
//        }
        return null;
    }

    @Override 
    public Map<Short, Claimable> getUnclaimed(UInt256 hash)
    {
        return null;
//        int height;
//        Transaction tx = GetTransaction(ReadOptions.Default, hash, out height);
//        if (tx == null) return null;
//        Slice value;
//        if (db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Unclaimed).add(hash), out value))
//        {
//            const int UnclaimedItemSize = sizeof(short) + sizeof(int);
//            byte[] data = value.ToArray();
//            return Enumerable.Range(0, data.Length / UnclaimedItemSize).ToDictionary(i => BitConverter.ToUInt16(data, i * UnclaimedItemSize), i => new Claimable
//            {
//                Output = tx.Outputs[BitConverter.ToUInt16(data, i * UnclaimedItemSize)],
//                StartHeight = (int)height,
//                EndHeight = BitConverter.ToUInt32(data, i * UnclaimedItemSize + sizeof(short))
//            });
//        }
//        else
//        {
//            return new Map<short, Claimable>();
//        }
    }

    @Override 
    public TransactionOutput getUnspent(UInt256 hash, int index) throws Exception
    {
//        ReadOptions options = new ReadOptions();
//        using (options.Snapshot = db.GetSnapshot())
//        {
//            Slice value;
//            if (!db.TryGet(options, SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(hash), out value))
//                return null;
//            if (!value.ToArray().GetUInt16Array().Contains(index))
//                return null;
//            int height;
//            return GetTransaction(options, hash, out height).Outputs[index];
//        }
        return null;
    }

    @Override 
    public Stream<Vote> getVotes(Stream<Transaction> others)
    {
//        ReadOptions options = new ReadOptions();
//        using (options.Snapshot = db.GetSnapshot())
//        {
//            int height;
//            for (var kv : db.Find(options, SliceBuilder.Begin(DataEntryPrefix.IX_Vote), (k, v) => new { Key = k, Value = v }))
//            {
//                UInt256 hash = new UInt256(kv.Key.ToArray().Skip(1).ToArray());
//                short[] indexes = kv.Value.ToArray().GetUInt16Array().Except(others.SelectMany(p => p.GetAllInputs()).Where(p => p.PrevHash == hash).Select(p => p.PrevIndex)).ToArray();
//                if (indexes.Length == 0) continue;
//                VotingTransaction tx = (VotingTransaction)GetTransaction(options, hash, out height);
//                yield return new Vote
//                {
//                    Enrollments = tx.Enrollments,
//                    Count = indexes.Sum(p => tx.Outputs[p].Value)
//                };
//            }
//        }
//        for (VotingTransaction tx : others.OfType<VotingTransaction>())
//        {
//            yield return new Vote
//            {
//                Enrollments = tx.Enrollments,
//                Count = tx.Outputs.Where(p => p.AssetId == AntShare.hash()).Sum(p => p.Value)
//            };
//        }
        return null;
    }

    @Override 
    public boolean isDoubleSpend(Transaction tx)
    {
//        TransactionInput[] inputs = tx.GetAllInputs().ToArray();
//        if (inputs.Length == 0) return false;
//        ReadOptions options = new ReadOptions();
//        using (options.Snapshot = db.GetSnapshot())
//        {
//            for (var group : inputs.GroupBy(p => p.PrevHash))
//            {
//                Slice value;
//                if (!db.TryGet(options, SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(group.Key), out value))
//                    return true;
//                HashSet<short> unspents = new HashSet<short>(value.ToArray().GetUInt16Array());
//                if (group.Any(p => !unspents.Contains(p.PrevIndex)))
//                    return true;
//            }
//        }
        return false;
    }

    private void OnAddHeader(Block header)
    {
//        if (header.prevBlock == current_header_hash)
//        {
//            current_header_hash = header.hash();
//            header_index.add(header.hash());
//            int height = header_chain.Nodes[current_header_hash].Height;
//            if (height % 2000 == 0)
//            {
//                WriteBatch batch = new WriteBatch();
//                while (height - 2000 > stored_header_count)
//                {
//                    using (MemoryStream ms = new MemoryStream())
//                    using (BinaryWriter w = new BinaryWriter(ms))
//                    {
//                        w.Write(header_index.Skip((int)stored_header_count).Take(2000).Select(p => header_chain[p]).ToArray());
//                        w.Flush();
//                        batch.Put(SliceBuilder.Begin(DataEntryPrefix.DATA_HeaderList).add(stored_header_count), ms.ToArray());
//                    }
//                    stored_header_count += 2000;
//                }
//                db.Write(WriteOptions.Default, batch);
//            }
//        }
//        else
//        {
//            TreeNode<Block> main = header_chain.Leaves.OrderByDescending(p => p.Height).First();
//            if (main.Item.hash() != current_header_hash)
//            {
//                TreeNode<Block> fork = header_chain.Nodes[current_header_hash];
//                current_header_hash = main.Item.hash();
//                TreeNode<Block> common = header_chain.FindCommonNode(main, fork);
//                header_index.RemoveRange((int)common.Height + 1, header_index.Count - (int)common.Height - 1);
//                for (TreeNode<Block> i = main; i != common; i = i.Parent)
//                {
//                    header_index.Insert((int)common.Height + 1, i.Item.hash());
//                }
//                if (header_chain.Nodes[current_block_hash].Height > common.Height)
//                {
//                    //Rollback(common.Item.hash());
//                    throw new InvalidDataException("Unexpected Rollback");
//                }
//            }
//        }
    }

    private void Persist(Block block)
    {
//        const int UnclaimedItemSize = sizeof(short) + sizeof(int);
//        MultiValueDictionary<UInt256, short> unspents = new MultiValueDictionary<UInt256, short>(p =>
//        {
//            Slice value = new byte[0];
//            db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(p), out value);
//            return new HashSet<short>(value.ToArray().GetUInt16Array());
//        });
//        MultiValueDictionary<UInt256, short, int> unclaimed = new MultiValueDictionary<UInt256, short, int>(p =>
//        {
//            Slice value = new byte[0];
//            db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Unclaimed).add(p), out value);
//            byte[] data = value.ToArray();
//            return Enumerable.Range(0, data.Length / UnclaimedItemSize).ToDictionary(i => BitConverter.ToUInt16(data, i * UnclaimedItemSize), i => BitConverter.ToUInt32(data, i * UnclaimedItemSize + sizeof(short)));
//        });
//        MultiValueDictionary<UInt256, short> unspent_votes = new MultiValueDictionary<UInt256, short>(p =>
//        {
//            Slice value = new byte[0];
//            db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Vote).add(p), out value);
//            return new HashSet<short>(value.ToArray().GetUInt16Array());
//        });
//        Map<UInt256, Fixed8> quantities = new Map<UInt256, Fixed8>();
//        WriteBatch batch = new WriteBatch();
//        long amount_sysfee = GetSysFeeAmount(block.prevBlock) + (long)block.Transactions.Sum(p => p.SystemFee);
//        batch.Put(SliceBuilder.Begin(DataEntryPrefix.DATA_Block).add(block.hash()), SliceBuilder.Begin().add(amount_sysfee).add(block.Trim()));
//        for (Transaction tx : block.Transactions)
//        {
//            batch.Put(SliceBuilder.Begin(DataEntryPrefix.DATA_Transaction).add(tx.hash()), SliceBuilder.Begin().add(block.Height).add(tx.ToArray()));
//            switch (tx.Type)
//            {
//                case TransactionType.IssueTransaction:
//                    for (TransactionResult result : tx.GetTransactionResults().Where(p => p.Amount < Fixed8.Zero))
//                    {
//                        if (quantities.ContainsKey(result.AssetId))
//                        {
//                            quantities[result.AssetId] -= result.Amount;
//                        }
//                        else
//                        {
//                            quantities.add(result.AssetId, -result.Amount);
//                        }
//                    }
//                    break;
//                case TransactionType.ClaimTransaction:
//                    for (TransactionInput input : ((ClaimTransaction)tx).Claims)
//                    {
//                        unclaimed.Remove(input.PrevHash, input.PrevIndex);
//                    }
//                    break;
//                case TransactionType.EnrollmentTransaction:
//                    {
//                        EnrollmentTransaction enroll_tx = (EnrollmentTransaction)tx;
//                        batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Enrollment).add(tx.hash()), true);
//                    }
//                    break;
//                case TransactionType.VotingTransaction:
//                    unspent_votes.AddEmpty(tx.hash());
//                    for (short index = 0; index < tx.Outputs.Length; index++)
//                    {
//                        if (tx.Outputs[index].AssetId == AntShare.hash())
//                        {
//                            unspent_votes.add(tx.hash(), index);
//                        }
//                    }
//                    break;
//                case TransactionType.RegisterTransaction:
//                    {
//                        RegisterTransaction reg_tx = (RegisterTransaction)tx;
//                        batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Asset).add(reg_tx.hash()), true);
//                    }
//                    break;
//            }
//            unspents.AddEmpty(tx.hash());
//            for (short index = 0; index < tx.Outputs.Length; index++)
//            {
//                unspents.add(tx.hash(), index);
//            }
//        }
//        for (var group : block.Transactions.SelectMany(p => p.GetAllInputs()).GroupBy(p => p.PrevHash))
//        {
//            int height;
//            Transaction tx = GetTransaction(ReadOptions.Default, group.Key, out height);
//            for (TransactionInput input : group)
//            {
//                if (input.PrevIndex == 0)
//                {
//                    batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Enrollment).add(input.PrevHash));
//                }
//                unspents.Remove(input.PrevHash, input.PrevIndex);
//                unspent_votes.Remove(input.PrevHash, input.PrevIndex);
//                if (tx?.Outputs[input.PrevIndex].AssetId == AntShare.hash())
//                {
//                    unclaimed.add(input.PrevHash, input.PrevIndex, block.Height);
//                }
//            }
//        }
//        for (var unspent : unspents)
//        {
//            if (unspent.Value.Count == 0)
//            {
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(unspent.Key));
//            }
//            else
//            {
//                batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(unspent.Key), unspent.Value.ToByteArray());
//            }
//        }
//        for (var spent : unclaimed)
//        {
//            if (spent.Value.Count == 0)
//            {
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Unclaimed).add(spent.Key));
//            }
//            else
//            {
//                using (MemoryStream ms = new MemoryStream(spent.Value.Count * UnclaimedItemSize))
//                using (BinaryWriter w = new BinaryWriter(ms))
//                {
//                    for (var pair : spent.Value)
//                    {
//                        w.Write(pair.Key);
//                        w.Write(pair.Value);
//                    }
//                    w.Flush();
//                    batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Unclaimed).add(spent.Key), ms.ToArray());
//                }
//            }
//        }
//        for (var unspent : unspent_votes)
//        {
//            if (unspent.Value.Count == 0)
//            {
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Vote).add(unspent.Key));
//            }
//            else
//            {
//                batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Vote).add(unspent.Key), unspent.Value.ToByteArray());
//            }
//        }
//        for (var quantity : quantities)
//        {
//            batch.Put(SliceBuilder.Begin(DataEntryPrefix.ST_QuantityIssued).add(quantity.Key), (GetQuantityIssued(quantity.Key) + quantity.Value).GetData());
//        }
//        current_block_hash = block.hash();
//        current_block_height = block.hash() == GENESIS_BLOCK.hash() ? 0 : current_block_height + 1;
//        batch.Put(SliceBuilder.Begin(DataEntryPrefix.SYS_CurrentBlock), SliceBuilder.Begin().add(block.hash()).add(current_block_height));
//        db.Write(WriteOptions.Default, batch);
    }

    private void PersistBlocks()
    {
//        while (!disposed)
//        {
//            new_block_event.WaitOne();
//            while (!disposed)
//            {
//                UInt256 hash;
//                synchronized (header_chain)
//                {
//                    TreeNode<Block> node = header_chain.Nodes[current_block_hash];
//                    if (header_index.Count <= node.Height + 1) break;
//                    hash = header_index[(int)node.Height + 1];
//                }
//                Block block;
//                synchronized (block_cache)
//                {
//                    if (!block_cache.ContainsKey(hash)) break;
//                    block = block_cache[hash];
//                }
//                Persist(block);
//                OnPersistCompleted(block);
//                synchronized (block_cache)
//                {
//                    block_cache.Remove(hash);
//                }
//            }
//        }
    }

    /*由于unclaimed无法恢复，所以Rollback没有办法实现，除非对每个交易输出都建立索引。
    /**
     * 将区块链的状态回滚到指定的位置
     * @param hash 要回滚到的区块的散列值
     */
//    private void Rollback(UInt256 hash)
//    {
//        if (hash == current_block_hash) return;
//        List<Block> blocks = new List<Block>();
//        UInt256 current = current_block_hash;
//        while (current != hash)
//        {
//            if (current == GENESIS_BLOCK.hash())
//                throw new InvalidOperationException();
//            Block block = GetBlockInternal(ReadOptions.Default, current);
//            blocks.add(block);
//            current = block.prevBlock;
//        }
//        WriteBatch batch = new WriteBatch();
//        for (Block block : blocks)
//        {
//            batch.Delete(SliceBuilder.Begin(DataEntryPrefix.DATA_Block).add(block.hash()));
//            for (Transaction tx : block.Transactions)
//            {
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.DATA_Transaction).add(tx.hash()));
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Enrollment).add(tx.hash()));
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(tx.hash()));
//                batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Vote).add(tx.hash()));
//                if (tx.Type == TransactionType.RegisterTransaction)
//                {
//                    RegisterTransaction reg_tx = (RegisterTransaction)tx;
//                    batch.Delete(SliceBuilder.Begin(DataEntryPrefix.IX_Asset).add(reg_tx.hash()));
//                }
//            }
//        }
//        HashSet<UInt256> tx_hashes = new HashSet<UInt256>(blocks.SelectMany(p => p.Transactions).Select(p => p.hash()));
//        for (var group : blocks.SelectMany(p => p.Transactions).SelectMany(p => p.GetAllInputs()).GroupBy(p => p.PrevHash).Where(g => !tx_hashes.Contains(g.Key)))
//        {
//            int height;
//            Transaction tx = GetTransaction(ReadOptions.Default, group.Key, out height);
//            Slice value = new byte[0];
//            db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(tx.hash()), out value);
//            Iterable<short> indexes = value.ToArray().GetUInt16Array().Union(group.Select(p => p.PrevIndex));
//            batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Unspent).add(tx.hash()), indexes.ToByteArray());
//            switch (tx.Type)
//            {
//                case TransactionType.EnrollmentTransaction:
//                    if (group.Any(p => p.PrevIndex == 0))
//                    {
//                        batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Enrollment).add(tx.hash()), true);
//                    }
//                    break;
//                case TransactionType.VotingTransaction:
//                    {
//                        TransactionInput[] votes = group.Where(p => tx.Outputs[p.PrevIndex].AssetId == AntShare.hash()).ToArray();
//                        if (votes.Length > 0)
//                        {
//                            value = new byte[0];
//                            db.TryGet(ReadOptions.Default, SliceBuilder.Begin(DataEntryPrefix.IX_Vote).add(tx.hash()), out value);
//                            indexes = value.ToArray().GetUInt16Array().Union(votes.Select(p => p.PrevIndex));
//                            batch.Put(SliceBuilder.Begin(DataEntryPrefix.IX_Vote).add(tx.hash()), indexes.ToByteArray());
//                        }
//                    }
//                    break;
//            }
//        }
//        for (var result : blocks.SelectMany(p => p.Transactions).Where(p => p.Type == TransactionType.IssueTransaction).SelectMany(p => p.GetTransactionResults()).Where(p => p.Amount < Fixed8.Zero).GroupBy(p => p.AssetId, (k, g) => new
//        {
//            AssetId = k,
//            Amount = -g.Sum(p => p.Amount)
//        }))
//        {
//            batch.Put(SliceBuilder.Begin(DataEntryPrefix.ST_QuantityIssued).add(result.AssetId), (GetQuantityIssued(result.AssetId) - result.Amount).GetData());
//        }
//        current_block_hash = current;
//        current_block_height -= (int)blocks.Count;
//        batch.Put(SliceBuilder.Begin(DataEntryPrefix.SYS_CurrentBlock), SliceBuilder.Begin().add(current_block_hash).add(current_block_height));
//        db.Write(WriteOptions.Default, batch);
//    }*/
}
