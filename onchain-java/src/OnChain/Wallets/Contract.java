package OnChain.Wallets;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Stream;

import org.bouncycastle.math.ec.ECPoint;

import OnChain.UInt160;
import OnChain.Core.Scripts.*;
import OnChain.IO.*;

/**
 *  所有合约的基类
 */
public class Contract implements Serializable
{
    /**
     *  合约脚本代码
     */
    public byte[] redeemScript;
    public ContractParameterType[] parameterList;
    /**
     *  公钥散列值，用于标识该合约在钱包中隶属于哪一个账户
     */
    public UInt160 publicKeyHash;

    private String _address;
    /**
     *  合约地址
     */
    public String address()
    {
        if (_address == null)
        {
            _address = Wallet.toAddress(scriptHash());
        }
        return _address;
    }

    public boolean isStandard()
    {
    	if (redeemScript.length != 35) return false;
        if (redeemScript[0] != 33 || redeemScript[34] != ScriptOp.OP_CHECKSIG.getByte())
            return false;
        return true;
    }

    private UInt160 _scriptHash;
    /**
     *  脚本散列值
     */
    public UInt160 scriptHash()
    {
        if (_scriptHash == null)
        {
            _scriptHash = Script.toScriptHash(redeemScript);
        }
        return _scriptHash;
    }
    
    public static Contract create(UInt160 publicKeyHash, ContractParameterType[] parameterList, byte[] redeemScript)
    {
    	Contract contract = new Contract();
    	contract.redeemScript = redeemScript;
    	contract.parameterList = parameterList;
    	contract.publicKeyHash = publicKeyHash;
    	return contract;
    }

    public static Contract createMultiSigContract(UInt160 publicKeyHash, int m, ECPoint ...publicKeys)
    {
        Contract contract = new Contract();
    	contract.redeemScript = createMultiSigRedeemScript(m, publicKeys);
    	contract.parameterList = Stream.generate(() -> ContractParameterType.Signature).limit(m).toArray(ContractParameterType[]::new);
    	contract.publicKeyHash = publicKeyHash;
    	return contract;
    }
    
    public static byte[] createMultiSigRedeemScript(int m, ECPoint ...publicKeys)
    {
        if (!(1 <= m && m <= publicKeys.length && publicKeys.length <= 1024))
            throw new IllegalArgumentException();
        try (ScriptBuilder sb = new ScriptBuilder())
        {
	        sb.push(BigInteger.valueOf(m));
	        for (ECPoint publicKey : Arrays.stream(publicKeys).sorted().toArray(ECPoint[]::new))
	        {
	            sb.push(publicKey.getEncoded(true));
	        }
	        sb.push(BigInteger.valueOf(publicKeys.length));
	        sb.add(ScriptOp.OP_CHECKMULTISIG);
	        return sb.toArray();
        }
    }
    
    public static Contract createSignatureContract(ECPoint publicKey)
    {
        Contract contract = new Contract();
    	contract.redeemScript = createSignatureRedeemScript(publicKey);
    	contract.parameterList = new ContractParameterType[] { ContractParameterType.Signature };
    	contract.publicKeyHash = Script.toScriptHash(publicKey.getEncoded(true));
    	return contract;
    }
    
    public static byte[] createSignatureRedeemScript(ECPoint publicKey)
    {
        try (ScriptBuilder sb = new ScriptBuilder())
        {
	        sb.push(publicKey.getEncoded(true));
	        sb.add(ScriptOp.OP_CHECKSIG);
	        return sb.toArray();
        }
    }
    
    @Override
    public void deserialize(BinaryReader reader) throws IOException
    {
        try
        {
			publicKeyHash = reader.readSerializable(UInt160.class);
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
			throw new RuntimeException(ex);
		}
    	byte[] buffer = reader.readVarBytes();
    	parameterList = new ContractParameterType[buffer.length];
    	for (int i = 0; i < parameterList.length; i++)
    		parameterList[i] = ContractParameterType.values()[buffer[i]];
    	redeemScript = reader.readVarBytes();
    }
    
    /**
     *  比较与另一个对象是否相等
     *  <param name="obj">另一个对象</param>
     *  <returns>返回比较的结果</returns>
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof Contract)) return false;
        return scriptHash().equals(((Contract) obj).scriptHash());
    }

    /**
     *  获得HashCode
     *  <returns>返回HashCode</returns>
     */
    @Override
    public int hashCode()
    {
        return scriptHash().hashCode();
    }
    
    @Override
    public void serialize(BinaryWriter writer) throws IOException
    {
    	writer.writeSerializable(publicKeyHash);
    	byte[] buffer = new byte[parameterList.length];
    	for (int i = 0; i < buffer.length; i++)
    		buffer[i] = (byte)parameterList[i].ordinal();
        writer.writeVarBytes(buffer);
        writer.writeVarBytes(redeemScript);
    }
}
