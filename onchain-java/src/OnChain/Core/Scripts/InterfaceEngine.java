package OnChain.Core.Scripts;
//package AntShares.Core.Scripts;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Stack;
//
//import org.bouncycastle.math.ec.ECPoint;
//
//import AntShares.UInt160;
//import AntShares.UInt256;
//import AntShares.Core.*;
//
//public class InterfaceEngine
//{
//    private final Stack<StackItem> stack;
//    private final Stack<StackItem> altStack;
//    private final Signable signable;
//
//    public InterfaceEngine(Stack<StackItem> stack, Stack<StackItem> altStack, Signable signable)
//    {
//        this.stack = stack;
//        this.altStack = altStack;
//        this.signable = signable;
//    }
//
//    // TODO
//    public boolean ExecuteOp(InterfaceOp code) throws Exception
//    {
//        switch (code)
//        {
//            case SYSTEM_NOW:
//                return SystemNow();
//            case SYSTEM_CURRENTTX:
//                return SystemCurrentTx();
//            case CHAIN_HEIGHT:
//                return ChainHeight();
//            case CHAIN_GETHEADER:
//                return ChainGetHeader();
//            case CHAIN_GETBLOCK:
//                return ChainGetBlock();
//            case CHAIN_GETTX:
//                return ChainGetTx();
//            case HEADER_HASH:
//                return HeaderHash();
//            case HEADER_VERSION:
//                return HeaderVersion();
//            case HEADER_PREVHASH:
//                return HeaderPrevHash();
//            case HEADER_MERKLEROOT:
//                return HeaderMerkleRoot();
//            case HEADER_TIMESTAMP:
//                return HeaderTimestamp();
//            case HEADER_NONCE:
//                return HeaderNonce();
//            case HEADER_NEXTMINER:
//                return HeaderNextMiner();
//            case BLOCK_TXCOUNT:
//                return BlockTxCount();
//            case BLOCK_TX:
//                return BlockTx();
//            case BLOCK_GETTX:
//                return BlockGetTx();
//            case TX_HASH:
//                return TxHash();
//            case TX_TYPE:
//                return TxType();
//            case ASSET_TYPE:
//                return AssetType();
//            case ASSET_AMOUNT:
//                return AssetAmount();
//            case ASSET_ISSUER:
//                return AssetIssuer();
//            case ASSET_ADMIN:
//                return AssetAdmin();
//            case ENROLL_PUBKEY:
//                return EnrollPubkey();
//            case VOTE_ENROLLMENTS:
//                return VoteEnrollments();
//            case TX_ATTRIBUTES:
//                return TxAttributes();
//            case TX_INPUTS:
//                return TxInputs();
//            case TX_OUTPUTS:
//                return TxOutputs();
//            case ATTR_USAGE:
//                return AttrUsage();
//            case ATTR_DATA:
//                return AttrData();
//            case TXIN_HASH:
//                return TxInHash();
//            case TXIN_INDEX:
//                return TxInIndex();
//            case TXOUT_ASSET:
//                return TxOutAsset();
//            case TXOUT_VALUE:
//                return TxOutValue();
//            case TXOUT_SCRIPTHASH:
//                return TxOutScriptHash();
//            default:
//                return false;
//        }
//    }
//
//    private boolean SystemNow()
//    {
//        // TODO
//        //stack.push(DateTime.Now.ToTimestamp());
//        return true;
//    }
//
//    private boolean SystemCurrentTx()
//    {
//        stack.push(new StackItem((Transaction) signable));
//        return true;
//    }
//
//    private boolean ChainHeight()
//    {
//        if (Blockchain.current() == null)
//        {
//            //stack.push(0);
//        }
//        else
//        {
//            //stack.push(Blockchain.getDefault().getHeight());
//        }
//        return true;
//    }
//
//    private boolean ChainGetHeader() throws Exception
//    {
//        if (stack.size() < 1) return false;
//        StackItem x = stack.pop();
//        byte[][] data = x.GetBytesArray();
//        List<Block> r = new ArrayList<Block>();
//        for (byte[] d : data)
//        {
//            switch (d.length)
//            {
//                case 4://sizeof(int):
//                    int height = 0;// TODO BitConverter.ToUInt32(d, 0);
//                    if (Blockchain.current() != null)
//                        r.add(Blockchain.current().getHeader(height));
//                    else if (height == 0)
//                        r.add(Blockchain.GENESIS_BLOCK.header());
//                    else
//                        r.add(null);
//                    break;
//                case 32:
//                    UInt256 hash = new UInt256(d);
//                    if (Blockchain.current() != null)
//                        r.add(Blockchain.current().getHeader(hash));
//                    else if (hash == Blockchain.GENESIS_BLOCK.hash())
//                        r.add(Blockchain.GENESIS_BLOCK.header());
//                    else
//                        r.add(null);
//                    break;
//                default:
//                    return false;
//            }
//        }
//        if (x.IsArray)
//            stack.push(new StackItem(r.toArray(new Block[r.size()])));
//        else
//            stack.push(new StackItem(r.get(0)));
//        return true;
//    }
//
//    private boolean ChainGetBlock() throws Exception
//    {
//        if (stack.size() < 1) return false;
//        StackItem x = stack.pop();
//        byte[][] data = x.GetBytesArray();
//        List<Block> r = new ArrayList<Block>();
//        for (byte[] d : data)
//        {
//            switch (d.length)
//            {
//                case 4://sizeof(int):
//                    int height = 0;// TODO BitConverter.ToUInt32(d, 0);
//                    if (Blockchain.current() != null)
//                        r.add(Blockchain.current().getBlock(height));
//                    else if (height == 0)
//                        r.add(Blockchain.GENESIS_BLOCK);
//                    else
//                        r.add(null);
//                    break;
//                case 32:
//                    UInt256 hash = new UInt256(d);
//                    if (Blockchain.current() != null)
//                        r.add(Blockchain.current().getBlock(hash));
//                    else if (hash == Blockchain.GENESIS_BLOCK.hash())
//                        r.add(Blockchain.GENESIS_BLOCK);
//                    else
//                        r.add(null);
//                    break;
//                default:
//                    return false;
//            }
//        }
//        if (x.IsArray)
//            stack.push(new StackItem(r.toArray(new Block[r.size()])));
//        else
//            stack.push(new StackItem(r.get(0)));
//        return true;
//    }
//
//    private boolean ChainGetTx()
//    {
//        if (stack.size() < 1) return false;
//        StackItem x = stack.pop();
//        // TODO
//        Transaction[] r = null;//Arrays.stream(x.GetArray()).map(p -> Blockchain.getDefault() == null ? Blockchain.getDefault().GetTransaction(p) : null).toArray();
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean HeaderHash()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        UInt256[] r = Arrays.stream(headers).map(p->p.hash()).toArray(UInt256[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean HeaderVersion()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        int[] r = Arrays.stream(headers).mapToInt(p -> p.version).toArray();
//        if (x.IsArray)
//        {
//            // TODO
//            //stack.push(r);
//        }
//        else
//        {
//            // TODO
//            //stack.push(r[0]);
//        }
//        return true;
//    }
//
//    private boolean HeaderPrevHash()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        UInt256[] r = Arrays.stream(headers).map(p -> p.prevBlock).toArray(UInt256[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean HeaderMerkleRoot()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        UInt256[] r = Arrays.stream(headers).map(p -> p.merkleRoot).toArray(UInt256[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean HeaderTimestamp()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        int[] r = Arrays.stream(headers).mapToInt(p -> p.timestamp).toArray();
//        // TODO
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean HeaderNonce()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        long[] r = Arrays.stream(headers).mapToLong(p -> p.nonce).toArray();
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean HeaderNextMiner()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] headers = x.GetArray();
//        if (Arrays.stream(headers).anyMatch(p -> p == null)) return false;
//        UInt160[] r = Arrays.stream(headers).map(p -> p.nextMiner).toArray(UInt160[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean BlockTxCount()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Block[] blocks = x.GetArray();
//        if (Arrays.stream(blocks).anyMatch(p -> p == null || p.isHeader())) return false;
//        int[] r = Arrays.stream(blocks).mapToInt(p -> p.transactions.length).toArray();
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean BlockTx()
//    {
//        if (altStack.size() < 1) return false;
//        Block block = altStack.peek().GetInterface();
//        if (block == null || block.isHeader()) return false;
//        stack.push(new StackItem(block.transactions));
//        return true;
//    }
//
//    private boolean BlockGetTx()
//    {
//        if (stack.size() < 1 || altStack.size() < 1) return false;
//        StackItem block_item = altStack.peek();
//        Block[] blocks = block_item.GetArray();
//        if (Arrays.stream(blocks).anyMatch(p -> p == null || p.isHeader())) return false;
//        StackItem index_item = stack.pop();
//        BigInteger[] indexes = index_item.GetIntArray();
//        if (block_item.IsArray && index_item.IsArray && blocks.length != indexes.length)
//            return false;
//        if (blocks.length == 1) {
//            Block[] temp = new Block[indexes.length];
//            Arrays.fill(temp, blocks[0]);
//            blocks = temp;
//        }
//        else if (indexes.length == 1) {
//            BigInteger[] temp = new BigInteger[blocks.length];
//            Arrays.fill(temp, indexes[0]);
//            indexes = temp;
//        }
//        // TODO Transaction[] tx = blocks.Zip(indexes, (b, i) -> i >= b.Transactions.length ? null : b.Transactions[(int)i]).toArray();
////        if (block_item.IsArray || index_item.IsArray)
////            stack.push(new StackItem(tx));
////        else
////            stack.push(new StackItem(tx[0]));
//        return true;
//    }
//
//    private boolean TxHash()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Transaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        UInt256[] r = Arrays.stream(tx).map(p -> p.hash()).toArray(UInt256[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean TxType()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        Transaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        // TODO
////        byte[][] r = Arrays.stream(tx).map(p -> new byte[] { (byte)p.type }).toArray();
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean AssetType()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        RegisterTransaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        // TODO
////        byte[][] r = Arrays.stream(tx).map(p -> new byte[] { (byte)p.AssetType }).toArray();
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean AssetAmount()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        RegisterTransaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        long[] r = Arrays.stream(tx).mapToLong(p -> p.amount.getData()).toArray();
//        // TODO
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean AssetIssuer()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        RegisterTransaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        ECPoint[] r = Arrays.stream(tx).map(p -> p.issuer).toArray(ECPoint[]::new);
//        // TODO
////        if (x.IsArray)
////            stack.push(new StackItem(r));
////        else
////            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean AssetAdmin()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        RegisterTransaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        UInt160[] r = Arrays.stream(tx).map(p -> p.admin).toArray(UInt160[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean EnrollPubkey()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        EnrollmentTransaction[] tx = x.GetArray();
//        if (Arrays.stream(tx).anyMatch(p -> p == null)) return false;
//        ECPoint[] r = Arrays.stream(tx).map(p -> p.publicKey).toArray(ECPoint[]::new);
////        if (x.IsArray)
////            stack.push(new StackItem(r));
////        else
////            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean VoteEnrollments()
//    {
//        if (altStack.size() < 1) return false;
//        VotingTransaction tx = altStack.pop().GetInterface();
//        if (tx == null) return false;
//        stack.push(new StackItem(tx.enrollments));
//        return true;
//    }
//
//    private boolean TxAttributes()
//    {
//        if (altStack.size() < 1) return false;
//        Transaction tx = altStack.pop().GetInterface();
//        if (tx == null) return false;
//        stack.push(new StackItem(tx.attributes));
//        return true;
//    }
//
//    private boolean TxInputs()
//    {
//        if (altStack.size() < 1) return false;
//        Transaction tx = altStack.pop().GetInterface();
//        if (tx == null) return false;
//        stack.push(new StackItem(tx.inputs));
//        return true;
//    }
//
//    private boolean TxOutputs()
//    {
//        if (altStack.size() < 1) return false;
//        Transaction tx = altStack.pop().GetInterface();
//        if (tx == null) return false;
//        stack.push(new StackItem(tx.outputs));
//        return true;
//    }
//
//    private boolean AttrUsage()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionAttribute[] attr = x.GetArray();
//        if (Arrays.stream(attr).anyMatch(p -> p == null)) return false;
//        byte[][] r = Arrays.stream(attr).map(p -> new byte[] { (byte)(p.usage.value()) }).toArray(byte[][]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean AttrData()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionAttribute[] attr = x.GetArray();
//        if (Arrays.stream(attr).anyMatch(p -> p == null)) return false;
//        byte[][] r = Arrays.stream(attr).map(p -> p.data).toArray(byte[][]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean TxInHash()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionInput[] inputs = x.GetArray();
//        if (Arrays.stream(inputs).anyMatch(p -> p == null)) return false;
//        UInt256[] r = Arrays.stream(inputs).map(p -> p.prevHash).toArray(UInt256[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean TxInIndex()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionInput[] inputs = x.GetArray();
//        if (Arrays.stream(inputs).anyMatch(p -> p == null)) return false;
//        int[] r = Arrays.stream(inputs).mapToInt(p -> (int)p.prevIndex).toArray();
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean TxOutAsset()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionOutput[] outputs = x.GetArray();
//        if (Arrays.stream(outputs).anyMatch(p -> p == null)) return false;
//        UInt256[] r = Arrays.stream(outputs).map(p -> p.assetId).toArray(UInt256[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//
//    private boolean TxOutValue()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionOutput[] outputs = x.GetArray();
//        if (Arrays.stream(outputs).anyMatch(p -> p == null)) return false;
//        long[] r = Arrays.stream(outputs).mapToLong(p -> p.value.getData()).toArray();
////        if (x.IsArray)
////            stack.push(r);
////        else
////            stack.push(r[0]);
//        return true;
//    }
//
//    private boolean TxOutScriptHash()
//    {
//        if (altStack.size() < 1) return false;
//        StackItem x = altStack.peek();
//        TransactionOutput[] outputs = x.GetArray();
//        if (Arrays.stream(outputs).anyMatch(p -> p == null)) return false;
//        UInt160[] r = Arrays.stream(outputs).map(p -> p.scriptHash).toArray(UInt160[]::new);
//        if (x.IsArray)
//            stack.push(new StackItem(r));
//        else
//            stack.push(new StackItem(r[0]));
//        return true;
//    }
//}