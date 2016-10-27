package OnChain.Implementations.Blockchains.LevelDB;

public enum DataEntryPrefix // : byte
{
    /**
     *  区块头表
     */
    DATA_HeaderList(0x00),

    /**
     *  区块
     */
    DATA_Block(0x01),

    /**
     *  交易
     */
    DATA_Transaction(0x02),

    /**
     *  当前区块，区块链的当前状态（包括所有的索引、统计信息）由该区块以及所有的前置区块共同决定
     */
    SYS_CurrentBlock(0x40),

    /**
     *  资产索引
     */
    IX_Asset(0x81),

    /**
     *  候选人索引
     */
    IX_Enrollment(0x84),

    /**
     *  未花费索引
     */
    IX_Unspent(0x90),

    /**
     *  未领取小蚁币的小蚁股
     */
    IX_Unclaimed(0x91),

    /**
     *  选票索引
     */
    IX_Vote(0x94),

    /**
     *  资产的已发行量
     */
    ST_QuantityIssued(0xc1),

    /**
     *  数据库版本
     */
    CFG_Version(0xf0),
    ;
    byte value;
    private DataEntryPrefix(int v)
    {
        value = (byte) v;
    }
    
    public byte get() {
        return value;
    }
}
