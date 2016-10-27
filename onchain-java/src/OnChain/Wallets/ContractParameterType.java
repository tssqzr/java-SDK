package OnChain.Wallets;

/**
 *  表示智能合约的参数类型
 */
public enum ContractParameterType
{
    /**
     *  签名
     */
    Signature,
    /**
     *  整数
     */
    Integer,
    /**
     *  160位散列值
     */
    Hash160,
    /**
     *  256位散列值
     */
    Hash256,
    /**
     *  字节数组
     */
    ByteArray
}