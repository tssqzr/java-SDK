package OnChain.Core;

/**
 * 资产类别
 */
public enum AssetType
{
    CreditFlag(0x40),
    DutyFlag(0x80),

    /**
     * 小蚁股
     */
    AntShare(0x00),

    /**
     * 小蚁币
     */
    AntCoin(0x01),

    /**
     * 法币
     */
    Currency(0x08),

    /**
     * 股权
     */
    Share(Byte.toUnsignedInt(DutyFlag.value()) | 0x10),

    Invoice(Byte.toUnsignedInt(DutyFlag.value()) | 0x18),

    Token(Byte.toUnsignedInt(CreditFlag.value()) | 0x20);

    private byte value;

    AssetType(int v)
    {
        value = (byte)v;
    }

    public byte value()
    {
        return value;
    }
    
    public static AssetType valueOf(byte v)
    {
    	for (AssetType e : AssetType.values())
    		if (e.value == v)
    			return e;
    	throw new IllegalArgumentException();
    }
}
